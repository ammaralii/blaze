package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.Milestone;
import com.venturedive.blaze.domain.Milestone;
import com.venturedive.blaze.domain.Project;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.domain.TestRun;
import com.venturedive.blaze.repository.MilestoneRepository;
import com.venturedive.blaze.service.criteria.MilestoneCriteria;
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
 * Integration tests for the {@link MilestoneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MilestoneResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_COMPLETED = false;
    private static final Boolean UPDATED_IS_COMPLETED = true;

    private static final String ENTITY_API_URL = "/api/milestones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMilestoneMockMvc;

    private Milestone milestone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Milestone createEntity(EntityManager em) {
        Milestone milestone = new Milestone()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .reference(DEFAULT_REFERENCE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isCompleted(DEFAULT_IS_COMPLETED);
        return milestone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Milestone createUpdatedEntity(EntityManager em) {
        Milestone milestone = new Milestone()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reference(UPDATED_REFERENCE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCompleted(UPDATED_IS_COMPLETED);
        return milestone;
    }

    @BeforeEach
    public void initTest() {
        milestone = createEntity(em);
    }

    @Test
    @Transactional
    void createMilestone() throws Exception {
        int databaseSizeBeforeCreate = milestoneRepository.findAll().size();
        // Create the Milestone
        restMilestoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isCreated());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeCreate + 1);
        Milestone testMilestone = milestoneList.get(milestoneList.size() - 1);
        assertThat(testMilestone.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMilestone.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMilestone.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testMilestone.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMilestone.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMilestone.getIsCompleted()).isEqualTo(DEFAULT_IS_COMPLETED);
    }

    @Test
    @Transactional
    void createMilestoneWithExistingId() throws Exception {
        // Create the Milestone with an existing ID
        milestone.setId(1L);

        int databaseSizeBeforeCreate = milestoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMilestoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMilestones() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList
        restMilestoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(milestone.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    void getMilestone() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get the milestone
        restMilestoneMockMvc
            .perform(get(ENTITY_API_URL_ID, milestone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(milestone.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isCompleted").value(DEFAULT_IS_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    void getMilestonesByIdFiltering() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        Long id = milestone.getId();

        defaultMilestoneShouldBeFound("id.equals=" + id);
        defaultMilestoneShouldNotBeFound("id.notEquals=" + id);

        defaultMilestoneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMilestoneShouldNotBeFound("id.greaterThan=" + id);

        defaultMilestoneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMilestoneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMilestonesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where name equals to DEFAULT_NAME
        defaultMilestoneShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the milestoneList where name equals to UPDATED_NAME
        defaultMilestoneShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMilestonesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMilestoneShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the milestoneList where name equals to UPDATED_NAME
        defaultMilestoneShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMilestonesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where name is not null
        defaultMilestoneShouldBeFound("name.specified=true");

        // Get all the milestoneList where name is null
        defaultMilestoneShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByNameContainsSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where name contains DEFAULT_NAME
        defaultMilestoneShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the milestoneList where name contains UPDATED_NAME
        defaultMilestoneShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMilestonesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where name does not contain DEFAULT_NAME
        defaultMilestoneShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the milestoneList where name does not contain UPDATED_NAME
        defaultMilestoneShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMilestonesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where description equals to DEFAULT_DESCRIPTION
        defaultMilestoneShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the milestoneList where description equals to UPDATED_DESCRIPTION
        defaultMilestoneShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMilestonesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMilestoneShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the milestoneList where description equals to UPDATED_DESCRIPTION
        defaultMilestoneShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMilestonesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where description is not null
        defaultMilestoneShouldBeFound("description.specified=true");

        // Get all the milestoneList where description is null
        defaultMilestoneShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where description contains DEFAULT_DESCRIPTION
        defaultMilestoneShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the milestoneList where description contains UPDATED_DESCRIPTION
        defaultMilestoneShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMilestonesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where description does not contain DEFAULT_DESCRIPTION
        defaultMilestoneShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the milestoneList where description does not contain UPDATED_DESCRIPTION
        defaultMilestoneShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMilestonesByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where reference equals to DEFAULT_REFERENCE
        defaultMilestoneShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the milestoneList where reference equals to UPDATED_REFERENCE
        defaultMilestoneShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMilestonesByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultMilestoneShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the milestoneList where reference equals to UPDATED_REFERENCE
        defaultMilestoneShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMilestonesByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where reference is not null
        defaultMilestoneShouldBeFound("reference.specified=true");

        // Get all the milestoneList where reference is null
        defaultMilestoneShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByReferenceContainsSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where reference contains DEFAULT_REFERENCE
        defaultMilestoneShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the milestoneList where reference contains UPDATED_REFERENCE
        defaultMilestoneShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMilestonesByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where reference does not contain DEFAULT_REFERENCE
        defaultMilestoneShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the milestoneList where reference does not contain UPDATED_REFERENCE
        defaultMilestoneShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMilestonesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where startDate equals to DEFAULT_START_DATE
        defaultMilestoneShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the milestoneList where startDate equals to UPDATED_START_DATE
        defaultMilestoneShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllMilestonesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultMilestoneShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the milestoneList where startDate equals to UPDATED_START_DATE
        defaultMilestoneShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllMilestonesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where startDate is not null
        defaultMilestoneShouldBeFound("startDate.specified=true");

        // Get all the milestoneList where startDate is null
        defaultMilestoneShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where endDate equals to DEFAULT_END_DATE
        defaultMilestoneShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the milestoneList where endDate equals to UPDATED_END_DATE
        defaultMilestoneShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMilestonesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultMilestoneShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the milestoneList where endDate equals to UPDATED_END_DATE
        defaultMilestoneShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMilestonesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where endDate is not null
        defaultMilestoneShouldBeFound("endDate.specified=true");

        // Get all the milestoneList where endDate is null
        defaultMilestoneShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByIsCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where isCompleted equals to DEFAULT_IS_COMPLETED
        defaultMilestoneShouldBeFound("isCompleted.equals=" + DEFAULT_IS_COMPLETED);

        // Get all the milestoneList where isCompleted equals to UPDATED_IS_COMPLETED
        defaultMilestoneShouldNotBeFound("isCompleted.equals=" + UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void getAllMilestonesByIsCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where isCompleted in DEFAULT_IS_COMPLETED or UPDATED_IS_COMPLETED
        defaultMilestoneShouldBeFound("isCompleted.in=" + DEFAULT_IS_COMPLETED + "," + UPDATED_IS_COMPLETED);

        // Get all the milestoneList where isCompleted equals to UPDATED_IS_COMPLETED
        defaultMilestoneShouldNotBeFound("isCompleted.in=" + UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void getAllMilestonesByIsCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList where isCompleted is not null
        defaultMilestoneShouldBeFound("isCompleted.specified=true");

        // Get all the milestoneList where isCompleted is null
        defaultMilestoneShouldNotBeFound("isCompleted.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByParentMilestoneIsEqualToSomething() throws Exception {
        Milestone parentMilestone;
        if (TestUtil.findAll(em, Milestone.class).isEmpty()) {
            milestoneRepository.saveAndFlush(milestone);
            parentMilestone = MilestoneResourceIT.createEntity(em);
        } else {
            parentMilestone = TestUtil.findAll(em, Milestone.class).get(0);
        }
        em.persist(parentMilestone);
        em.flush();
        milestone.setParentMilestone(parentMilestone);
        milestoneRepository.saveAndFlush(milestone);
        Long parentMilestoneId = parentMilestone.getId();

        // Get all the milestoneList where parentMilestone equals to parentMilestoneId
        defaultMilestoneShouldBeFound("parentMilestoneId.equals=" + parentMilestoneId);

        // Get all the milestoneList where parentMilestone equals to (parentMilestoneId + 1)
        defaultMilestoneShouldNotBeFound("parentMilestoneId.equals=" + (parentMilestoneId + 1));
    }

    @Test
    @Transactional
    void getAllMilestonesByProjectIsEqualToSomething() throws Exception {
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            milestoneRepository.saveAndFlush(milestone);
            project = ProjectResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        milestone.setProject(project);
        milestoneRepository.saveAndFlush(milestone);
        Long projectId = project.getId();

        // Get all the milestoneList where project equals to projectId
        defaultMilestoneShouldBeFound("projectId.equals=" + projectId);

        // Get all the milestoneList where project equals to (projectId + 1)
        defaultMilestoneShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllMilestonesByMilestoneParentmilestoneIsEqualToSomething() throws Exception {
        Milestone milestoneParentmilestone;
        if (TestUtil.findAll(em, Milestone.class).isEmpty()) {
            milestoneRepository.saveAndFlush(milestone);
            milestoneParentmilestone = MilestoneResourceIT.createEntity(em);
        } else {
            milestoneParentmilestone = TestUtil.findAll(em, Milestone.class).get(0);
        }
        em.persist(milestoneParentmilestone);
        em.flush();
        milestone.addMilestoneParentmilestone(milestoneParentmilestone);
        milestoneRepository.saveAndFlush(milestone);
        Long milestoneParentmilestoneId = milestoneParentmilestone.getId();

        // Get all the milestoneList where milestoneParentmilestone equals to milestoneParentmilestoneId
        defaultMilestoneShouldBeFound("milestoneParentmilestoneId.equals=" + milestoneParentmilestoneId);

        // Get all the milestoneList where milestoneParentmilestone equals to (milestoneParentmilestoneId + 1)
        defaultMilestoneShouldNotBeFound("milestoneParentmilestoneId.equals=" + (milestoneParentmilestoneId + 1));
    }

    @Test
    @Transactional
    void getAllMilestonesByTestcaseMilestoneIsEqualToSomething() throws Exception {
        TestCase testcaseMilestone;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            milestoneRepository.saveAndFlush(milestone);
            testcaseMilestone = TestCaseResourceIT.createEntity(em);
        } else {
            testcaseMilestone = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testcaseMilestone);
        em.flush();
        milestone.addTestcaseMilestone(testcaseMilestone);
        milestoneRepository.saveAndFlush(milestone);
        Long testcaseMilestoneId = testcaseMilestone.getId();

        // Get all the milestoneList where testcaseMilestone equals to testcaseMilestoneId
        defaultMilestoneShouldBeFound("testcaseMilestoneId.equals=" + testcaseMilestoneId);

        // Get all the milestoneList where testcaseMilestone equals to (testcaseMilestoneId + 1)
        defaultMilestoneShouldNotBeFound("testcaseMilestoneId.equals=" + (testcaseMilestoneId + 1));
    }

    @Test
    @Transactional
    void getAllMilestonesByTestrunMilestoneIsEqualToSomething() throws Exception {
        TestRun testrunMilestone;
        if (TestUtil.findAll(em, TestRun.class).isEmpty()) {
            milestoneRepository.saveAndFlush(milestone);
            testrunMilestone = TestRunResourceIT.createEntity(em);
        } else {
            testrunMilestone = TestUtil.findAll(em, TestRun.class).get(0);
        }
        em.persist(testrunMilestone);
        em.flush();
        milestone.addTestrunMilestone(testrunMilestone);
        milestoneRepository.saveAndFlush(milestone);
        Long testrunMilestoneId = testrunMilestone.getId();

        // Get all the milestoneList where testrunMilestone equals to testrunMilestoneId
        defaultMilestoneShouldBeFound("testrunMilestoneId.equals=" + testrunMilestoneId);

        // Get all the milestoneList where testrunMilestone equals to (testrunMilestoneId + 1)
        defaultMilestoneShouldNotBeFound("testrunMilestoneId.equals=" + (testrunMilestoneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMilestoneShouldBeFound(String filter) throws Exception {
        restMilestoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(milestone.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED.booleanValue())));

        // Check, that the count call also returns 1
        restMilestoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMilestoneShouldNotBeFound(String filter) throws Exception {
        restMilestoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMilestoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMilestone() throws Exception {
        // Get the milestone
        restMilestoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMilestone() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();

        // Update the milestone
        Milestone updatedMilestone = milestoneRepository.findById(milestone.getId()).get();
        // Disconnect from session so that the updates on updatedMilestone are not directly saved in db
        em.detach(updatedMilestone);
        updatedMilestone
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reference(UPDATED_REFERENCE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCompleted(UPDATED_IS_COMPLETED);

        restMilestoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMilestone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMilestone))
            )
            .andExpect(status().isOk());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
        Milestone testMilestone = milestoneList.get(milestoneList.size() - 1);
        assertThat(testMilestone.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMilestone.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMilestone.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMilestone.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMilestone.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMilestone.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void putNonExistingMilestone() throws Exception {
        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();
        milestone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMilestoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, milestone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(milestone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMilestone() throws Exception {
        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();
        milestone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilestoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(milestone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMilestone() throws Exception {
        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();
        milestone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilestoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMilestoneWithPatch() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();

        // Update the milestone using partial update
        Milestone partialUpdatedMilestone = new Milestone();
        partialUpdatedMilestone.setId(milestone.getId());

        partialUpdatedMilestone.reference(UPDATED_REFERENCE).startDate(UPDATED_START_DATE).isCompleted(UPDATED_IS_COMPLETED);

        restMilestoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMilestone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMilestone))
            )
            .andExpect(status().isOk());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
        Milestone testMilestone = milestoneList.get(milestoneList.size() - 1);
        assertThat(testMilestone.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMilestone.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMilestone.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMilestone.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMilestone.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMilestone.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void fullUpdateMilestoneWithPatch() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();

        // Update the milestone using partial update
        Milestone partialUpdatedMilestone = new Milestone();
        partialUpdatedMilestone.setId(milestone.getId());

        partialUpdatedMilestone
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reference(UPDATED_REFERENCE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCompleted(UPDATED_IS_COMPLETED);

        restMilestoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMilestone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMilestone))
            )
            .andExpect(status().isOk());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
        Milestone testMilestone = milestoneList.get(milestoneList.size() - 1);
        assertThat(testMilestone.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMilestone.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMilestone.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMilestone.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMilestone.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMilestone.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void patchNonExistingMilestone() throws Exception {
        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();
        milestone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMilestoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, milestone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(milestone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMilestone() throws Exception {
        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();
        milestone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilestoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(milestone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMilestone() throws Exception {
        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();
        milestone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilestoneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(milestone))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMilestone() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        int databaseSizeBeforeDelete = milestoneRepository.findAll().size();

        // Delete the milestone
        restMilestoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, milestone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
