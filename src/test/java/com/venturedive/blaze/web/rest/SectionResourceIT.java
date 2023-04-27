package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.Section;
import com.venturedive.blaze.domain.Section;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.domain.TestSuite;
import com.venturedive.blaze.repository.SectionRepository;
import com.venturedive.blaze.service.criteria.SectionCriteria;
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
 * Integration tests for the {@link SectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SectionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;
    private static final Integer SMALLER_UPDATED_BY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSectionMockMvc;

    private Section section;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createEntity(EntityManager em) {
        Section section = new Section()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY);
        return section;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createUpdatedEntity(EntityManager em) {
        Section section = new Section()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        return section;
    }

    @BeforeEach
    public void initTest() {
        section = createEntity(em);
    }

    @Test
    @Transactional
    void createSection() throws Exception {
        int databaseSizeBeforeCreate = sectionRepository.findAll().size();
        // Create the Section
        restSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isCreated());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate + 1);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSection.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSection.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSection.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSection.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createSectionWithExistingId() throws Exception {
        // Create the Section with an existing ID
        section.setId(1L);

        int databaseSizeBeforeCreate = sectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSections() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get the section
        restSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, section.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(section.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getSectionsByIdFiltering() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        Long id = section.getId();

        defaultSectionShouldBeFound("id.equals=" + id);
        defaultSectionShouldNotBeFound("id.notEquals=" + id);

        defaultSectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSectionShouldNotBeFound("id.greaterThan=" + id);

        defaultSectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSectionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSectionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where name equals to DEFAULT_NAME
        defaultSectionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sectionList where name equals to UPDATED_NAME
        defaultSectionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSectionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sectionList where name equals to UPDATED_NAME
        defaultSectionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where name is not null
        defaultSectionShouldBeFound("name.specified=true");

        // Get all the sectionList where name is null
        defaultSectionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByNameContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where name contains DEFAULT_NAME
        defaultSectionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sectionList where name contains UPDATED_NAME
        defaultSectionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where name does not contain DEFAULT_NAME
        defaultSectionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sectionList where name does not contain UPDATED_NAME
        defaultSectionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where description equals to DEFAULT_DESCRIPTION
        defaultSectionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the sectionList where description equals to UPDATED_DESCRIPTION
        defaultSectionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSectionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSectionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the sectionList where description equals to UPDATED_DESCRIPTION
        defaultSectionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSectionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where description is not null
        defaultSectionShouldBeFound("description.specified=true");

        // Get all the sectionList where description is null
        defaultSectionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where description contains DEFAULT_DESCRIPTION
        defaultSectionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the sectionList where description contains UPDATED_DESCRIPTION
        defaultSectionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSectionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where description does not contain DEFAULT_DESCRIPTION
        defaultSectionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the sectionList where description does not contain UPDATED_DESCRIPTION
        defaultSectionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdAt equals to DEFAULT_CREATED_AT
        defaultSectionShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the sectionList where createdAt equals to UPDATED_CREATED_AT
        defaultSectionShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSectionShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the sectionList where createdAt equals to UPDATED_CREATED_AT
        defaultSectionShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdAt is not null
        defaultSectionShouldBeFound("createdAt.specified=true");

        // Get all the sectionList where createdAt is null
        defaultSectionShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdBy equals to DEFAULT_CREATED_BY
        defaultSectionShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the sectionList where createdBy equals to UPDATED_CREATED_BY
        defaultSectionShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultSectionShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the sectionList where createdBy equals to UPDATED_CREATED_BY
        defaultSectionShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdBy is not null
        defaultSectionShouldBeFound("createdBy.specified=true");

        // Get all the sectionList where createdBy is null
        defaultSectionShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultSectionShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the sectionList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultSectionShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultSectionShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the sectionList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultSectionShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdBy is less than DEFAULT_CREATED_BY
        defaultSectionShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the sectionList where createdBy is less than UPDATED_CREATED_BY
        defaultSectionShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where createdBy is greater than DEFAULT_CREATED_BY
        defaultSectionShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the sectionList where createdBy is greater than SMALLER_CREATED_BY
        defaultSectionShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSectionShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the sectionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSectionShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSectionShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the sectionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSectionShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedAt is not null
        defaultSectionShouldBeFound("updatedAt.specified=true");

        // Get all the sectionList where updatedAt is null
        defaultSectionShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultSectionShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the sectionList where updatedBy equals to UPDATED_UPDATED_BY
        defaultSectionShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultSectionShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the sectionList where updatedBy equals to UPDATED_UPDATED_BY
        defaultSectionShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedBy is not null
        defaultSectionShouldBeFound("updatedBy.specified=true");

        // Get all the sectionList where updatedBy is null
        defaultSectionShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultSectionShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the sectionList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultSectionShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultSectionShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the sectionList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultSectionShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultSectionShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the sectionList where updatedBy is less than UPDATED_UPDATED_BY
        defaultSectionShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultSectionShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the sectionList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultSectionShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByTestSuiteIsEqualToSomething() throws Exception {
        TestSuite testSuite;
        if (TestUtil.findAll(em, TestSuite.class).isEmpty()) {
            sectionRepository.saveAndFlush(section);
            testSuite = TestSuiteResourceIT.createEntity(em);
        } else {
            testSuite = TestUtil.findAll(em, TestSuite.class).get(0);
        }
        em.persist(testSuite);
        em.flush();
        section.setTestSuite(testSuite);
        sectionRepository.saveAndFlush(section);
        Long testSuiteId = testSuite.getId();

        // Get all the sectionList where testSuite equals to testSuiteId
        defaultSectionShouldBeFound("testSuiteId.equals=" + testSuiteId);

        // Get all the sectionList where testSuite equals to (testSuiteId + 1)
        defaultSectionShouldNotBeFound("testSuiteId.equals=" + (testSuiteId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsByParentSectionIsEqualToSomething() throws Exception {
        Section parentSection;
        if (TestUtil.findAll(em, Section.class).isEmpty()) {
            sectionRepository.saveAndFlush(section);
            parentSection = SectionResourceIT.createEntity(em);
        } else {
            parentSection = TestUtil.findAll(em, Section.class).get(0);
        }
        em.persist(parentSection);
        em.flush();
        section.setParentSection(parentSection);
        sectionRepository.saveAndFlush(section);
        Long parentSectionId = parentSection.getId();

        // Get all the sectionList where parentSection equals to parentSectionId
        defaultSectionShouldBeFound("parentSectionId.equals=" + parentSectionId);

        // Get all the sectionList where parentSection equals to (parentSectionId + 1)
        defaultSectionShouldNotBeFound("parentSectionId.equals=" + (parentSectionId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsBySectionParentsectionIsEqualToSomething() throws Exception {
        Section sectionParentsection;
        if (TestUtil.findAll(em, Section.class).isEmpty()) {
            sectionRepository.saveAndFlush(section);
            sectionParentsection = SectionResourceIT.createEntity(em);
        } else {
            sectionParentsection = TestUtil.findAll(em, Section.class).get(0);
        }
        em.persist(sectionParentsection);
        em.flush();
        section.addSectionParentsection(sectionParentsection);
        sectionRepository.saveAndFlush(section);
        Long sectionParentsectionId = sectionParentsection.getId();

        // Get all the sectionList where sectionParentsection equals to sectionParentsectionId
        defaultSectionShouldBeFound("sectionParentsectionId.equals=" + sectionParentsectionId);

        // Get all the sectionList where sectionParentsection equals to (sectionParentsectionId + 1)
        defaultSectionShouldNotBeFound("sectionParentsectionId.equals=" + (sectionParentsectionId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsByTestcaseSectionIsEqualToSomething() throws Exception {
        TestCase testcaseSection;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            sectionRepository.saveAndFlush(section);
            testcaseSection = TestCaseResourceIT.createEntity(em);
        } else {
            testcaseSection = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testcaseSection);
        em.flush();
        section.addTestcaseSection(testcaseSection);
        sectionRepository.saveAndFlush(section);
        Long testcaseSectionId = testcaseSection.getId();

        // Get all the sectionList where testcaseSection equals to testcaseSectionId
        defaultSectionShouldBeFound("testcaseSectionId.equals=" + testcaseSectionId);

        // Get all the sectionList where testcaseSection equals to (testcaseSectionId + 1)
        defaultSectionShouldNotBeFound("testcaseSectionId.equals=" + (testcaseSectionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSectionShouldBeFound(String filter) throws Exception {
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));

        // Check, that the count call also returns 1
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSectionShouldNotBeFound(String filter) throws Exception {
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSection() throws Exception {
        // Get the section
        restSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section
        Section updatedSection = sectionRepository.findById(section.getId()).get();
        // Disconnect from session so that the updates on updatedSection are not directly saved in db
        em.detach(updatedSection);
        updatedSection
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSection.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSection.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSection.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSection.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, section.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        partialUpdatedSection.description(UPDATED_DESCRIPTION);

        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSection.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSection.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSection.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSection.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        partialUpdatedSection
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSection.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSection.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSection.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSection.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, section.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeDelete = sectionRepository.findAll().size();

        // Delete the section
        restSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, section.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
