package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.ApplicationUser;
import com.venturedive.blaze.domain.Company;
import com.venturedive.blaze.domain.Project;
import com.venturedive.blaze.domain.Role;
import com.venturedive.blaze.repository.ApplicationUserRepository;
import com.venturedive.blaze.service.ApplicationUserService;
import com.venturedive.blaze.service.criteria.ApplicationUserCriteria;
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
 * Integration tests for the {@link ApplicationUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplicationUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_ACTIVE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACTIVE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

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

    private static final String DEFAULT_USER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/application-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepositoryMock;

    @Mock
    private ApplicationUserService applicationUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .password(DEFAULT_PASSWORD)
            .lastActive(DEFAULT_LAST_ACTIVE)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .userEmail(DEFAULT_USER_EMAIL)
            .isDeleted(DEFAULT_IS_DELETED);
        return applicationUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .password(UPDATED_PASSWORD)
            .lastActive(UPDATED_LAST_ACTIVE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .userEmail(UPDATED_USER_EMAIL)
            .isDeleted(UPDATED_IS_DELETED);
        return applicationUser;
    }

    @BeforeEach
    public void initTest() {
        applicationUser = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationUser() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();
        // Create the ApplicationUser
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testApplicationUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testApplicationUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testApplicationUser.getLastActive()).isEqualTo(DEFAULT_LAST_ACTIVE);
        assertThat(testApplicationUser.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testApplicationUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testApplicationUser.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testApplicationUser.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testApplicationUser.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testApplicationUser.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testApplicationUser.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createApplicationUserWithExistingId() throws Exception {
        // Create the ApplicationUser with an existing ID
        applicationUser.setId(1L);

        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setIsDeleted(null);

        // Create the ApplicationUser, which fails.

        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicationUsers() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].lastActive").value(hasItem(DEFAULT_LAST_ACTIVE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(applicationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicationUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(applicationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(applicationUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.lastActive").value(DEFAULT_LAST_ACTIVE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getApplicationUsersByIdFiltering() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        Long id = applicationUser.getId();

        defaultApplicationUserShouldBeFound("id.equals=" + id);
        defaultApplicationUserShouldNotBeFound("id.notEquals=" + id);

        defaultApplicationUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.greaterThan=" + id);

        defaultApplicationUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultApplicationUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the applicationUserList where firstName equals to UPDATED_FIRST_NAME
        defaultApplicationUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultApplicationUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the applicationUserList where firstName equals to UPDATED_FIRST_NAME
        defaultApplicationUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where firstName is not null
        defaultApplicationUserShouldBeFound("firstName.specified=true");

        // Get all the applicationUserList where firstName is null
        defaultApplicationUserShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where firstName contains DEFAULT_FIRST_NAME
        defaultApplicationUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the applicationUserList where firstName contains UPDATED_FIRST_NAME
        defaultApplicationUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultApplicationUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the applicationUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultApplicationUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where lastName equals to DEFAULT_LAST_NAME
        defaultApplicationUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the applicationUserList where lastName equals to UPDATED_LAST_NAME
        defaultApplicationUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultApplicationUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the applicationUserList where lastName equals to UPDATED_LAST_NAME
        defaultApplicationUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where lastName is not null
        defaultApplicationUserShouldBeFound("lastName.specified=true");

        // Get all the applicationUserList where lastName is null
        defaultApplicationUserShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where lastName contains DEFAULT_LAST_NAME
        defaultApplicationUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the applicationUserList where lastName contains UPDATED_LAST_NAME
        defaultApplicationUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultApplicationUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the applicationUserList where lastName does not contain UPDATED_LAST_NAME
        defaultApplicationUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where password equals to DEFAULT_PASSWORD
        defaultApplicationUserShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the applicationUserList where password equals to UPDATED_PASSWORD
        defaultApplicationUserShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultApplicationUserShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the applicationUserList where password equals to UPDATED_PASSWORD
        defaultApplicationUserShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where password is not null
        defaultApplicationUserShouldBeFound("password.specified=true");

        // Get all the applicationUserList where password is null
        defaultApplicationUserShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where password contains DEFAULT_PASSWORD
        defaultApplicationUserShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the applicationUserList where password contains UPDATED_PASSWORD
        defaultApplicationUserShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where password does not contain DEFAULT_PASSWORD
        defaultApplicationUserShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the applicationUserList where password does not contain UPDATED_PASSWORD
        defaultApplicationUserShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByLastActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where lastActive equals to DEFAULT_LAST_ACTIVE
        defaultApplicationUserShouldBeFound("lastActive.equals=" + DEFAULT_LAST_ACTIVE);

        // Get all the applicationUserList where lastActive equals to UPDATED_LAST_ACTIVE
        defaultApplicationUserShouldNotBeFound("lastActive.equals=" + UPDATED_LAST_ACTIVE);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByLastActiveIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where lastActive in DEFAULT_LAST_ACTIVE or UPDATED_LAST_ACTIVE
        defaultApplicationUserShouldBeFound("lastActive.in=" + DEFAULT_LAST_ACTIVE + "," + UPDATED_LAST_ACTIVE);

        // Get all the applicationUserList where lastActive equals to UPDATED_LAST_ACTIVE
        defaultApplicationUserShouldNotBeFound("lastActive.in=" + UPDATED_LAST_ACTIVE);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByLastActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where lastActive is not null
        defaultApplicationUserShouldBeFound("lastActive.specified=true");

        // Get all the applicationUserList where lastActive is null
        defaultApplicationUserShouldNotBeFound("lastActive.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where status equals to DEFAULT_STATUS
        defaultApplicationUserShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the applicationUserList where status equals to UPDATED_STATUS
        defaultApplicationUserShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultApplicationUserShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the applicationUserList where status equals to UPDATED_STATUS
        defaultApplicationUserShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where status is not null
        defaultApplicationUserShouldBeFound("status.specified=true");

        // Get all the applicationUserList where status is null
        defaultApplicationUserShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByStatusContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where status contains DEFAULT_STATUS
        defaultApplicationUserShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the applicationUserList where status contains UPDATED_STATUS
        defaultApplicationUserShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where status does not contain DEFAULT_STATUS
        defaultApplicationUserShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the applicationUserList where status does not contain UPDATED_STATUS
        defaultApplicationUserShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdBy equals to DEFAULT_CREATED_BY
        defaultApplicationUserShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the applicationUserList where createdBy equals to UPDATED_CREATED_BY
        defaultApplicationUserShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultApplicationUserShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the applicationUserList where createdBy equals to UPDATED_CREATED_BY
        defaultApplicationUserShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdBy is not null
        defaultApplicationUserShouldBeFound("createdBy.specified=true");

        // Get all the applicationUserList where createdBy is null
        defaultApplicationUserShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultApplicationUserShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the applicationUserList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultApplicationUserShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultApplicationUserShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the applicationUserList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultApplicationUserShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdBy is less than DEFAULT_CREATED_BY
        defaultApplicationUserShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the applicationUserList where createdBy is less than UPDATED_CREATED_BY
        defaultApplicationUserShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdBy is greater than DEFAULT_CREATED_BY
        defaultApplicationUserShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the applicationUserList where createdBy is greater than SMALLER_CREATED_BY
        defaultApplicationUserShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdAt equals to DEFAULT_CREATED_AT
        defaultApplicationUserShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the applicationUserList where createdAt equals to UPDATED_CREATED_AT
        defaultApplicationUserShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultApplicationUserShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the applicationUserList where createdAt equals to UPDATED_CREATED_AT
        defaultApplicationUserShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where createdAt is not null
        defaultApplicationUserShouldBeFound("createdAt.specified=true");

        // Get all the applicationUserList where createdAt is null
        defaultApplicationUserShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultApplicationUserShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the applicationUserList where updatedBy equals to UPDATED_UPDATED_BY
        defaultApplicationUserShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultApplicationUserShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the applicationUserList where updatedBy equals to UPDATED_UPDATED_BY
        defaultApplicationUserShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedBy is not null
        defaultApplicationUserShouldBeFound("updatedBy.specified=true");

        // Get all the applicationUserList where updatedBy is null
        defaultApplicationUserShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultApplicationUserShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the applicationUserList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultApplicationUserShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultApplicationUserShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the applicationUserList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultApplicationUserShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultApplicationUserShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the applicationUserList where updatedBy is less than UPDATED_UPDATED_BY
        defaultApplicationUserShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultApplicationUserShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the applicationUserList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultApplicationUserShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultApplicationUserShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the applicationUserList where updatedAt equals to UPDATED_UPDATED_AT
        defaultApplicationUserShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultApplicationUserShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the applicationUserList where updatedAt equals to UPDATED_UPDATED_AT
        defaultApplicationUserShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where updatedAt is not null
        defaultApplicationUserShouldBeFound("updatedAt.specified=true");

        // Get all the applicationUserList where updatedAt is null
        defaultApplicationUserShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUserEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where userEmail equals to DEFAULT_USER_EMAIL
        defaultApplicationUserShouldBeFound("userEmail.equals=" + DEFAULT_USER_EMAIL);

        // Get all the applicationUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultApplicationUserShouldNotBeFound("userEmail.equals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUserEmailIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where userEmail in DEFAULT_USER_EMAIL or UPDATED_USER_EMAIL
        defaultApplicationUserShouldBeFound("userEmail.in=" + DEFAULT_USER_EMAIL + "," + UPDATED_USER_EMAIL);

        // Get all the applicationUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultApplicationUserShouldNotBeFound("userEmail.in=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUserEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where userEmail is not null
        defaultApplicationUserShouldBeFound("userEmail.specified=true");

        // Get all the applicationUserList where userEmail is null
        defaultApplicationUserShouldNotBeFound("userEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUserEmailContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where userEmail contains DEFAULT_USER_EMAIL
        defaultApplicationUserShouldBeFound("userEmail.contains=" + DEFAULT_USER_EMAIL);

        // Get all the applicationUserList where userEmail contains UPDATED_USER_EMAIL
        defaultApplicationUserShouldNotBeFound("userEmail.contains=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUserEmailNotContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where userEmail does not contain DEFAULT_USER_EMAIL
        defaultApplicationUserShouldNotBeFound("userEmail.doesNotContain=" + DEFAULT_USER_EMAIL);

        // Get all the applicationUserList where userEmail does not contain UPDATED_USER_EMAIL
        defaultApplicationUserShouldBeFound("userEmail.doesNotContain=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where isDeleted equals to DEFAULT_IS_DELETED
        defaultApplicationUserShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the applicationUserList where isDeleted equals to UPDATED_IS_DELETED
        defaultApplicationUserShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultApplicationUserShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the applicationUserList where isDeleted equals to UPDATED_IS_DELETED
        defaultApplicationUserShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where isDeleted is not null
        defaultApplicationUserShouldBeFound("isDeleted.specified=true");

        // Get all the applicationUserList where isDeleted is null
        defaultApplicationUserShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        applicationUser.setCompany(company);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long companyId = company.getId();

        // Get all the applicationUserList where company equals to companyId
        defaultApplicationUserShouldBeFound("companyId.equals=" + companyId);

        // Get all the applicationUserList where company equals to (companyId + 1)
        defaultApplicationUserShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByProjectIsEqualToSomething() throws Exception {
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            project = ProjectResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        applicationUser.addProject(project);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long projectId = project.getId();

        // Get all the applicationUserList where project equals to projectId
        defaultApplicationUserShouldBeFound("projectId.equals=" + projectId);

        // Get all the applicationUserList where project equals to (projectId + 1)
        defaultApplicationUserShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByRoleIsEqualToSomething() throws Exception {
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            role = RoleResourceIT.createEntity(em);
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        em.persist(role);
        em.flush();
        applicationUser.addRole(role);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long roleId = role.getId();

        // Get all the applicationUserList where role equals to roleId
        defaultApplicationUserShouldBeFound("roleId.equals=" + roleId);

        // Get all the applicationUserList where role equals to (roleId + 1)
        defaultApplicationUserShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationUserShouldBeFound(String filter) throws Exception {
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].lastActive").value(hasItem(DEFAULT_LAST_ACTIVE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationUserShouldNotBeFound(String filter) throws Exception {
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);
        updatedApplicationUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .password(UPDATED_PASSWORD)
            .lastActive(UPDATED_LAST_ACTIVE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .userEmail(UPDATED_USER_EMAIL)
            .isDeleted(UPDATED_IS_DELETED);

        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testApplicationUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicationUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testApplicationUser.getLastActive()).isEqualTo(UPDATED_LAST_ACTIVE);
        assertThat(testApplicationUser.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testApplicationUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApplicationUser.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApplicationUser.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testApplicationUser.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testApplicationUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testApplicationUser.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser
            .lastName(UPDATED_LAST_NAME)
            .password(UPDATED_PASSWORD)
            .createdAt(UPDATED_CREATED_AT)
            .isDeleted(UPDATED_IS_DELETED);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testApplicationUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicationUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testApplicationUser.getLastActive()).isEqualTo(DEFAULT_LAST_ACTIVE);
        assertThat(testApplicationUser.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testApplicationUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testApplicationUser.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApplicationUser.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testApplicationUser.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testApplicationUser.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testApplicationUser.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .password(UPDATED_PASSWORD)
            .lastActive(UPDATED_LAST_ACTIVE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .userEmail(UPDATED_USER_EMAIL)
            .isDeleted(UPDATED_IS_DELETED);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testApplicationUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicationUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testApplicationUser.getLastActive()).isEqualTo(UPDATED_LAST_ACTIVE);
        assertThat(testApplicationUser.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testApplicationUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApplicationUser.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApplicationUser.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testApplicationUser.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testApplicationUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testApplicationUser.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeDelete = applicationUserRepository.findAll().size();

        // Delete the applicationUser
        restApplicationUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
