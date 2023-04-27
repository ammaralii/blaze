package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.domain.TestRun;
import com.venturedive.blaze.domain.TestRunDetailAttachment;
import com.venturedive.blaze.domain.TestRunDetails;
import com.venturedive.blaze.domain.TestRunStepDetails;
import com.venturedive.blaze.domain.TestStatus;
import com.venturedive.blaze.repository.TestRunDetailsRepository;
import com.venturedive.blaze.service.criteria.TestRunDetailsCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TestRunDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestRunDetailsResourceIT {

    private static final String DEFAULT_RESULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_DETAIL = "BBBBBBBBBB";

    private static final String DEFAULT_JIRA_ID = "AAAAAAAAAA";
    private static final String UPDATED_JIRA_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final Integer DEFAULT_EXECUTED_BY = 1;
    private static final Integer UPDATED_EXECUTED_BY = 2;
    private static final Integer SMALLER_EXECUTED_BY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/test-run-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestRunDetailsRepository testRunDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestRunDetailsMockMvc;

    private TestRunDetails testRunDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunDetails createEntity(EntityManager em) {
        TestRunDetails testRunDetails = new TestRunDetails()
            .resultDetail(DEFAULT_RESULT_DETAIL)
            .jiraId(DEFAULT_JIRA_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .executedBy(DEFAULT_EXECUTED_BY);
        return testRunDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunDetails createUpdatedEntity(EntityManager em) {
        TestRunDetails testRunDetails = new TestRunDetails()
            .resultDetail(UPDATED_RESULT_DETAIL)
            .jiraId(UPDATED_JIRA_ID)
            .createdBy(UPDATED_CREATED_BY)
            .executedBy(UPDATED_EXECUTED_BY);
        return testRunDetails;
    }

    @BeforeEach
    public void initTest() {
        testRunDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createTestRunDetails() throws Exception {
        int databaseSizeBeforeCreate = testRunDetailsRepository.findAll().size();
        // Create the TestRunDetails
        restTestRunDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRunDetails))
            )
            .andExpect(status().isCreated());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TestRunDetails testTestRunDetails = testRunDetailsList.get(testRunDetailsList.size() - 1);
        assertThat(testTestRunDetails.getResultDetail()).isEqualTo(DEFAULT_RESULT_DETAIL);
        assertThat(testTestRunDetails.getJiraId()).isEqualTo(DEFAULT_JIRA_ID);
        assertThat(testTestRunDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestRunDetails.getExecutedBy()).isEqualTo(DEFAULT_EXECUTED_BY);
    }

    @Test
    @Transactional
    void createTestRunDetailsWithExistingId() throws Exception {
        // Create the TestRunDetails with an existing ID
        testRunDetails.setId(1L);

        int databaseSizeBeforeCreate = testRunDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestRunDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRunDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestRunDetails() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList
        restTestRunDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].resultDetail").value(hasItem(DEFAULT_RESULT_DETAIL)))
            .andExpect(jsonPath("$.[*].jiraId").value(hasItem(DEFAULT_JIRA_ID)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].executedBy").value(hasItem(DEFAULT_EXECUTED_BY)));
    }

    @Test
    @Transactional
    void getTestRunDetails() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get the testRunDetails
        restTestRunDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, testRunDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testRunDetails.getId().intValue()))
            .andExpect(jsonPath("$.resultDetail").value(DEFAULT_RESULT_DETAIL))
            .andExpect(jsonPath("$.jiraId").value(DEFAULT_JIRA_ID))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.executedBy").value(DEFAULT_EXECUTED_BY));
    }

    @Test
    @Transactional
    void getTestRunDetailsByIdFiltering() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        Long id = testRunDetails.getId();

        defaultTestRunDetailsShouldBeFound("id.equals=" + id);
        defaultTestRunDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultTestRunDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestRunDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestRunDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestRunDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByResultDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where resultDetail equals to DEFAULT_RESULT_DETAIL
        defaultTestRunDetailsShouldBeFound("resultDetail.equals=" + DEFAULT_RESULT_DETAIL);

        // Get all the testRunDetailsList where resultDetail equals to UPDATED_RESULT_DETAIL
        defaultTestRunDetailsShouldNotBeFound("resultDetail.equals=" + UPDATED_RESULT_DETAIL);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByResultDetailIsInShouldWork() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where resultDetail in DEFAULT_RESULT_DETAIL or UPDATED_RESULT_DETAIL
        defaultTestRunDetailsShouldBeFound("resultDetail.in=" + DEFAULT_RESULT_DETAIL + "," + UPDATED_RESULT_DETAIL);

        // Get all the testRunDetailsList where resultDetail equals to UPDATED_RESULT_DETAIL
        defaultTestRunDetailsShouldNotBeFound("resultDetail.in=" + UPDATED_RESULT_DETAIL);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByResultDetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where resultDetail is not null
        defaultTestRunDetailsShouldBeFound("resultDetail.specified=true");

        // Get all the testRunDetailsList where resultDetail is null
        defaultTestRunDetailsShouldNotBeFound("resultDetail.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByResultDetailContainsSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where resultDetail contains DEFAULT_RESULT_DETAIL
        defaultTestRunDetailsShouldBeFound("resultDetail.contains=" + DEFAULT_RESULT_DETAIL);

        // Get all the testRunDetailsList where resultDetail contains UPDATED_RESULT_DETAIL
        defaultTestRunDetailsShouldNotBeFound("resultDetail.contains=" + UPDATED_RESULT_DETAIL);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByResultDetailNotContainsSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where resultDetail does not contain DEFAULT_RESULT_DETAIL
        defaultTestRunDetailsShouldNotBeFound("resultDetail.doesNotContain=" + DEFAULT_RESULT_DETAIL);

        // Get all the testRunDetailsList where resultDetail does not contain UPDATED_RESULT_DETAIL
        defaultTestRunDetailsShouldBeFound("resultDetail.doesNotContain=" + UPDATED_RESULT_DETAIL);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByJiraIdIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where jiraId equals to DEFAULT_JIRA_ID
        defaultTestRunDetailsShouldBeFound("jiraId.equals=" + DEFAULT_JIRA_ID);

        // Get all the testRunDetailsList where jiraId equals to UPDATED_JIRA_ID
        defaultTestRunDetailsShouldNotBeFound("jiraId.equals=" + UPDATED_JIRA_ID);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByJiraIdIsInShouldWork() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where jiraId in DEFAULT_JIRA_ID or UPDATED_JIRA_ID
        defaultTestRunDetailsShouldBeFound("jiraId.in=" + DEFAULT_JIRA_ID + "," + UPDATED_JIRA_ID);

        // Get all the testRunDetailsList where jiraId equals to UPDATED_JIRA_ID
        defaultTestRunDetailsShouldNotBeFound("jiraId.in=" + UPDATED_JIRA_ID);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByJiraIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where jiraId is not null
        defaultTestRunDetailsShouldBeFound("jiraId.specified=true");

        // Get all the testRunDetailsList where jiraId is null
        defaultTestRunDetailsShouldNotBeFound("jiraId.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByJiraIdContainsSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where jiraId contains DEFAULT_JIRA_ID
        defaultTestRunDetailsShouldBeFound("jiraId.contains=" + DEFAULT_JIRA_ID);

        // Get all the testRunDetailsList where jiraId contains UPDATED_JIRA_ID
        defaultTestRunDetailsShouldNotBeFound("jiraId.contains=" + UPDATED_JIRA_ID);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByJiraIdNotContainsSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where jiraId does not contain DEFAULT_JIRA_ID
        defaultTestRunDetailsShouldNotBeFound("jiraId.doesNotContain=" + DEFAULT_JIRA_ID);

        // Get all the testRunDetailsList where jiraId does not contain UPDATED_JIRA_ID
        defaultTestRunDetailsShouldBeFound("jiraId.doesNotContain=" + UPDATED_JIRA_ID);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where createdBy equals to DEFAULT_CREATED_BY
        defaultTestRunDetailsShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the testRunDetailsList where createdBy equals to UPDATED_CREATED_BY
        defaultTestRunDetailsShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTestRunDetailsShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the testRunDetailsList where createdBy equals to UPDATED_CREATED_BY
        defaultTestRunDetailsShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where createdBy is not null
        defaultTestRunDetailsShouldBeFound("createdBy.specified=true");

        // Get all the testRunDetailsList where createdBy is null
        defaultTestRunDetailsShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTestRunDetailsShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testRunDetailsList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTestRunDetailsShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTestRunDetailsShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testRunDetailsList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTestRunDetailsShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where createdBy is less than DEFAULT_CREATED_BY
        defaultTestRunDetailsShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the testRunDetailsList where createdBy is less than UPDATED_CREATED_BY
        defaultTestRunDetailsShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTestRunDetailsShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the testRunDetailsList where createdBy is greater than SMALLER_CREATED_BY
        defaultTestRunDetailsShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByExecutedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where executedBy equals to DEFAULT_EXECUTED_BY
        defaultTestRunDetailsShouldBeFound("executedBy.equals=" + DEFAULT_EXECUTED_BY);

        // Get all the testRunDetailsList where executedBy equals to UPDATED_EXECUTED_BY
        defaultTestRunDetailsShouldNotBeFound("executedBy.equals=" + UPDATED_EXECUTED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByExecutedByIsInShouldWork() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where executedBy in DEFAULT_EXECUTED_BY or UPDATED_EXECUTED_BY
        defaultTestRunDetailsShouldBeFound("executedBy.in=" + DEFAULT_EXECUTED_BY + "," + UPDATED_EXECUTED_BY);

        // Get all the testRunDetailsList where executedBy equals to UPDATED_EXECUTED_BY
        defaultTestRunDetailsShouldNotBeFound("executedBy.in=" + UPDATED_EXECUTED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByExecutedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where executedBy is not null
        defaultTestRunDetailsShouldBeFound("executedBy.specified=true");

        // Get all the testRunDetailsList where executedBy is null
        defaultTestRunDetailsShouldNotBeFound("executedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByExecutedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where executedBy is greater than or equal to DEFAULT_EXECUTED_BY
        defaultTestRunDetailsShouldBeFound("executedBy.greaterThanOrEqual=" + DEFAULT_EXECUTED_BY);

        // Get all the testRunDetailsList where executedBy is greater than or equal to UPDATED_EXECUTED_BY
        defaultTestRunDetailsShouldNotBeFound("executedBy.greaterThanOrEqual=" + UPDATED_EXECUTED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByExecutedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where executedBy is less than or equal to DEFAULT_EXECUTED_BY
        defaultTestRunDetailsShouldBeFound("executedBy.lessThanOrEqual=" + DEFAULT_EXECUTED_BY);

        // Get all the testRunDetailsList where executedBy is less than or equal to SMALLER_EXECUTED_BY
        defaultTestRunDetailsShouldNotBeFound("executedBy.lessThanOrEqual=" + SMALLER_EXECUTED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByExecutedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where executedBy is less than DEFAULT_EXECUTED_BY
        defaultTestRunDetailsShouldNotBeFound("executedBy.lessThan=" + DEFAULT_EXECUTED_BY);

        // Get all the testRunDetailsList where executedBy is less than UPDATED_EXECUTED_BY
        defaultTestRunDetailsShouldBeFound("executedBy.lessThan=" + UPDATED_EXECUTED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByExecutedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        // Get all the testRunDetailsList where executedBy is greater than DEFAULT_EXECUTED_BY
        defaultTestRunDetailsShouldNotBeFound("executedBy.greaterThan=" + DEFAULT_EXECUTED_BY);

        // Get all the testRunDetailsList where executedBy is greater than SMALLER_EXECUTED_BY
        defaultTestRunDetailsShouldBeFound("executedBy.greaterThan=" + SMALLER_EXECUTED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByTestRunIsEqualToSomething() throws Exception {
        TestRun testRun;
        if (TestUtil.findAll(em, TestRun.class).isEmpty()) {
            testRunDetailsRepository.saveAndFlush(testRunDetails);
            testRun = TestRunResourceIT.createEntity(em);
        } else {
            testRun = TestUtil.findAll(em, TestRun.class).get(0);
        }
        em.persist(testRun);
        em.flush();
        testRunDetails.setTestRun(testRun);
        testRunDetailsRepository.saveAndFlush(testRunDetails);
        Long testRunId = testRun.getId();

        // Get all the testRunDetailsList where testRun equals to testRunId
        defaultTestRunDetailsShouldBeFound("testRunId.equals=" + testRunId);

        // Get all the testRunDetailsList where testRun equals to (testRunId + 1)
        defaultTestRunDetailsShouldNotBeFound("testRunId.equals=" + (testRunId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByTestCaseIsEqualToSomething() throws Exception {
        TestCase testCase;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testRunDetailsRepository.saveAndFlush(testRunDetails);
            testCase = TestCaseResourceIT.createEntity(em);
        } else {
            testCase = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testCase);
        em.flush();
        testRunDetails.setTestCase(testCase);
        testRunDetailsRepository.saveAndFlush(testRunDetails);
        Long testCaseId = testCase.getId();

        // Get all the testRunDetailsList where testCase equals to testCaseId
        defaultTestRunDetailsShouldBeFound("testCaseId.equals=" + testCaseId);

        // Get all the testRunDetailsList where testCase equals to (testCaseId + 1)
        defaultTestRunDetailsShouldNotBeFound("testCaseId.equals=" + (testCaseId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByStatusIsEqualToSomething() throws Exception {
        TestStatus status;
        if (TestUtil.findAll(em, TestStatus.class).isEmpty()) {
            testRunDetailsRepository.saveAndFlush(testRunDetails);
            status = TestStatusResourceIT.createEntity(em);
        } else {
            status = TestUtil.findAll(em, TestStatus.class).get(0);
        }
        em.persist(status);
        em.flush();
        testRunDetails.setStatus(status);
        testRunDetailsRepository.saveAndFlush(testRunDetails);
        Long statusId = status.getId();

        // Get all the testRunDetailsList where status equals to statusId
        defaultTestRunDetailsShouldBeFound("statusId.equals=" + statusId);

        // Get all the testRunDetailsList where status equals to (statusId + 1)
        defaultTestRunDetailsShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByTestrundetailattachmentTestrundetailIsEqualToSomething() throws Exception {
        TestRunDetailAttachment testrundetailattachmentTestrundetail;
        if (TestUtil.findAll(em, TestRunDetailAttachment.class).isEmpty()) {
            testRunDetailsRepository.saveAndFlush(testRunDetails);
            testrundetailattachmentTestrundetail = TestRunDetailAttachmentResourceIT.createEntity(em);
        } else {
            testrundetailattachmentTestrundetail = TestUtil.findAll(em, TestRunDetailAttachment.class).get(0);
        }
        em.persist(testrundetailattachmentTestrundetail);
        em.flush();
        testRunDetails.addTestrundetailattachmentTestrundetail(testrundetailattachmentTestrundetail);
        testRunDetailsRepository.saveAndFlush(testRunDetails);
        Long testrundetailattachmentTestrundetailId = testrundetailattachmentTestrundetail.getId();

        // Get all the testRunDetailsList where testrundetailattachmentTestrundetail equals to testrundetailattachmentTestrundetailId
        defaultTestRunDetailsShouldBeFound("testrundetailattachmentTestrundetailId.equals=" + testrundetailattachmentTestrundetailId);

        // Get all the testRunDetailsList where testrundetailattachmentTestrundetail equals to (testrundetailattachmentTestrundetailId + 1)
        defaultTestRunDetailsShouldNotBeFound(
            "testrundetailattachmentTestrundetailId.equals=" + (testrundetailattachmentTestrundetailId + 1)
        );
    }

    @Test
    @Transactional
    void getAllTestRunDetailsByTestrunstepdetailsTestrundetailIsEqualToSomething() throws Exception {
        TestRunStepDetails testrunstepdetailsTestrundetail;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testRunDetailsRepository.saveAndFlush(testRunDetails);
            testrunstepdetailsTestrundetail = TestRunStepDetailsResourceIT.createEntity(em);
        } else {
            testrunstepdetailsTestrundetail = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        em.persist(testrunstepdetailsTestrundetail);
        em.flush();
        testRunDetails.addTestrunstepdetailsTestrundetail(testrunstepdetailsTestrundetail);
        testRunDetailsRepository.saveAndFlush(testRunDetails);
        Long testrunstepdetailsTestrundetailId = testrunstepdetailsTestrundetail.getId();

        // Get all the testRunDetailsList where testrunstepdetailsTestrundetail equals to testrunstepdetailsTestrundetailId
        defaultTestRunDetailsShouldBeFound("testrunstepdetailsTestrundetailId.equals=" + testrunstepdetailsTestrundetailId);

        // Get all the testRunDetailsList where testrunstepdetailsTestrundetail equals to (testrunstepdetailsTestrundetailId + 1)
        defaultTestRunDetailsShouldNotBeFound("testrunstepdetailsTestrundetailId.equals=" + (testrunstepdetailsTestrundetailId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestRunDetailsShouldBeFound(String filter) throws Exception {
        restTestRunDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].resultDetail").value(hasItem(DEFAULT_RESULT_DETAIL)))
            .andExpect(jsonPath("$.[*].jiraId").value(hasItem(DEFAULT_JIRA_ID)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].executedBy").value(hasItem(DEFAULT_EXECUTED_BY)));

        // Check, that the count call also returns 1
        restTestRunDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestRunDetailsShouldNotBeFound(String filter) throws Exception {
        restTestRunDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestRunDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestRunDetails() throws Exception {
        // Get the testRunDetails
        restTestRunDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestRunDetails() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        int databaseSizeBeforeUpdate = testRunDetailsRepository.findAll().size();

        // Update the testRunDetails
        TestRunDetails updatedTestRunDetails = testRunDetailsRepository.findById(testRunDetails.getId()).get();
        // Disconnect from session so that the updates on updatedTestRunDetails are not directly saved in db
        em.detach(updatedTestRunDetails);
        updatedTestRunDetails
            .resultDetail(UPDATED_RESULT_DETAIL)
            .jiraId(UPDATED_JIRA_ID)
            .createdBy(UPDATED_CREATED_BY)
            .executedBy(UPDATED_EXECUTED_BY);

        restTestRunDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestRunDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestRunDetails))
            )
            .andExpect(status().isOk());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeUpdate);
        TestRunDetails testTestRunDetails = testRunDetailsList.get(testRunDetailsList.size() - 1);
        assertThat(testTestRunDetails.getResultDetail()).isEqualTo(UPDATED_RESULT_DETAIL);
        assertThat(testTestRunDetails.getJiraId()).isEqualTo(UPDATED_JIRA_ID);
        assertThat(testTestRunDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestRunDetails.getExecutedBy()).isEqualTo(UPDATED_EXECUTED_BY);
    }

    @Test
    @Transactional
    void putNonExistingTestRunDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailsRepository.findAll().size();
        testRunDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testRunDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestRunDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailsRepository.findAll().size();
        testRunDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestRunDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailsRepository.findAll().size();
        testRunDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRunDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestRunDetailsWithPatch() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        int databaseSizeBeforeUpdate = testRunDetailsRepository.findAll().size();

        // Update the testRunDetails using partial update
        TestRunDetails partialUpdatedTestRunDetails = new TestRunDetails();
        partialUpdatedTestRunDetails.setId(testRunDetails.getId());

        partialUpdatedTestRunDetails.jiraId(UPDATED_JIRA_ID);

        restTestRunDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunDetails))
            )
            .andExpect(status().isOk());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeUpdate);
        TestRunDetails testTestRunDetails = testRunDetailsList.get(testRunDetailsList.size() - 1);
        assertThat(testTestRunDetails.getResultDetail()).isEqualTo(DEFAULT_RESULT_DETAIL);
        assertThat(testTestRunDetails.getJiraId()).isEqualTo(UPDATED_JIRA_ID);
        assertThat(testTestRunDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestRunDetails.getExecutedBy()).isEqualTo(DEFAULT_EXECUTED_BY);
    }

    @Test
    @Transactional
    void fullUpdateTestRunDetailsWithPatch() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        int databaseSizeBeforeUpdate = testRunDetailsRepository.findAll().size();

        // Update the testRunDetails using partial update
        TestRunDetails partialUpdatedTestRunDetails = new TestRunDetails();
        partialUpdatedTestRunDetails.setId(testRunDetails.getId());

        partialUpdatedTestRunDetails
            .resultDetail(UPDATED_RESULT_DETAIL)
            .jiraId(UPDATED_JIRA_ID)
            .createdBy(UPDATED_CREATED_BY)
            .executedBy(UPDATED_EXECUTED_BY);

        restTestRunDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunDetails))
            )
            .andExpect(status().isOk());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeUpdate);
        TestRunDetails testTestRunDetails = testRunDetailsList.get(testRunDetailsList.size() - 1);
        assertThat(testTestRunDetails.getResultDetail()).isEqualTo(UPDATED_RESULT_DETAIL);
        assertThat(testTestRunDetails.getJiraId()).isEqualTo(UPDATED_JIRA_ID);
        assertThat(testTestRunDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestRunDetails.getExecutedBy()).isEqualTo(UPDATED_EXECUTED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingTestRunDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailsRepository.findAll().size();
        testRunDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testRunDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestRunDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailsRepository.findAll().size();
        testRunDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestRunDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailsRepository.findAll().size();
        testRunDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testRunDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunDetails in the database
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestRunDetails() throws Exception {
        // Initialize the database
        testRunDetailsRepository.saveAndFlush(testRunDetails);

        int databaseSizeBeforeDelete = testRunDetailsRepository.findAll().size();

        // Delete the testRunDetails
        restTestRunDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testRunDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestRunDetails> testRunDetailsList = testRunDetailsRepository.findAll();
        assertThat(testRunDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
