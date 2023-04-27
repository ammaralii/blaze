package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.Project;
import com.venturedive.blaze.domain.TestPlan;
import com.venturedive.blaze.repository.TestPlanRepository;
import com.venturedive.blaze.service.criteria.TestPlanCriteria;
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
 * Integration tests for the {@link TestPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestPlanResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/test-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestPlanRepository testPlanRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestPlanMockMvc;

    private TestPlan testPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestPlan createEntity(EntityManager em) {
        TestPlan testPlan = new TestPlan()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT);
        return testPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestPlan createUpdatedEntity(EntityManager em) {
        TestPlan testPlan = new TestPlan()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT);
        return testPlan;
    }

    @BeforeEach
    public void initTest() {
        testPlan = createEntity(em);
    }

    @Test
    @Transactional
    void createTestPlan() throws Exception {
        int databaseSizeBeforeCreate = testPlanRepository.findAll().size();
        // Create the TestPlan
        restTestPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testPlan)))
            .andExpect(status().isCreated());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeCreate + 1);
        TestPlan testTestPlan = testPlanList.get(testPlanList.size() - 1);
        assertThat(testTestPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestPlan.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestPlan.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestPlan.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createTestPlanWithExistingId() throws Exception {
        // Create the TestPlan with an existing ID
        testPlan.setId(1L);

        int databaseSizeBeforeCreate = testPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testPlan)))
            .andExpect(status().isBadRequest());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestPlans() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList
        restTestPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTestPlan() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get the testPlan
        restTestPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, testPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testPlan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getTestPlansByIdFiltering() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        Long id = testPlan.getId();

        defaultTestPlanShouldBeFound("id.equals=" + id);
        defaultTestPlanShouldNotBeFound("id.notEquals=" + id);

        defaultTestPlanShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestPlanShouldNotBeFound("id.greaterThan=" + id);

        defaultTestPlanShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestPlanShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestPlansByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where name equals to DEFAULT_NAME
        defaultTestPlanShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the testPlanList where name equals to UPDATED_NAME
        defaultTestPlanShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestPlansByNameIsInShouldWork() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTestPlanShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the testPlanList where name equals to UPDATED_NAME
        defaultTestPlanShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestPlansByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where name is not null
        defaultTestPlanShouldBeFound("name.specified=true");

        // Get all the testPlanList where name is null
        defaultTestPlanShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTestPlansByNameContainsSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where name contains DEFAULT_NAME
        defaultTestPlanShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the testPlanList where name contains UPDATED_NAME
        defaultTestPlanShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestPlansByNameNotContainsSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where name does not contain DEFAULT_NAME
        defaultTestPlanShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the testPlanList where name does not contain UPDATED_NAME
        defaultTestPlanShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestPlansByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where description equals to DEFAULT_DESCRIPTION
        defaultTestPlanShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the testPlanList where description equals to UPDATED_DESCRIPTION
        defaultTestPlanShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestPlansByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTestPlanShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the testPlanList where description equals to UPDATED_DESCRIPTION
        defaultTestPlanShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestPlansByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where description is not null
        defaultTestPlanShouldBeFound("description.specified=true");

        // Get all the testPlanList where description is null
        defaultTestPlanShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTestPlansByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where description contains DEFAULT_DESCRIPTION
        defaultTestPlanShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the testPlanList where description contains UPDATED_DESCRIPTION
        defaultTestPlanShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestPlansByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where description does not contain DEFAULT_DESCRIPTION
        defaultTestPlanShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the testPlanList where description does not contain UPDATED_DESCRIPTION
        defaultTestPlanShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdBy equals to DEFAULT_CREATED_BY
        defaultTestPlanShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the testPlanList where createdBy equals to UPDATED_CREATED_BY
        defaultTestPlanShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTestPlanShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the testPlanList where createdBy equals to UPDATED_CREATED_BY
        defaultTestPlanShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdBy is not null
        defaultTestPlanShouldBeFound("createdBy.specified=true");

        // Get all the testPlanList where createdBy is null
        defaultTestPlanShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTestPlanShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testPlanList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTestPlanShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTestPlanShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testPlanList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTestPlanShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdBy is less than DEFAULT_CREATED_BY
        defaultTestPlanShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the testPlanList where createdBy is less than UPDATED_CREATED_BY
        defaultTestPlanShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTestPlanShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the testPlanList where createdBy is greater than SMALLER_CREATED_BY
        defaultTestPlanShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdAt equals to DEFAULT_CREATED_AT
        defaultTestPlanShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the testPlanList where createdAt equals to UPDATED_CREATED_AT
        defaultTestPlanShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTestPlanShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the testPlanList where createdAt equals to UPDATED_CREATED_AT
        defaultTestPlanShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlanList where createdAt is not null
        defaultTestPlanShouldBeFound("createdAt.specified=true");

        // Get all the testPlanList where createdAt is null
        defaultTestPlanShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestPlansByProjectIsEqualToSomething() throws Exception {
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            testPlanRepository.saveAndFlush(testPlan);
            project = ProjectResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        testPlan.setProject(project);
        testPlanRepository.saveAndFlush(testPlan);
        Long projectId = project.getId();

        // Get all the testPlanList where project equals to projectId
        defaultTestPlanShouldBeFound("projectId.equals=" + projectId);

        // Get all the testPlanList where project equals to (projectId + 1)
        defaultTestPlanShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestPlanShouldBeFound(String filter) throws Exception {
        restTestPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restTestPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestPlanShouldNotBeFound(String filter) throws Exception {
        restTestPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestPlan() throws Exception {
        // Get the testPlan
        restTestPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestPlan() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();

        // Update the testPlan
        TestPlan updatedTestPlan = testPlanRepository.findById(testPlan.getId()).get();
        // Disconnect from session so that the updates on updatedTestPlan are not directly saved in db
        em.detach(updatedTestPlan);
        updatedTestPlan.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdBy(UPDATED_CREATED_BY).createdAt(UPDATED_CREATED_AT);

        restTestPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestPlan))
            )
            .andExpect(status().isOk());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeUpdate);
        TestPlan testTestPlan = testPlanList.get(testPlanList.size() - 1);
        assertThat(testTestPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestPlan.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestPlan.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTestPlan() throws Exception {
        int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();
        testPlan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestPlan() throws Exception {
        int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();
        testPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestPlan() throws Exception {
        int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();
        testPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testPlan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestPlanWithPatch() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();

        // Update the testPlan using partial update
        TestPlan partialUpdatedTestPlan = new TestPlan();
        partialUpdatedTestPlan.setId(testPlan.getId());

        partialUpdatedTestPlan.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTestPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestPlan))
            )
            .andExpect(status().isOk());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeUpdate);
        TestPlan testTestPlan = testPlanList.get(testPlanList.size() - 1);
        assertThat(testTestPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestPlan.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestPlan.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTestPlanWithPatch() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();

        // Update the testPlan using partial update
        TestPlan partialUpdatedTestPlan = new TestPlan();
        partialUpdatedTestPlan.setId(testPlan.getId());

        partialUpdatedTestPlan
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restTestPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestPlan))
            )
            .andExpect(status().isOk());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeUpdate);
        TestPlan testTestPlan = testPlanList.get(testPlanList.size() - 1);
        assertThat(testTestPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestPlan.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestPlan.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTestPlan() throws Exception {
        int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();
        testPlan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestPlan() throws Exception {
        int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();
        testPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestPlan() throws Exception {
        int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();
        testPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testPlan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestPlan in the database
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestPlan() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        int databaseSizeBeforeDelete = testPlanRepository.findAll().size();

        // Delete the testPlan
        restTestPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, testPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestPlan> testPlanList = testPlanRepository.findAll();
        assertThat(testPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
