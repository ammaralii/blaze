package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.Milestone;
import com.venturedive.blaze.domain.Section;
import com.venturedive.blaze.domain.Template;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.domain.TestCaseAttachment;
import com.venturedive.blaze.domain.TestCaseField;
import com.venturedive.blaze.domain.TestCasePriority;
import com.venturedive.blaze.domain.TestLevel;
import com.venturedive.blaze.domain.TestRunDetails;
import com.venturedive.blaze.domain.TestSuite;
import com.venturedive.blaze.repository.TestCaseRepository;
import com.venturedive.blaze.service.TestCaseService;
import com.venturedive.blaze.service.criteria.TestCaseCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TestCaseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TestCaseResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTIMATE = "AAAAAAAAAA";
    private static final String UPDATED_ESTIMATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;
    private static final Integer SMALLER_UPDATED_BY = 1 - 1;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PRECONDITION = "AAAAAAAAAA";
    private static final String UPDATED_PRECONDITION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_AUTOMATED = false;
    private static final Boolean UPDATED_IS_AUTOMATED = true;

    private static final String ENTITY_API_URL = "/api/test-cases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Mock
    private TestCaseRepository testCaseRepositoryMock;

    @Mock
    private TestCaseService testCaseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseMockMvc;

    private TestCase testCase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createEntity(EntityManager em) {
        TestCase testCase = new TestCase()
            .title(DEFAULT_TITLE)
            .estimate(DEFAULT_ESTIMATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .precondition(DEFAULT_PRECONDITION)
            .description(DEFAULT_DESCRIPTION)
            .isAutomated(DEFAULT_IS_AUTOMATED);
        // Add required entity
        TestCasePriority testCasePriority;
        if (TestUtil.findAll(em, TestCasePriority.class).isEmpty()) {
            testCasePriority = TestCasePriorityResourceIT.createEntity(em);
            em.persist(testCasePriority);
            em.flush();
        } else {
            testCasePriority = TestUtil.findAll(em, TestCasePriority.class).get(0);
        }
        testCase.setPriority(testCasePriority);
        return testCase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createUpdatedEntity(EntityManager em) {
        TestCase testCase = new TestCase()
            .title(UPDATED_TITLE)
            .estimate(UPDATED_ESTIMATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .precondition(UPDATED_PRECONDITION)
            .description(UPDATED_DESCRIPTION)
            .isAutomated(UPDATED_IS_AUTOMATED);
        // Add required entity
        TestCasePriority testCasePriority;
        if (TestUtil.findAll(em, TestCasePriority.class).isEmpty()) {
            testCasePriority = TestCasePriorityResourceIT.createUpdatedEntity(em);
            em.persist(testCasePriority);
            em.flush();
        } else {
            testCasePriority = TestUtil.findAll(em, TestCasePriority.class).get(0);
        }
        testCase.setPriority(testCasePriority);
        return testCase;
    }

    @BeforeEach
    public void initTest() {
        testCase = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCase() throws Exception {
        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();
        // Create the TestCase
        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCase)))
            .andExpect(status().isCreated());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate + 1);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTestCase.getEstimate()).isEqualTo(DEFAULT_ESTIMATE);
        assertThat(testTestCase.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestCase.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestCase.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTestCase.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testTestCase.getPrecondition()).isEqualTo(DEFAULT_PRECONDITION);
        assertThat(testTestCase.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestCase.getIsAutomated()).isEqualTo(DEFAULT_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void createTestCaseWithExistingId() throws Exception {
        // Create the TestCase with an existing ID
        testCase.setId(1L);

        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCase)))
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCases() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].estimate").value(hasItem(DEFAULT_ESTIMATE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].precondition").value(hasItem(DEFAULT_PRECONDITION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isAutomated").value(hasItem(DEFAULT_IS_AUTOMATED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestCasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(testCaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestCaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(testCaseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestCasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(testCaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestCaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(testCaseRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get the testCase
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL_ID, testCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCase.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.estimate").value(DEFAULT_ESTIMATE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.precondition").value(DEFAULT_PRECONDITION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isAutomated").value(DEFAULT_IS_AUTOMATED.booleanValue()));
    }

    @Test
    @Transactional
    void getTestCasesByIdFiltering() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        Long id = testCase.getId();

        defaultTestCaseShouldBeFound("id.equals=" + id);
        defaultTestCaseShouldNotBeFound("id.notEquals=" + id);

        defaultTestCaseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCaseShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCaseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCaseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where title equals to DEFAULT_TITLE
        defaultTestCaseShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the testCaseList where title equals to UPDATED_TITLE
        defaultTestCaseShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultTestCaseShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the testCaseList where title equals to UPDATED_TITLE
        defaultTestCaseShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where title is not null
        defaultTestCaseShouldBeFound("title.specified=true");

        // Get all the testCaseList where title is null
        defaultTestCaseShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleContainsSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where title contains DEFAULT_TITLE
        defaultTestCaseShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the testCaseList where title contains UPDATED_TITLE
        defaultTestCaseShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where title does not contain DEFAULT_TITLE
        defaultTestCaseShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the testCaseList where title does not contain UPDATED_TITLE
        defaultTestCaseShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where estimate equals to DEFAULT_ESTIMATE
        defaultTestCaseShouldBeFound("estimate.equals=" + DEFAULT_ESTIMATE);

        // Get all the testCaseList where estimate equals to UPDATED_ESTIMATE
        defaultTestCaseShouldNotBeFound("estimate.equals=" + UPDATED_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where estimate in DEFAULT_ESTIMATE or UPDATED_ESTIMATE
        defaultTestCaseShouldBeFound("estimate.in=" + DEFAULT_ESTIMATE + "," + UPDATED_ESTIMATE);

        // Get all the testCaseList where estimate equals to UPDATED_ESTIMATE
        defaultTestCaseShouldNotBeFound("estimate.in=" + UPDATED_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where estimate is not null
        defaultTestCaseShouldBeFound("estimate.specified=true");

        // Get all the testCaseList where estimate is null
        defaultTestCaseShouldNotBeFound("estimate.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateContainsSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where estimate contains DEFAULT_ESTIMATE
        defaultTestCaseShouldBeFound("estimate.contains=" + DEFAULT_ESTIMATE);

        // Get all the testCaseList where estimate contains UPDATED_ESTIMATE
        defaultTestCaseShouldNotBeFound("estimate.contains=" + UPDATED_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateNotContainsSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where estimate does not contain DEFAULT_ESTIMATE
        defaultTestCaseShouldNotBeFound("estimate.doesNotContain=" + DEFAULT_ESTIMATE);

        // Get all the testCaseList where estimate does not contain UPDATED_ESTIMATE
        defaultTestCaseShouldBeFound("estimate.doesNotContain=" + UPDATED_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdBy equals to DEFAULT_CREATED_BY
        defaultTestCaseShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the testCaseList where createdBy equals to UPDATED_CREATED_BY
        defaultTestCaseShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTestCaseShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the testCaseList where createdBy equals to UPDATED_CREATED_BY
        defaultTestCaseShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdBy is not null
        defaultTestCaseShouldBeFound("createdBy.specified=true");

        // Get all the testCaseList where createdBy is null
        defaultTestCaseShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTestCaseShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testCaseList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTestCaseShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTestCaseShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testCaseList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTestCaseShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdBy is less than DEFAULT_CREATED_BY
        defaultTestCaseShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the testCaseList where createdBy is less than UPDATED_CREATED_BY
        defaultTestCaseShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTestCaseShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the testCaseList where createdBy is greater than SMALLER_CREATED_BY
        defaultTestCaseShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdAt equals to DEFAULT_CREATED_AT
        defaultTestCaseShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the testCaseList where createdAt equals to UPDATED_CREATED_AT
        defaultTestCaseShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTestCaseShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the testCaseList where createdAt equals to UPDATED_CREATED_AT
        defaultTestCaseShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where createdAt is not null
        defaultTestCaseShouldBeFound("createdAt.specified=true");

        // Get all the testCaseList where createdAt is null
        defaultTestCaseShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultTestCaseShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the testCaseList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTestCaseShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultTestCaseShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the testCaseList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTestCaseShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedBy is not null
        defaultTestCaseShouldBeFound("updatedBy.specified=true");

        // Get all the testCaseList where updatedBy is null
        defaultTestCaseShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultTestCaseShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the testCaseList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultTestCaseShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultTestCaseShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the testCaseList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultTestCaseShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultTestCaseShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the testCaseList where updatedBy is less than UPDATED_UPDATED_BY
        defaultTestCaseShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultTestCaseShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the testCaseList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultTestCaseShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTestCaseShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the testCaseList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestCaseShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTestCaseShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the testCaseList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestCaseShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where updatedAt is not null
        defaultTestCaseShouldBeFound("updatedAt.specified=true");

        // Get all the testCaseList where updatedAt is null
        defaultTestCaseShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where precondition equals to DEFAULT_PRECONDITION
        defaultTestCaseShouldBeFound("precondition.equals=" + DEFAULT_PRECONDITION);

        // Get all the testCaseList where precondition equals to UPDATED_PRECONDITION
        defaultTestCaseShouldNotBeFound("precondition.equals=" + UPDATED_PRECONDITION);
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where precondition in DEFAULT_PRECONDITION or UPDATED_PRECONDITION
        defaultTestCaseShouldBeFound("precondition.in=" + DEFAULT_PRECONDITION + "," + UPDATED_PRECONDITION);

        // Get all the testCaseList where precondition equals to UPDATED_PRECONDITION
        defaultTestCaseShouldNotBeFound("precondition.in=" + UPDATED_PRECONDITION);
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where precondition is not null
        defaultTestCaseShouldBeFound("precondition.specified=true");

        // Get all the testCaseList where precondition is null
        defaultTestCaseShouldNotBeFound("precondition.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionContainsSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where precondition contains DEFAULT_PRECONDITION
        defaultTestCaseShouldBeFound("precondition.contains=" + DEFAULT_PRECONDITION);

        // Get all the testCaseList where precondition contains UPDATED_PRECONDITION
        defaultTestCaseShouldNotBeFound("precondition.contains=" + UPDATED_PRECONDITION);
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionNotContainsSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where precondition does not contain DEFAULT_PRECONDITION
        defaultTestCaseShouldNotBeFound("precondition.doesNotContain=" + DEFAULT_PRECONDITION);

        // Get all the testCaseList where precondition does not contain UPDATED_PRECONDITION
        defaultTestCaseShouldBeFound("precondition.doesNotContain=" + UPDATED_PRECONDITION);
    }

    @Test
    @Transactional
    void getAllTestCasesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where description equals to DEFAULT_DESCRIPTION
        defaultTestCaseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the testCaseList where description equals to UPDATED_DESCRIPTION
        defaultTestCaseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestCasesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTestCaseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the testCaseList where description equals to UPDATED_DESCRIPTION
        defaultTestCaseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestCasesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where description is not null
        defaultTestCaseShouldBeFound("description.specified=true");

        // Get all the testCaseList where description is null
        defaultTestCaseShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where description contains DEFAULT_DESCRIPTION
        defaultTestCaseShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the testCaseList where description contains UPDATED_DESCRIPTION
        defaultTestCaseShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestCasesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where description does not contain DEFAULT_DESCRIPTION
        defaultTestCaseShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the testCaseList where description does not contain UPDATED_DESCRIPTION
        defaultTestCaseShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestCasesByIsAutomatedIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where isAutomated equals to DEFAULT_IS_AUTOMATED
        defaultTestCaseShouldBeFound("isAutomated.equals=" + DEFAULT_IS_AUTOMATED);

        // Get all the testCaseList where isAutomated equals to UPDATED_IS_AUTOMATED
        defaultTestCaseShouldNotBeFound("isAutomated.equals=" + UPDATED_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void getAllTestCasesByIsAutomatedIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where isAutomated in DEFAULT_IS_AUTOMATED or UPDATED_IS_AUTOMATED
        defaultTestCaseShouldBeFound("isAutomated.in=" + DEFAULT_IS_AUTOMATED + "," + UPDATED_IS_AUTOMATED);

        // Get all the testCaseList where isAutomated equals to UPDATED_IS_AUTOMATED
        defaultTestCaseShouldNotBeFound("isAutomated.in=" + UPDATED_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void getAllTestCasesByIsAutomatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList where isAutomated is not null
        defaultTestCaseShouldBeFound("isAutomated.specified=true");

        // Get all the testCaseList where isAutomated is null
        defaultTestCaseShouldNotBeFound("isAutomated.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByTestSuiteIsEqualToSomething() throws Exception {
        TestSuite testSuite;
        if (TestUtil.findAll(em, TestSuite.class).isEmpty()) {
            testCaseRepository.saveAndFlush(testCase);
            testSuite = TestSuiteResourceIT.createEntity(em);
        } else {
            testSuite = TestUtil.findAll(em, TestSuite.class).get(0);
        }
        em.persist(testSuite);
        em.flush();
        testCase.setTestSuite(testSuite);
        testCaseRepository.saveAndFlush(testCase);
        Long testSuiteId = testSuite.getId();

        // Get all the testCaseList where testSuite equals to testSuiteId
        defaultTestCaseShouldBeFound("testSuiteId.equals=" + testSuiteId);

        // Get all the testCaseList where testSuite equals to (testSuiteId + 1)
        defaultTestCaseShouldNotBeFound("testSuiteId.equals=" + (testSuiteId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesBySectionIsEqualToSomething() throws Exception {
        Section section;
        if (TestUtil.findAll(em, Section.class).isEmpty()) {
            testCaseRepository.saveAndFlush(testCase);
            section = SectionResourceIT.createEntity(em);
        } else {
            section = TestUtil.findAll(em, Section.class).get(0);
        }
        em.persist(section);
        em.flush();
        testCase.setSection(section);
        testCaseRepository.saveAndFlush(testCase);
        Long sectionId = section.getId();

        // Get all the testCaseList where section equals to sectionId
        defaultTestCaseShouldBeFound("sectionId.equals=" + sectionId);

        // Get all the testCaseList where section equals to (sectionId + 1)
        defaultTestCaseShouldNotBeFound("sectionId.equals=" + (sectionId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByPriorityIsEqualToSomething() throws Exception {
        TestCasePriority priority;
        if (TestUtil.findAll(em, TestCasePriority.class).isEmpty()) {
            testCaseRepository.saveAndFlush(testCase);
            priority = TestCasePriorityResourceIT.createEntity(em);
        } else {
            priority = TestUtil.findAll(em, TestCasePriority.class).get(0);
        }
        em.persist(priority);
        em.flush();
        testCase.setPriority(priority);
        testCaseRepository.saveAndFlush(testCase);
        Long priorityId = priority.getId();

        // Get all the testCaseList where priority equals to priorityId
        defaultTestCaseShouldBeFound("priorityId.equals=" + priorityId);

        // Get all the testCaseList where priority equals to (priorityId + 1)
        defaultTestCaseShouldNotBeFound("priorityId.equals=" + (priorityId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTemplateIsEqualToSomething() throws Exception {
        Template template;
        if (TestUtil.findAll(em, Template.class).isEmpty()) {
            testCaseRepository.saveAndFlush(testCase);
            template = TemplateResourceIT.createEntity(em);
        } else {
            template = TestUtil.findAll(em, Template.class).get(0);
        }
        em.persist(template);
        em.flush();
        testCase.setTemplate(template);
        testCaseRepository.saveAndFlush(testCase);
        Long templateId = template.getId();

        // Get all the testCaseList where template equals to templateId
        defaultTestCaseShouldBeFound("templateId.equals=" + templateId);

        // Get all the testCaseList where template equals to (templateId + 1)
        defaultTestCaseShouldNotBeFound("templateId.equals=" + (templateId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByMilestoneIsEqualToSomething() throws Exception {
        Milestone milestone;
        if (TestUtil.findAll(em, Milestone.class).isEmpty()) {
            testCaseRepository.saveAndFlush(testCase);
            milestone = MilestoneResourceIT.createEntity(em);
        } else {
            milestone = TestUtil.findAll(em, Milestone.class).get(0);
        }
        em.persist(milestone);
        em.flush();
        testCase.setMilestone(milestone);
        testCaseRepository.saveAndFlush(testCase);
        Long milestoneId = milestone.getId();

        // Get all the testCaseList where milestone equals to milestoneId
        defaultTestCaseShouldBeFound("milestoneId.equals=" + milestoneId);

        // Get all the testCaseList where milestone equals to (milestoneId + 1)
        defaultTestCaseShouldNotBeFound("milestoneId.equals=" + (milestoneId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTestLevelIsEqualToSomething() throws Exception {
        TestLevel testLevel;
        if (TestUtil.findAll(em, TestLevel.class).isEmpty()) {
            testCaseRepository.saveAndFlush(testCase);
            testLevel = TestLevelResourceIT.createEntity(em);
        } else {
            testLevel = TestUtil.findAll(em, TestLevel.class).get(0);
        }
        em.persist(testLevel);
        em.flush();
        testCase.addTestLevel(testLevel);
        testCaseRepository.saveAndFlush(testCase);
        Long testLevelId = testLevel.getId();

        // Get all the testCaseList where testLevel equals to testLevelId
        defaultTestCaseShouldBeFound("testLevelId.equals=" + testLevelId);

        // Get all the testCaseList where testLevel equals to (testLevelId + 1)
        defaultTestCaseShouldNotBeFound("testLevelId.equals=" + (testLevelId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTestcaseattachmentTestcaseIsEqualToSomething() throws Exception {
        TestCaseAttachment testcaseattachmentTestcase;
        if (TestUtil.findAll(em, TestCaseAttachment.class).isEmpty()) {
            testCaseRepository.saveAndFlush(testCase);
            testcaseattachmentTestcase = TestCaseAttachmentResourceIT.createEntity(em);
        } else {
            testcaseattachmentTestcase = TestUtil.findAll(em, TestCaseAttachment.class).get(0);
        }
        em.persist(testcaseattachmentTestcase);
        em.flush();
        testCase.addTestcaseattachmentTestcase(testcaseattachmentTestcase);
        testCaseRepository.saveAndFlush(testCase);
        Long testcaseattachmentTestcaseId = testcaseattachmentTestcase.getId();

        // Get all the testCaseList where testcaseattachmentTestcase equals to testcaseattachmentTestcaseId
        defaultTestCaseShouldBeFound("testcaseattachmentTestcaseId.equals=" + testcaseattachmentTestcaseId);

        // Get all the testCaseList where testcaseattachmentTestcase equals to (testcaseattachmentTestcaseId + 1)
        defaultTestCaseShouldNotBeFound("testcaseattachmentTestcaseId.equals=" + (testcaseattachmentTestcaseId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTestcasefieldTestcaseIsEqualToSomething() throws Exception {
        TestCaseField testcasefieldTestcase;
        if (TestUtil.findAll(em, TestCaseField.class).isEmpty()) {
            testCaseRepository.saveAndFlush(testCase);
            testcasefieldTestcase = TestCaseFieldResourceIT.createEntity(em);
        } else {
            testcasefieldTestcase = TestUtil.findAll(em, TestCaseField.class).get(0);
        }
        em.persist(testcasefieldTestcase);
        em.flush();
        testCase.addTestcasefieldTestcase(testcasefieldTestcase);
        testCaseRepository.saveAndFlush(testCase);
        Long testcasefieldTestcaseId = testcasefieldTestcase.getId();

        // Get all the testCaseList where testcasefieldTestcase equals to testcasefieldTestcaseId
        defaultTestCaseShouldBeFound("testcasefieldTestcaseId.equals=" + testcasefieldTestcaseId);

        // Get all the testCaseList where testcasefieldTestcase equals to (testcasefieldTestcaseId + 1)
        defaultTestCaseShouldNotBeFound("testcasefieldTestcaseId.equals=" + (testcasefieldTestcaseId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTestrundetailsTestcaseIsEqualToSomething() throws Exception {
        TestRunDetails testrundetailsTestcase;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testCaseRepository.saveAndFlush(testCase);
            testrundetailsTestcase = TestRunDetailsResourceIT.createEntity(em);
        } else {
            testrundetailsTestcase = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        em.persist(testrundetailsTestcase);
        em.flush();
        testCase.addTestrundetailsTestcase(testrundetailsTestcase);
        testCaseRepository.saveAndFlush(testCase);
        Long testrundetailsTestcaseId = testrundetailsTestcase.getId();

        // Get all the testCaseList where testrundetailsTestcase equals to testrundetailsTestcaseId
        defaultTestCaseShouldBeFound("testrundetailsTestcaseId.equals=" + testrundetailsTestcaseId);

        // Get all the testCaseList where testrundetailsTestcase equals to (testrundetailsTestcaseId + 1)
        defaultTestCaseShouldNotBeFound("testrundetailsTestcaseId.equals=" + (testrundetailsTestcaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCaseShouldBeFound(String filter) throws Exception {
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].estimate").value(hasItem(DEFAULT_ESTIMATE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].precondition").value(hasItem(DEFAULT_PRECONDITION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isAutomated").value(hasItem(DEFAULT_IS_AUTOMATED.booleanValue())));

        // Check, that the count call also returns 1
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCaseShouldNotBeFound(String filter) throws Exception {
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCase() throws Exception {
        // Get the testCase
        restTestCaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Update the testCase
        TestCase updatedTestCase = testCaseRepository.findById(testCase.getId()).get();
        // Disconnect from session so that the updates on updatedTestCase are not directly saved in db
        em.detach(updatedTestCase);
        updatedTestCase
            .title(UPDATED_TITLE)
            .estimate(UPDATED_ESTIMATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .precondition(UPDATED_PRECONDITION)
            .description(UPDATED_DESCRIPTION)
            .isAutomated(UPDATED_IS_AUTOMATED);

        restTestCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCase.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCase))
            )
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestCase.getEstimate()).isEqualTo(UPDATED_ESTIMATE);
        assertThat(testTestCase.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestCase.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestCase.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestCase.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTestCase.getPrecondition()).isEqualTo(UPDATED_PRECONDITION);
        assertThat(testTestCase.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestCase.getIsAutomated()).isEqualTo(UPDATED_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void putNonExistingTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCase.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCase))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCase))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCase)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseWithPatch() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Update the testCase using partial update
        TestCase partialUpdatedTestCase = new TestCase();
        partialUpdatedTestCase.setId(testCase.getId());

        partialUpdatedTestCase.title(UPDATED_TITLE).estimate(UPDATED_ESTIMATE).updatedBy(UPDATED_UPDATED_BY);

        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCase))
            )
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestCase.getEstimate()).isEqualTo(UPDATED_ESTIMATE);
        assertThat(testTestCase.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestCase.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestCase.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestCase.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testTestCase.getPrecondition()).isEqualTo(DEFAULT_PRECONDITION);
        assertThat(testTestCase.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestCase.getIsAutomated()).isEqualTo(DEFAULT_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void fullUpdateTestCaseWithPatch() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Update the testCase using partial update
        TestCase partialUpdatedTestCase = new TestCase();
        partialUpdatedTestCase.setId(testCase.getId());

        partialUpdatedTestCase
            .title(UPDATED_TITLE)
            .estimate(UPDATED_ESTIMATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .precondition(UPDATED_PRECONDITION)
            .description(UPDATED_DESCRIPTION)
            .isAutomated(UPDATED_IS_AUTOMATED);

        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCase))
            )
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestCase.getEstimate()).isEqualTo(UPDATED_ESTIMATE);
        assertThat(testTestCase.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestCase.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestCase.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestCase.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTestCase.getPrecondition()).isEqualTo(UPDATED_PRECONDITION);
        assertThat(testTestCase.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestCase.getIsAutomated()).isEqualTo(UPDATED_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void patchNonExistingTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCase))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCase))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testCase)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        int databaseSizeBeforeDelete = testCaseRepository.findAll().size();

        // Delete the testCase
        restTestCaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
