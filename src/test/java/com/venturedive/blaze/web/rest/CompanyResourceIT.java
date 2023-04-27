package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.ApplicationUser;
import com.venturedive.blaze.domain.Company;
import com.venturedive.blaze.domain.Project;
import com.venturedive.blaze.domain.Template;
import com.venturedive.blaze.domain.TemplateField;
import com.venturedive.blaze.repository.CompanyRepository;
import com.venturedive.blaze.service.criteria.CompanyCriteria;
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
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyResourceIT {

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXPECTED_NO_OF_USERS = 1;
    private static final Integer UPDATED_EXPECTED_NO_OF_USERS = 2;
    private static final Integer SMALLER_EXPECTED_NO_OF_USERS = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .country(DEFAULT_COUNTRY)
            .companyAddress(DEFAULT_COMPANY_ADDRESS)
            .organization(DEFAULT_ORGANIZATION)
            .expectedNoOfUsers(DEFAULT_EXPECTED_NO_OF_USERS)
            .createdAt(DEFAULT_CREATED_AT);
        return company;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .country(UPDATED_COUNTRY)
            .companyAddress(UPDATED_COMPANY_ADDRESS)
            .organization(UPDATED_ORGANIZATION)
            .expectedNoOfUsers(UPDATED_EXPECTED_NO_OF_USERS)
            .createdAt(UPDATED_CREATED_AT);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();
        // Create the Company
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCompany.getCompanyAddress()).isEqualTo(DEFAULT_COMPANY_ADDRESS);
        assertThat(testCompany.getOrganization()).isEqualTo(DEFAULT_ORGANIZATION);
        assertThat(testCompany.getExpectedNoOfUsers()).isEqualTo(DEFAULT_EXPECTED_NO_OF_USERS);
        assertThat(testCompany.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId(1L);

        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].companyAddress").value(hasItem(DEFAULT_COMPANY_ADDRESS)))
            .andExpect(jsonPath("$.[*].organization").value(hasItem(DEFAULT_ORGANIZATION)))
            .andExpect(jsonPath("$.[*].expectedNoOfUsers").value(hasItem(DEFAULT_EXPECTED_NO_OF_USERS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.companyAddress").value(DEFAULT_COMPANY_ADDRESS))
            .andExpect(jsonPath("$.organization").value(DEFAULT_ORGANIZATION))
            .andExpect(jsonPath("$.expectedNoOfUsers").value(DEFAULT_EXPECTED_NO_OF_USERS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        Long id = company.getId();

        defaultCompanyShouldBeFound("id.equals=" + id);
        defaultCompanyShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country equals to DEFAULT_COUNTRY
        defaultCompanyShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the companyList where country equals to UPDATED_COUNTRY
        defaultCompanyShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultCompanyShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the companyList where country equals to UPDATED_COUNTRY
        defaultCompanyShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country is not null
        defaultCompanyShouldBeFound("country.specified=true");

        // Get all the companyList where country is null
        defaultCompanyShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country contains DEFAULT_COUNTRY
        defaultCompanyShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the companyList where country contains UPDATED_COUNTRY
        defaultCompanyShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country does not contain DEFAULT_COUNTRY
        defaultCompanyShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the companyList where country does not contain UPDATED_COUNTRY
        defaultCompanyShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyAddress equals to DEFAULT_COMPANY_ADDRESS
        defaultCompanyShouldBeFound("companyAddress.equals=" + DEFAULT_COMPANY_ADDRESS);

        // Get all the companyList where companyAddress equals to UPDATED_COMPANY_ADDRESS
        defaultCompanyShouldNotBeFound("companyAddress.equals=" + UPDATED_COMPANY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyAddressIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyAddress in DEFAULT_COMPANY_ADDRESS or UPDATED_COMPANY_ADDRESS
        defaultCompanyShouldBeFound("companyAddress.in=" + DEFAULT_COMPANY_ADDRESS + "," + UPDATED_COMPANY_ADDRESS);

        // Get all the companyList where companyAddress equals to UPDATED_COMPANY_ADDRESS
        defaultCompanyShouldNotBeFound("companyAddress.in=" + UPDATED_COMPANY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyAddress is not null
        defaultCompanyShouldBeFound("companyAddress.specified=true");

        // Get all the companyList where companyAddress is null
        defaultCompanyShouldNotBeFound("companyAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyAddressContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyAddress contains DEFAULT_COMPANY_ADDRESS
        defaultCompanyShouldBeFound("companyAddress.contains=" + DEFAULT_COMPANY_ADDRESS);

        // Get all the companyList where companyAddress contains UPDATED_COMPANY_ADDRESS
        defaultCompanyShouldNotBeFound("companyAddress.contains=" + UPDATED_COMPANY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyAddressNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyAddress does not contain DEFAULT_COMPANY_ADDRESS
        defaultCompanyShouldNotBeFound("companyAddress.doesNotContain=" + DEFAULT_COMPANY_ADDRESS);

        // Get all the companyList where companyAddress does not contain UPDATED_COMPANY_ADDRESS
        defaultCompanyShouldBeFound("companyAddress.doesNotContain=" + UPDATED_COMPANY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where organization equals to DEFAULT_ORGANIZATION
        defaultCompanyShouldBeFound("organization.equals=" + DEFAULT_ORGANIZATION);

        // Get all the companyList where organization equals to UPDATED_ORGANIZATION
        defaultCompanyShouldNotBeFound("organization.equals=" + UPDATED_ORGANIZATION);
    }

    @Test
    @Transactional
    void getAllCompaniesByOrganizationIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where organization in DEFAULT_ORGANIZATION or UPDATED_ORGANIZATION
        defaultCompanyShouldBeFound("organization.in=" + DEFAULT_ORGANIZATION + "," + UPDATED_ORGANIZATION);

        // Get all the companyList where organization equals to UPDATED_ORGANIZATION
        defaultCompanyShouldNotBeFound("organization.in=" + UPDATED_ORGANIZATION);
    }

    @Test
    @Transactional
    void getAllCompaniesByOrganizationIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where organization is not null
        defaultCompanyShouldBeFound("organization.specified=true");

        // Get all the companyList where organization is null
        defaultCompanyShouldNotBeFound("organization.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByOrganizationContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where organization contains DEFAULT_ORGANIZATION
        defaultCompanyShouldBeFound("organization.contains=" + DEFAULT_ORGANIZATION);

        // Get all the companyList where organization contains UPDATED_ORGANIZATION
        defaultCompanyShouldNotBeFound("organization.contains=" + UPDATED_ORGANIZATION);
    }

    @Test
    @Transactional
    void getAllCompaniesByOrganizationNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where organization does not contain DEFAULT_ORGANIZATION
        defaultCompanyShouldNotBeFound("organization.doesNotContain=" + DEFAULT_ORGANIZATION);

        // Get all the companyList where organization does not contain UPDATED_ORGANIZATION
        defaultCompanyShouldBeFound("organization.doesNotContain=" + UPDATED_ORGANIZATION);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expectedNoOfUsers equals to DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompanyShouldBeFound("expectedNoOfUsers.equals=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companyList where expectedNoOfUsers equals to UPDATED_EXPECTED_NO_OF_USERS
        defaultCompanyShouldNotBeFound("expectedNoOfUsers.equals=" + UPDATED_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expectedNoOfUsers in DEFAULT_EXPECTED_NO_OF_USERS or UPDATED_EXPECTED_NO_OF_USERS
        defaultCompanyShouldBeFound("expectedNoOfUsers.in=" + DEFAULT_EXPECTED_NO_OF_USERS + "," + UPDATED_EXPECTED_NO_OF_USERS);

        // Get all the companyList where expectedNoOfUsers equals to UPDATED_EXPECTED_NO_OF_USERS
        defaultCompanyShouldNotBeFound("expectedNoOfUsers.in=" + UPDATED_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expectedNoOfUsers is not null
        defaultCompanyShouldBeFound("expectedNoOfUsers.specified=true");

        // Get all the companyList where expectedNoOfUsers is null
        defaultCompanyShouldNotBeFound("expectedNoOfUsers.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expectedNoOfUsers is greater than or equal to DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompanyShouldBeFound("expectedNoOfUsers.greaterThanOrEqual=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companyList where expectedNoOfUsers is greater than or equal to UPDATED_EXPECTED_NO_OF_USERS
        defaultCompanyShouldNotBeFound("expectedNoOfUsers.greaterThanOrEqual=" + UPDATED_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expectedNoOfUsers is less than or equal to DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompanyShouldBeFound("expectedNoOfUsers.lessThanOrEqual=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companyList where expectedNoOfUsers is less than or equal to SMALLER_EXPECTED_NO_OF_USERS
        defaultCompanyShouldNotBeFound("expectedNoOfUsers.lessThanOrEqual=" + SMALLER_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsLessThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expectedNoOfUsers is less than DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompanyShouldNotBeFound("expectedNoOfUsers.lessThan=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companyList where expectedNoOfUsers is less than UPDATED_EXPECTED_NO_OF_USERS
        defaultCompanyShouldBeFound("expectedNoOfUsers.lessThan=" + UPDATED_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expectedNoOfUsers is greater than DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompanyShouldNotBeFound("expectedNoOfUsers.greaterThan=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companyList where expectedNoOfUsers is greater than SMALLER_EXPECTED_NO_OF_USERS
        defaultCompanyShouldBeFound("expectedNoOfUsers.greaterThan=" + SMALLER_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where createdAt equals to DEFAULT_CREATED_AT
        defaultCompanyShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the companyList where createdAt equals to UPDATED_CREATED_AT
        defaultCompanyShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultCompanyShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the companyList where createdAt equals to UPDATED_CREATED_AT
        defaultCompanyShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where createdAt is not null
        defaultCompanyShouldBeFound("createdAt.specified=true");

        // Get all the companyList where createdAt is null
        defaultCompanyShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByApplicationuserCompanyIsEqualToSomething() throws Exception {
        ApplicationUser applicationuserCompany;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            companyRepository.saveAndFlush(company);
            applicationuserCompany = ApplicationUserResourceIT.createEntity(em);
        } else {
            applicationuserCompany = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(applicationuserCompany);
        em.flush();
        company.addApplicationuserCompany(applicationuserCompany);
        companyRepository.saveAndFlush(company);
        Long applicationuserCompanyId = applicationuserCompany.getId();

        // Get all the companyList where applicationuserCompany equals to applicationuserCompanyId
        defaultCompanyShouldBeFound("applicationuserCompanyId.equals=" + applicationuserCompanyId);

        // Get all the companyList where applicationuserCompany equals to (applicationuserCompanyId + 1)
        defaultCompanyShouldNotBeFound("applicationuserCompanyId.equals=" + (applicationuserCompanyId + 1));
    }

    @Test
    @Transactional
    void getAllCompaniesByProjectCompanyIsEqualToSomething() throws Exception {
        Project projectCompany;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            companyRepository.saveAndFlush(company);
            projectCompany = ProjectResourceIT.createEntity(em);
        } else {
            projectCompany = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(projectCompany);
        em.flush();
        company.addProjectCompany(projectCompany);
        companyRepository.saveAndFlush(company);
        Long projectCompanyId = projectCompany.getId();

        // Get all the companyList where projectCompany equals to projectCompanyId
        defaultCompanyShouldBeFound("projectCompanyId.equals=" + projectCompanyId);

        // Get all the companyList where projectCompany equals to (projectCompanyId + 1)
        defaultCompanyShouldNotBeFound("projectCompanyId.equals=" + (projectCompanyId + 1));
    }

    @Test
    @Transactional
    void getAllCompaniesByTemplateCompanyIsEqualToSomething() throws Exception {
        Template templateCompany;
        if (TestUtil.findAll(em, Template.class).isEmpty()) {
            companyRepository.saveAndFlush(company);
            templateCompany = TemplateResourceIT.createEntity(em);
        } else {
            templateCompany = TestUtil.findAll(em, Template.class).get(0);
        }
        em.persist(templateCompany);
        em.flush();
        company.addTemplateCompany(templateCompany);
        companyRepository.saveAndFlush(company);
        Long templateCompanyId = templateCompany.getId();

        // Get all the companyList where templateCompany equals to templateCompanyId
        defaultCompanyShouldBeFound("templateCompanyId.equals=" + templateCompanyId);

        // Get all the companyList where templateCompany equals to (templateCompanyId + 1)
        defaultCompanyShouldNotBeFound("templateCompanyId.equals=" + (templateCompanyId + 1));
    }

    @Test
    @Transactional
    void getAllCompaniesByTemplatefieldCompanyIsEqualToSomething() throws Exception {
        TemplateField templatefieldCompany;
        if (TestUtil.findAll(em, TemplateField.class).isEmpty()) {
            companyRepository.saveAndFlush(company);
            templatefieldCompany = TemplateFieldResourceIT.createEntity(em);
        } else {
            templatefieldCompany = TestUtil.findAll(em, TemplateField.class).get(0);
        }
        em.persist(templatefieldCompany);
        em.flush();
        company.addTemplatefieldCompany(templatefieldCompany);
        companyRepository.saveAndFlush(company);
        Long templatefieldCompanyId = templatefieldCompany.getId();

        // Get all the companyList where templatefieldCompany equals to templatefieldCompanyId
        defaultCompanyShouldBeFound("templatefieldCompanyId.equals=" + templatefieldCompanyId);

        // Get all the companyList where templatefieldCompany equals to (templatefieldCompanyId + 1)
        defaultCompanyShouldNotBeFound("templatefieldCompanyId.equals=" + (templatefieldCompanyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].companyAddress").value(hasItem(DEFAULT_COMPANY_ADDRESS)))
            .andExpect(jsonPath("$.[*].organization").value(hasItem(DEFAULT_ORGANIZATION)))
            .andExpect(jsonPath("$.[*].expectedNoOfUsers").value(hasItem(DEFAULT_EXPECTED_NO_OF_USERS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .country(UPDATED_COUNTRY)
            .companyAddress(UPDATED_COMPANY_ADDRESS)
            .organization(UPDATED_ORGANIZATION)
            .expectedNoOfUsers(UPDATED_EXPECTED_NO_OF_USERS)
            .createdAt(UPDATED_CREATED_AT);

        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCompany.getCompanyAddress()).isEqualTo(UPDATED_COMPANY_ADDRESS);
        assertThat(testCompany.getOrganization()).isEqualTo(UPDATED_ORGANIZATION);
        assertThat(testCompany.getExpectedNoOfUsers()).isEqualTo(UPDATED_EXPECTED_NO_OF_USERS);
        assertThat(testCompany.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, company.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany.companyAddress(UPDATED_COMPANY_ADDRESS).organization(UPDATED_ORGANIZATION).createdAt(UPDATED_CREATED_AT);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCompany.getCompanyAddress()).isEqualTo(UPDATED_COMPANY_ADDRESS);
        assertThat(testCompany.getOrganization()).isEqualTo(UPDATED_ORGANIZATION);
        assertThat(testCompany.getExpectedNoOfUsers()).isEqualTo(DEFAULT_EXPECTED_NO_OF_USERS);
        assertThat(testCompany.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .country(UPDATED_COUNTRY)
            .companyAddress(UPDATED_COMPANY_ADDRESS)
            .organization(UPDATED_ORGANIZATION)
            .expectedNoOfUsers(UPDATED_EXPECTED_NO_OF_USERS)
            .createdAt(UPDATED_CREATED_AT);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCompany.getCompanyAddress()).isEqualTo(UPDATED_COMPANY_ADDRESS);
        assertThat(testCompany.getOrganization()).isEqualTo(UPDATED_ORGANIZATION);
        assertThat(testCompany.getExpectedNoOfUsers()).isEqualTo(UPDATED_EXPECTED_NO_OF_USERS);
        assertThat(testCompany.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, company.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, company.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
