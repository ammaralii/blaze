package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.Milestone;
import com.venturedive.blaze.domain.TestLevel;
import com.venturedive.blaze.domain.TestRun;
import com.venturedive.blaze.domain.TestRunDetails;
import com.venturedive.blaze.repository.TestRunRepository;
import com.venturedive.blaze.service.criteria.TestRunCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TestRunResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestRunResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/test-runs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestRunRepository testRunRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestRunMockMvc;

    private TestRun testRun;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRun createEntity(EntityManager em) {
        TestRun testRun = new TestRun()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY);
        return testRun;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRun createUpdatedEntity(EntityManager em) {
        TestRun testRun = new TestRun()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);
        return testRun;
    }

    @BeforeEach
    public void initTest() {
        testRun = createEntity(em);
    }

    @Test
    @Transactional
    void createTestRun() throws Exception {
        int databaseSizeBeforeCreate = testRunRepository.findAll().size();
        // Create the TestRun
        restTestRunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRun)))
            .andExpect(status().isCreated());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeCreate + 1);
        TestRun testTestRun = testRunList.get(testRunList.size() - 1);
        assertThat(testTestRun.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestRun.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestRun.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestRun.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createTestRunWithExistingId() throws Exception {
        // Create the TestRun with an existing ID
        testRun.setId(1L);

        int databaseSizeBeforeCreate = testRunRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestRunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRun)))
            .andExpect(status().isBadRequest());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestRuns() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList
        restTestRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getTestRun() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get the testRun
        restTestRunMockMvc
            .perform(get(ENTITY_API_URL_ID, testRun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testRun.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getTestRunsByIdFiltering() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        Long id = testRun.getId();

        defaultTestRunShouldBeFound("id.equals=" + id);
        defaultTestRunShouldNotBeFound("id.notEquals=" + id);

        defaultTestRunShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestRunShouldNotBeFound("id.greaterThan=" + id);

        defaultTestRunShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestRunShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestRunsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where name equals to DEFAULT_NAME
        defaultTestRunShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the testRunList where name equals to UPDATED_NAME
        defaultTestRunShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestRunsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTestRunShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the testRunList where name equals to UPDATED_NAME
        defaultTestRunShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestRunsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where name is not null
        defaultTestRunShouldBeFound("name.specified=true");

        // Get all the testRunList where name is null
        defaultTestRunShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunsByNameContainsSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where name contains DEFAULT_NAME
        defaultTestRunShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the testRunList where name contains UPDATED_NAME
        defaultTestRunShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestRunsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where name does not contain DEFAULT_NAME
        defaultTestRunShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the testRunList where name does not contain UPDATED_NAME
        defaultTestRunShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where description equals to DEFAULT_DESCRIPTION
        defaultTestRunShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the testRunList where description equals to UPDATED_DESCRIPTION
        defaultTestRunShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTestRunShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the testRunList where description equals to UPDATED_DESCRIPTION
        defaultTestRunShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where description is not null
        defaultTestRunShouldBeFound("description.specified=true");

        // Get all the testRunList where description is null
        defaultTestRunShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where description contains DEFAULT_DESCRIPTION
        defaultTestRunShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the testRunList where description contains UPDATED_DESCRIPTION
        defaultTestRunShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where description does not contain DEFAULT_DESCRIPTION
        defaultTestRunShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the testRunList where description does not contain UPDATED_DESCRIPTION
        defaultTestRunShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdAt equals to DEFAULT_CREATED_AT
        defaultTestRunShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the testRunList where createdAt equals to UPDATED_CREATED_AT
        defaultTestRunShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTestRunShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the testRunList where createdAt equals to UPDATED_CREATED_AT
        defaultTestRunShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdAt is not null
        defaultTestRunShouldBeFound("createdAt.specified=true");

        // Get all the testRunList where createdAt is null
        defaultTestRunShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdBy equals to DEFAULT_CREATED_BY
        defaultTestRunShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the testRunList where createdBy equals to UPDATED_CREATED_BY
        defaultTestRunShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTestRunShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the testRunList where createdBy equals to UPDATED_CREATED_BY
        defaultTestRunShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdBy is not null
        defaultTestRunShouldBeFound("createdBy.specified=true");

        // Get all the testRunList where createdBy is null
        defaultTestRunShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTestRunShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testRunList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTestRunShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTestRunShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testRunList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTestRunShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdBy is less than DEFAULT_CREATED_BY
        defaultTestRunShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the testRunList where createdBy is less than UPDATED_CREATED_BY
        defaultTestRunShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        // Get all the testRunList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTestRunShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the testRunList where createdBy is greater than SMALLER_CREATED_BY
        defaultTestRunShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByTestLevelIsEqualToSomething() throws Exception {
        TestLevel testLevel;
        if (TestUtil.findAll(em, TestLevel.class).isEmpty()) {
            testRunRepository.saveAndFlush(testRun);
            testLevel = TestLevelResourceIT.createEntity(em);
        } else {
            testLevel = TestUtil.findAll(em, TestLevel.class).get(0);
        }
        em.persist(testLevel);
        em.flush();
        testRun.setTestLevel(testLevel);
        testRunRepository.saveAndFlush(testRun);
        Long testLevelId = testLevel.getId();

        // Get all the testRunList where testLevel equals to testLevelId
        defaultTestRunShouldBeFound("testLevelId.equals=" + testLevelId);

        // Get all the testRunList where testLevel equals to (testLevelId + 1)
        defaultTestRunShouldNotBeFound("testLevelId.equals=" + (testLevelId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunsByMileStoneIsEqualToSomething() throws Exception {
        Milestone mileStone;
        if (TestUtil.findAll(em, Milestone.class).isEmpty()) {
            testRunRepository.saveAndFlush(testRun);
            mileStone = MilestoneResourceIT.createEntity(em);
        } else {
            mileStone = TestUtil.findAll(em, Milestone.class).get(0);
        }
        em.persist(mileStone);
        em.flush();
        testRun.setMileStone(mileStone);
        testRunRepository.saveAndFlush(testRun);
        Long mileStoneId = mileStone.getId();

        // Get all the testRunList where mileStone equals to mileStoneId
        defaultTestRunShouldBeFound("mileStoneId.equals=" + mileStoneId);

        // Get all the testRunList where mileStone equals to (mileStoneId + 1)
        defaultTestRunShouldNotBeFound("mileStoneId.equals=" + (mileStoneId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunsByTestrundetailsTestrunIsEqualToSomething() throws Exception {
        TestRunDetails testrundetailsTestrun;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testRunRepository.saveAndFlush(testRun);
            testrundetailsTestrun = TestRunDetailsResourceIT.createEntity(em);
        } else {
            testrundetailsTestrun = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        em.persist(testrundetailsTestrun);
        em.flush();
        testRun.addTestrundetailsTestrun(testrundetailsTestrun);
        testRunRepository.saveAndFlush(testRun);
        Long testrundetailsTestrunId = testrundetailsTestrun.getId();

        // Get all the testRunList where testrundetailsTestrun equals to testrundetailsTestrunId
        defaultTestRunShouldBeFound("testrundetailsTestrunId.equals=" + testrundetailsTestrunId);

        // Get all the testRunList where testrundetailsTestrun equals to (testrundetailsTestrunId + 1)
        defaultTestRunShouldNotBeFound("testrundetailsTestrunId.equals=" + (testrundetailsTestrunId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestRunShouldBeFound(String filter) throws Exception {
        restTestRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restTestRunMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestRunShouldNotBeFound(String filter) throws Exception {
        restTestRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestRunMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestRun() throws Exception {
        // Get the testRun
        restTestRunMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestRun() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        int databaseSizeBeforeUpdate = testRunRepository.findAll().size();

        // Update the testRun
        TestRun updatedTestRun = testRunRepository.findById(testRun.getId()).get();
        // Disconnect from session so that the updates on updatedTestRun are not directly saved in db
        em.detach(updatedTestRun);
        updatedTestRun.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);

        restTestRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestRun.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestRun))
            )
            .andExpect(status().isOk());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeUpdate);
        TestRun testTestRun = testRunList.get(testRunList.size() - 1);
        assertThat(testTestRun.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestRun.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestRun.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestRun.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingTestRun() throws Exception {
        int databaseSizeBeforeUpdate = testRunRepository.findAll().size();
        testRun.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testRun.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRun))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestRun() throws Exception {
        int databaseSizeBeforeUpdate = testRunRepository.findAll().size();
        testRun.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRun))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestRun() throws Exception {
        int databaseSizeBeforeUpdate = testRunRepository.findAll().size();
        testRun.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRun)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestRunWithPatch() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        int databaseSizeBeforeUpdate = testRunRepository.findAll().size();

        // Update the testRun using partial update
        TestRun partialUpdatedTestRun = new TestRun();
        partialUpdatedTestRun.setId(testRun.getId());

        partialUpdatedTestRun.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);

        restTestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRun))
            )
            .andExpect(status().isOk());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeUpdate);
        TestRun testTestRun = testRunList.get(testRunList.size() - 1);
        assertThat(testTestRun.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestRun.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestRun.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestRun.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateTestRunWithPatch() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        int databaseSizeBeforeUpdate = testRunRepository.findAll().size();

        // Update the testRun using partial update
        TestRun partialUpdatedTestRun = new TestRun();
        partialUpdatedTestRun.setId(testRun.getId());

        partialUpdatedTestRun
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);

        restTestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRun))
            )
            .andExpect(status().isOk());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeUpdate);
        TestRun testTestRun = testRunList.get(testRunList.size() - 1);
        assertThat(testTestRun.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestRun.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestRun.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestRun.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingTestRun() throws Exception {
        int databaseSizeBeforeUpdate = testRunRepository.findAll().size();
        testRun.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRun))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestRun() throws Exception {
        int databaseSizeBeforeUpdate = testRunRepository.findAll().size();
        testRun.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRun))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestRun() throws Exception {
        int databaseSizeBeforeUpdate = testRunRepository.findAll().size();
        testRun.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testRun)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRun in the database
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestRun() throws Exception {
        // Initialize the database
        testRunRepository.saveAndFlush(testRun);

        int databaseSizeBeforeDelete = testRunRepository.findAll().size();

        // Delete the testRun
        restTestRunMockMvc
            .perform(delete(ENTITY_API_URL_ID, testRun.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestRun> testRunList = testRunRepository.findAll();
        assertThat(testRunList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
