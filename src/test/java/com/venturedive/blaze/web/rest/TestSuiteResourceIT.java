package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.Project;
import com.venturedive.blaze.domain.Section;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.domain.TestSuite;
import com.venturedive.blaze.repository.TestSuiteRepository;
import com.venturedive.blaze.service.criteria.TestSuiteCriteria;
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
 * Integration tests for the {@link TestSuiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestSuiteResourceIT {

    private static final String DEFAULT_TEST_SUITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEST_SUITE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/test-suites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestSuiteRepository testSuiteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestSuiteMockMvc;

    private TestSuite testSuite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestSuite createEntity(EntityManager em) {
        TestSuite testSuite = new TestSuite()
            .testSuiteName(DEFAULT_TEST_SUITE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return testSuite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestSuite createUpdatedEntity(EntityManager em) {
        TestSuite testSuite = new TestSuite()
            .testSuiteName(UPDATED_TEST_SUITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return testSuite;
    }

    @BeforeEach
    public void initTest() {
        testSuite = createEntity(em);
    }

    @Test
    @Transactional
    void createTestSuite() throws Exception {
        int databaseSizeBeforeCreate = testSuiteRepository.findAll().size();
        // Create the TestSuite
        restTestSuiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testSuite)))
            .andExpect(status().isCreated());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeCreate + 1);
        TestSuite testTestSuite = testSuiteList.get(testSuiteList.size() - 1);
        assertThat(testTestSuite.getTestSuiteName()).isEqualTo(DEFAULT_TEST_SUITE_NAME);
        assertThat(testTestSuite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestSuite.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestSuite.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestSuite.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTestSuite.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTestSuiteWithExistingId() throws Exception {
        // Create the TestSuite with an existing ID
        testSuite.setId(1L);

        int databaseSizeBeforeCreate = testSuiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestSuiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testSuite)))
            .andExpect(status().isBadRequest());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestSuites() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList
        restTestSuiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testSuite.getId().intValue())))
            .andExpect(jsonPath("$.[*].testSuiteName").value(hasItem(DEFAULT_TEST_SUITE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTestSuite() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get the testSuite
        restTestSuiteMockMvc
            .perform(get(ENTITY_API_URL_ID, testSuite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testSuite.getId().intValue()))
            .andExpect(jsonPath("$.testSuiteName").value(DEFAULT_TEST_SUITE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getTestSuitesByIdFiltering() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        Long id = testSuite.getId();

        defaultTestSuiteShouldBeFound("id.equals=" + id);
        defaultTestSuiteShouldNotBeFound("id.notEquals=" + id);

        defaultTestSuiteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestSuiteShouldNotBeFound("id.greaterThan=" + id);

        defaultTestSuiteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestSuiteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where testSuiteName equals to DEFAULT_TEST_SUITE_NAME
        defaultTestSuiteShouldBeFound("testSuiteName.equals=" + DEFAULT_TEST_SUITE_NAME);

        // Get all the testSuiteList where testSuiteName equals to UPDATED_TEST_SUITE_NAME
        defaultTestSuiteShouldNotBeFound("testSuiteName.equals=" + UPDATED_TEST_SUITE_NAME);
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameIsInShouldWork() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where testSuiteName in DEFAULT_TEST_SUITE_NAME or UPDATED_TEST_SUITE_NAME
        defaultTestSuiteShouldBeFound("testSuiteName.in=" + DEFAULT_TEST_SUITE_NAME + "," + UPDATED_TEST_SUITE_NAME);

        // Get all the testSuiteList where testSuiteName equals to UPDATED_TEST_SUITE_NAME
        defaultTestSuiteShouldNotBeFound("testSuiteName.in=" + UPDATED_TEST_SUITE_NAME);
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where testSuiteName is not null
        defaultTestSuiteShouldBeFound("testSuiteName.specified=true");

        // Get all the testSuiteList where testSuiteName is null
        defaultTestSuiteShouldNotBeFound("testSuiteName.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameContainsSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where testSuiteName contains DEFAULT_TEST_SUITE_NAME
        defaultTestSuiteShouldBeFound("testSuiteName.contains=" + DEFAULT_TEST_SUITE_NAME);

        // Get all the testSuiteList where testSuiteName contains UPDATED_TEST_SUITE_NAME
        defaultTestSuiteShouldNotBeFound("testSuiteName.contains=" + UPDATED_TEST_SUITE_NAME);
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameNotContainsSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where testSuiteName does not contain DEFAULT_TEST_SUITE_NAME
        defaultTestSuiteShouldNotBeFound("testSuiteName.doesNotContain=" + DEFAULT_TEST_SUITE_NAME);

        // Get all the testSuiteList where testSuiteName does not contain UPDATED_TEST_SUITE_NAME
        defaultTestSuiteShouldBeFound("testSuiteName.doesNotContain=" + UPDATED_TEST_SUITE_NAME);
    }

    @Test
    @Transactional
    void getAllTestSuitesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where description equals to DEFAULT_DESCRIPTION
        defaultTestSuiteShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the testSuiteList where description equals to UPDATED_DESCRIPTION
        defaultTestSuiteShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestSuitesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTestSuiteShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the testSuiteList where description equals to UPDATED_DESCRIPTION
        defaultTestSuiteShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestSuitesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where description is not null
        defaultTestSuiteShouldBeFound("description.specified=true");

        // Get all the testSuiteList where description is null
        defaultTestSuiteShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where description contains DEFAULT_DESCRIPTION
        defaultTestSuiteShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the testSuiteList where description contains UPDATED_DESCRIPTION
        defaultTestSuiteShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestSuitesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where description does not contain DEFAULT_DESCRIPTION
        defaultTestSuiteShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the testSuiteList where description does not contain UPDATED_DESCRIPTION
        defaultTestSuiteShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdBy equals to DEFAULT_CREATED_BY
        defaultTestSuiteShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the testSuiteList where createdBy equals to UPDATED_CREATED_BY
        defaultTestSuiteShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTestSuiteShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the testSuiteList where createdBy equals to UPDATED_CREATED_BY
        defaultTestSuiteShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdBy is not null
        defaultTestSuiteShouldBeFound("createdBy.specified=true");

        // Get all the testSuiteList where createdBy is null
        defaultTestSuiteShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTestSuiteShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testSuiteList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTestSuiteShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTestSuiteShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testSuiteList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTestSuiteShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdBy is less than DEFAULT_CREATED_BY
        defaultTestSuiteShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the testSuiteList where createdBy is less than UPDATED_CREATED_BY
        defaultTestSuiteShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTestSuiteShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the testSuiteList where createdBy is greater than SMALLER_CREATED_BY
        defaultTestSuiteShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdAt equals to DEFAULT_CREATED_AT
        defaultTestSuiteShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the testSuiteList where createdAt equals to UPDATED_CREATED_AT
        defaultTestSuiteShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTestSuiteShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the testSuiteList where createdAt equals to UPDATED_CREATED_AT
        defaultTestSuiteShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where createdAt is not null
        defaultTestSuiteShouldBeFound("createdAt.specified=true");

        // Get all the testSuiteList where createdAt is null
        defaultTestSuiteShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultTestSuiteShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the testSuiteList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTestSuiteShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultTestSuiteShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the testSuiteList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTestSuiteShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedBy is not null
        defaultTestSuiteShouldBeFound("updatedBy.specified=true");

        // Get all the testSuiteList where updatedBy is null
        defaultTestSuiteShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultTestSuiteShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the testSuiteList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultTestSuiteShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultTestSuiteShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the testSuiteList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultTestSuiteShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultTestSuiteShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the testSuiteList where updatedBy is less than UPDATED_UPDATED_BY
        defaultTestSuiteShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultTestSuiteShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the testSuiteList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultTestSuiteShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTestSuiteShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the testSuiteList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestSuiteShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTestSuiteShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the testSuiteList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestSuiteShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList where updatedAt is not null
        defaultTestSuiteShouldBeFound("updatedAt.specified=true");

        // Get all the testSuiteList where updatedAt is null
        defaultTestSuiteShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByProjectIsEqualToSomething() throws Exception {
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            testSuiteRepository.saveAndFlush(testSuite);
            project = ProjectResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        testSuite.setProject(project);
        testSuiteRepository.saveAndFlush(testSuite);
        Long projectId = project.getId();

        // Get all the testSuiteList where project equals to projectId
        defaultTestSuiteShouldBeFound("projectId.equals=" + projectId);

        // Get all the testSuiteList where project equals to (projectId + 1)
        defaultTestSuiteShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllTestSuitesBySectionTestsuiteIsEqualToSomething() throws Exception {
        Section sectionTestsuite;
        if (TestUtil.findAll(em, Section.class).isEmpty()) {
            testSuiteRepository.saveAndFlush(testSuite);
            sectionTestsuite = SectionResourceIT.createEntity(em);
        } else {
            sectionTestsuite = TestUtil.findAll(em, Section.class).get(0);
        }
        em.persist(sectionTestsuite);
        em.flush();
        testSuite.addSectionTestsuite(sectionTestsuite);
        testSuiteRepository.saveAndFlush(testSuite);
        Long sectionTestsuiteId = sectionTestsuite.getId();

        // Get all the testSuiteList where sectionTestsuite equals to sectionTestsuiteId
        defaultTestSuiteShouldBeFound("sectionTestsuiteId.equals=" + sectionTestsuiteId);

        // Get all the testSuiteList where sectionTestsuite equals to (sectionTestsuiteId + 1)
        defaultTestSuiteShouldNotBeFound("sectionTestsuiteId.equals=" + (sectionTestsuiteId + 1));
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestcaseTestsuiteIsEqualToSomething() throws Exception {
        TestCase testcaseTestsuite;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testSuiteRepository.saveAndFlush(testSuite);
            testcaseTestsuite = TestCaseResourceIT.createEntity(em);
        } else {
            testcaseTestsuite = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testcaseTestsuite);
        em.flush();
        testSuite.addTestcaseTestsuite(testcaseTestsuite);
        testSuiteRepository.saveAndFlush(testSuite);
        Long testcaseTestsuiteId = testcaseTestsuite.getId();

        // Get all the testSuiteList where testcaseTestsuite equals to testcaseTestsuiteId
        defaultTestSuiteShouldBeFound("testcaseTestsuiteId.equals=" + testcaseTestsuiteId);

        // Get all the testSuiteList where testcaseTestsuite equals to (testcaseTestsuiteId + 1)
        defaultTestSuiteShouldNotBeFound("testcaseTestsuiteId.equals=" + (testcaseTestsuiteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestSuiteShouldBeFound(String filter) throws Exception {
        restTestSuiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testSuite.getId().intValue())))
            .andExpect(jsonPath("$.[*].testSuiteName").value(hasItem(DEFAULT_TEST_SUITE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restTestSuiteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestSuiteShouldNotBeFound(String filter) throws Exception {
        restTestSuiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestSuiteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestSuite() throws Exception {
        // Get the testSuite
        restTestSuiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestSuite() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();

        // Update the testSuite
        TestSuite updatedTestSuite = testSuiteRepository.findById(testSuite.getId()).get();
        // Disconnect from session so that the updates on updatedTestSuite are not directly saved in db
        em.detach(updatedTestSuite);
        updatedTestSuite
            .testSuiteName(UPDATED_TEST_SUITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restTestSuiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestSuite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestSuite))
            )
            .andExpect(status().isOk());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
        TestSuite testTestSuite = testSuiteList.get(testSuiteList.size() - 1);
        assertThat(testTestSuite.getTestSuiteName()).isEqualTo(UPDATED_TEST_SUITE_NAME);
        assertThat(testTestSuite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestSuite.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestSuite.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestSuite.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestSuite.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTestSuite() throws Exception {
        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();
        testSuite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestSuiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testSuite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testSuite))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestSuite() throws Exception {
        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();
        testSuite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestSuiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testSuite))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestSuite() throws Exception {
        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();
        testSuite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestSuiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testSuite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestSuiteWithPatch() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();

        // Update the testSuite using partial update
        TestSuite partialUpdatedTestSuite = new TestSuite();
        partialUpdatedTestSuite.setId(testSuite.getId());

        partialUpdatedTestSuite
            .testSuiteName(UPDATED_TEST_SUITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTestSuiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestSuite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestSuite))
            )
            .andExpect(status().isOk());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
        TestSuite testTestSuite = testSuiteList.get(testSuiteList.size() - 1);
        assertThat(testTestSuite.getTestSuiteName()).isEqualTo(UPDATED_TEST_SUITE_NAME);
        assertThat(testTestSuite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestSuite.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestSuite.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestSuite.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTestSuite.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTestSuiteWithPatch() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();

        // Update the testSuite using partial update
        TestSuite partialUpdatedTestSuite = new TestSuite();
        partialUpdatedTestSuite.setId(testSuite.getId());

        partialUpdatedTestSuite
            .testSuiteName(UPDATED_TEST_SUITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restTestSuiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestSuite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestSuite))
            )
            .andExpect(status().isOk());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
        TestSuite testTestSuite = testSuiteList.get(testSuiteList.size() - 1);
        assertThat(testTestSuite.getTestSuiteName()).isEqualTo(UPDATED_TEST_SUITE_NAME);
        assertThat(testTestSuite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestSuite.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestSuite.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestSuite.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestSuite.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTestSuite() throws Exception {
        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();
        testSuite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestSuiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testSuite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testSuite))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestSuite() throws Exception {
        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();
        testSuite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestSuiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testSuite))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestSuite() throws Exception {
        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();
        testSuite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestSuiteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testSuite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestSuite() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        int databaseSizeBeforeDelete = testSuiteRepository.findAll().size();

        // Delete the testSuite
        restTestSuiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, testSuite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
