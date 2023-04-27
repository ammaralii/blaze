package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.ApplicationUser;
import com.venturedive.blaze.domain.Company;
import com.venturedive.blaze.domain.Milestone;
import com.venturedive.blaze.domain.Project;
import com.venturedive.blaze.domain.Template;
import com.venturedive.blaze.domain.TestPlan;
import com.venturedive.blaze.domain.TestSuite;
import com.venturedive.blaze.repository.ProjectRepository;
import com.venturedive.blaze.service.criteria.ProjectCriteria;
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
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectResourceIT {

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ISACTIVE = false;
    private static final Boolean UPDATED_ISACTIVE = true;

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

    private static final String ENTITY_API_URL = "/api/projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMockMvc;

    private Project project;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .projectName(DEFAULT_PROJECT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .isactive(DEFAULT_ISACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return project;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .isactive(UPDATED_ISACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return project;
    }

    @BeforeEach
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();
        // Create the Project
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProject.getIsactive()).isEqualTo(DEFAULT_ISACTIVE);
        assertThat(testProject.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProject.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProject.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testProject.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createProjectWithExistingId() throws Exception {
        // Create the Project with an existing ID
        project.setId(1L);

        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setProjectName(null);

        // Create the Project, which fails.

        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isactive").value(hasItem(DEFAULT_ISACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isactive").value(DEFAULT_ISACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getProjectsByIdFiltering() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        Long id = project.getId();

        defaultProjectShouldBeFound("id.equals=" + id);
        defaultProjectShouldNotBeFound("id.notEquals=" + id);

        defaultProjectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.greaterThan=" + id);

        defaultProjectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName equals to DEFAULT_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.equals=" + DEFAULT_PROJECT_NAME);

        // Get all the projectList where projectName equals to UPDATED_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.equals=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName in DEFAULT_PROJECT_NAME or UPDATED_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.in=" + DEFAULT_PROJECT_NAME + "," + UPDATED_PROJECT_NAME);

        // Get all the projectList where projectName equals to UPDATED_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.in=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName is not null
        defaultProjectShouldBeFound("projectName.specified=true");

        // Get all the projectList where projectName is null
        defaultProjectShouldNotBeFound("projectName.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName contains DEFAULT_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.contains=" + DEFAULT_PROJECT_NAME);

        // Get all the projectList where projectName contains UPDATED_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.contains=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName does not contain DEFAULT_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.doesNotContain=" + DEFAULT_PROJECT_NAME);

        // Get all the projectList where projectName does not contain UPDATED_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.doesNotContain=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description equals to DEFAULT_DESCRIPTION
        defaultProjectShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description equals to UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the projectList where description equals to UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description is not null
        defaultProjectShouldBeFound("description.specified=true");

        // Get all the projectList where description is null
        defaultProjectShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description contains DEFAULT_DESCRIPTION
        defaultProjectShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description contains UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description does not contain DEFAULT_DESCRIPTION
        defaultProjectShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description does not contain UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByIsactiveIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where isactive equals to DEFAULT_ISACTIVE
        defaultProjectShouldBeFound("isactive.equals=" + DEFAULT_ISACTIVE);

        // Get all the projectList where isactive equals to UPDATED_ISACTIVE
        defaultProjectShouldNotBeFound("isactive.equals=" + UPDATED_ISACTIVE);
    }

    @Test
    @Transactional
    void getAllProjectsByIsactiveIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where isactive in DEFAULT_ISACTIVE or UPDATED_ISACTIVE
        defaultProjectShouldBeFound("isactive.in=" + DEFAULT_ISACTIVE + "," + UPDATED_ISACTIVE);

        // Get all the projectList where isactive equals to UPDATED_ISACTIVE
        defaultProjectShouldNotBeFound("isactive.in=" + UPDATED_ISACTIVE);
    }

    @Test
    @Transactional
    void getAllProjectsByIsactiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where isactive is not null
        defaultProjectShouldBeFound("isactive.specified=true");

        // Get all the projectList where isactive is null
        defaultProjectShouldNotBeFound("isactive.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdBy equals to DEFAULT_CREATED_BY
        defaultProjectShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the projectList where createdBy equals to UPDATED_CREATED_BY
        defaultProjectShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultProjectShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the projectList where createdBy equals to UPDATED_CREATED_BY
        defaultProjectShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdBy is not null
        defaultProjectShouldBeFound("createdBy.specified=true");

        // Get all the projectList where createdBy is null
        defaultProjectShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultProjectShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the projectList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultProjectShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultProjectShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the projectList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultProjectShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdBy is less than DEFAULT_CREATED_BY
        defaultProjectShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the projectList where createdBy is less than UPDATED_CREATED_BY
        defaultProjectShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdBy is greater than DEFAULT_CREATED_BY
        defaultProjectShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the projectList where createdBy is greater than SMALLER_CREATED_BY
        defaultProjectShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdAt equals to DEFAULT_CREATED_AT
        defaultProjectShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the projectList where createdAt equals to UPDATED_CREATED_AT
        defaultProjectShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultProjectShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the projectList where createdAt equals to UPDATED_CREATED_AT
        defaultProjectShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where createdAt is not null
        defaultProjectShouldBeFound("createdAt.specified=true");

        // Get all the projectList where createdAt is null
        defaultProjectShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultProjectShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the projectList where updatedBy equals to UPDATED_UPDATED_BY
        defaultProjectShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultProjectShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the projectList where updatedBy equals to UPDATED_UPDATED_BY
        defaultProjectShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedBy is not null
        defaultProjectShouldBeFound("updatedBy.specified=true");

        // Get all the projectList where updatedBy is null
        defaultProjectShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultProjectShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the projectList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultProjectShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultProjectShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the projectList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultProjectShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultProjectShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the projectList where updatedBy is less than UPDATED_UPDATED_BY
        defaultProjectShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultProjectShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the projectList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultProjectShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultProjectShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the projectList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProjectShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultProjectShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the projectList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProjectShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where updatedAt is not null
        defaultProjectShouldBeFound("updatedAt.specified=true");

        // Get all the projectList where updatedAt is null
        defaultProjectShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByDefaultTemplateIsEqualToSomething() throws Exception {
        Template defaultTemplate;
        if (TestUtil.findAll(em, Template.class).isEmpty()) {
            projectRepository.saveAndFlush(project);
            defaultTemplate = TemplateResourceIT.createEntity(em);
        } else {
            defaultTemplate = TestUtil.findAll(em, Template.class).get(0);
        }
        em.persist(defaultTemplate);
        em.flush();
        project.setDefaultTemplate(defaultTemplate);
        projectRepository.saveAndFlush(project);
        Long defaultTemplateId = defaultTemplate.getId();

        // Get all the projectList where defaultTemplate equals to defaultTemplateId
        defaultProjectShouldBeFound("defaultTemplateId.equals=" + defaultTemplateId);

        // Get all the projectList where defaultTemplate equals to (defaultTemplateId + 1)
        defaultProjectShouldNotBeFound("defaultTemplateId.equals=" + (defaultTemplateId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            projectRepository.saveAndFlush(project);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        project.setCompany(company);
        projectRepository.saveAndFlush(project);
        Long companyId = company.getId();

        // Get all the projectList where company equals to companyId
        defaultProjectShouldBeFound("companyId.equals=" + companyId);

        // Get all the projectList where company equals to (companyId + 1)
        defaultProjectShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByMilestoneProjectIsEqualToSomething() throws Exception {
        Milestone milestoneProject;
        if (TestUtil.findAll(em, Milestone.class).isEmpty()) {
            projectRepository.saveAndFlush(project);
            milestoneProject = MilestoneResourceIT.createEntity(em);
        } else {
            milestoneProject = TestUtil.findAll(em, Milestone.class).get(0);
        }
        em.persist(milestoneProject);
        em.flush();
        project.addMilestoneProject(milestoneProject);
        projectRepository.saveAndFlush(project);
        Long milestoneProjectId = milestoneProject.getId();

        // Get all the projectList where milestoneProject equals to milestoneProjectId
        defaultProjectShouldBeFound("milestoneProjectId.equals=" + milestoneProjectId);

        // Get all the projectList where milestoneProject equals to (milestoneProjectId + 1)
        defaultProjectShouldNotBeFound("milestoneProjectId.equals=" + (milestoneProjectId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByTestplanProjectIsEqualToSomething() throws Exception {
        TestPlan testplanProject;
        if (TestUtil.findAll(em, TestPlan.class).isEmpty()) {
            projectRepository.saveAndFlush(project);
            testplanProject = TestPlanResourceIT.createEntity(em);
        } else {
            testplanProject = TestUtil.findAll(em, TestPlan.class).get(0);
        }
        em.persist(testplanProject);
        em.flush();
        project.addTestplanProject(testplanProject);
        projectRepository.saveAndFlush(project);
        Long testplanProjectId = testplanProject.getId();

        // Get all the projectList where testplanProject equals to testplanProjectId
        defaultProjectShouldBeFound("testplanProjectId.equals=" + testplanProjectId);

        // Get all the projectList where testplanProject equals to (testplanProjectId + 1)
        defaultProjectShouldNotBeFound("testplanProjectId.equals=" + (testplanProjectId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByTestsuiteProjectIsEqualToSomething() throws Exception {
        TestSuite testsuiteProject;
        if (TestUtil.findAll(em, TestSuite.class).isEmpty()) {
            projectRepository.saveAndFlush(project);
            testsuiteProject = TestSuiteResourceIT.createEntity(em);
        } else {
            testsuiteProject = TestUtil.findAll(em, TestSuite.class).get(0);
        }
        em.persist(testsuiteProject);
        em.flush();
        project.addTestsuiteProject(testsuiteProject);
        projectRepository.saveAndFlush(project);
        Long testsuiteProjectId = testsuiteProject.getId();

        // Get all the projectList where testsuiteProject equals to testsuiteProjectId
        defaultProjectShouldBeFound("testsuiteProjectId.equals=" + testsuiteProjectId);

        // Get all the projectList where testsuiteProject equals to (testsuiteProjectId + 1)
        defaultProjectShouldNotBeFound("testsuiteProjectId.equals=" + (testsuiteProjectId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByUserIsEqualToSomething() throws Exception {
        ApplicationUser user;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            projectRepository.saveAndFlush(project);
            user = ApplicationUserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(user);
        em.flush();
        project.addUser(user);
        projectRepository.saveAndFlush(project);
        Long userId = user.getId();

        // Get all the projectList where user equals to userId
        defaultProjectShouldBeFound("userId.equals=" + userId);

        // Get all the projectList where user equals to (userId + 1)
        defaultProjectShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjectShouldBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isactive").value(hasItem(DEFAULT_ISACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjectShouldNotBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).get();
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .isactive(UPDATED_ISACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getIsactive()).isEqualTo(UPDATED_ISACTIVE);
        assertThat(testProject.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProject.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProject.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testProject.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, project.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .projectName(UPDATED_PROJECT_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProject.getIsactive()).isEqualTo(DEFAULT_ISACTIVE);
        assertThat(testProject.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProject.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProject.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testProject.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .isactive(UPDATED_ISACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getIsactive()).isEqualTo(UPDATED_ISACTIVE);
        assertThat(testProject.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProject.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProject.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testProject.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, project.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Delete the project
        restProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, project.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
