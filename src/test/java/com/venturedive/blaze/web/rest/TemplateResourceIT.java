package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.Company;
import com.venturedive.blaze.domain.Project;
import com.venturedive.blaze.domain.Template;
import com.venturedive.blaze.domain.TemplateField;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.repository.TemplateRepository;
import com.venturedive.blaze.service.TemplateService;
import com.venturedive.blaze.service.criteria.TemplateCriteria;
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
 * Integration tests for the {@link TemplateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TemplateResourceIT {

    private static final String DEFAULT_TEMPLATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemplateRepository templateRepository;

    @Mock
    private TemplateRepository templateRepositoryMock;

    @Mock
    private TemplateService templateServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemplateMockMvc;

    private Template template;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Template createEntity(EntityManager em) {
        Template template = new Template().templateName(DEFAULT_TEMPLATE_NAME).createdAt(DEFAULT_CREATED_AT).createdBy(DEFAULT_CREATED_BY);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        template.setCompany(company);
        return template;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Template createUpdatedEntity(EntityManager em) {
        Template template = new Template().templateName(UPDATED_TEMPLATE_NAME).createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        template.setCompany(company);
        return template;
    }

    @BeforeEach
    public void initTest() {
        template = createEntity(em);
    }

    @Test
    @Transactional
    void createTemplate() throws Exception {
        int databaseSizeBeforeCreate = templateRepository.findAll().size();
        // Create the Template
        restTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(template)))
            .andExpect(status().isCreated());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeCreate + 1);
        Template testTemplate = templateList.get(templateList.size() - 1);
        assertThat(testTemplate.getTemplateName()).isEqualTo(DEFAULT_TEMPLATE_NAME);
        assertThat(testTemplate.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTemplate.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createTemplateWithExistingId() throws Exception {
        // Create the Template with an existing ID
        template.setId(1L);

        int databaseSizeBeforeCreate = templateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(template)))
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTemplates() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(template.getId().intValue())))
            .andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(templateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(templateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(templateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(templateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get the template
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, template.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(template.getId().intValue()))
            .andExpect(jsonPath("$.templateName").value(DEFAULT_TEMPLATE_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        Long id = template.getId();

        defaultTemplateShouldBeFound("id.equals=" + id);
        defaultTemplateShouldNotBeFound("id.notEquals=" + id);

        defaultTemplateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTemplateShouldNotBeFound("id.greaterThan=" + id);

        defaultTemplateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTemplateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameIsEqualToSomething() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where templateName equals to DEFAULT_TEMPLATE_NAME
        defaultTemplateShouldBeFound("templateName.equals=" + DEFAULT_TEMPLATE_NAME);

        // Get all the templateList where templateName equals to UPDATED_TEMPLATE_NAME
        defaultTemplateShouldNotBeFound("templateName.equals=" + UPDATED_TEMPLATE_NAME);
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameIsInShouldWork() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where templateName in DEFAULT_TEMPLATE_NAME or UPDATED_TEMPLATE_NAME
        defaultTemplateShouldBeFound("templateName.in=" + DEFAULT_TEMPLATE_NAME + "," + UPDATED_TEMPLATE_NAME);

        // Get all the templateList where templateName equals to UPDATED_TEMPLATE_NAME
        defaultTemplateShouldNotBeFound("templateName.in=" + UPDATED_TEMPLATE_NAME);
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where templateName is not null
        defaultTemplateShouldBeFound("templateName.specified=true");

        // Get all the templateList where templateName is null
        defaultTemplateShouldNotBeFound("templateName.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameContainsSomething() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where templateName contains DEFAULT_TEMPLATE_NAME
        defaultTemplateShouldBeFound("templateName.contains=" + DEFAULT_TEMPLATE_NAME);

        // Get all the templateList where templateName contains UPDATED_TEMPLATE_NAME
        defaultTemplateShouldNotBeFound("templateName.contains=" + UPDATED_TEMPLATE_NAME);
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameNotContainsSomething() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where templateName does not contain DEFAULT_TEMPLATE_NAME
        defaultTemplateShouldNotBeFound("templateName.doesNotContain=" + DEFAULT_TEMPLATE_NAME);

        // Get all the templateList where templateName does not contain UPDATED_TEMPLATE_NAME
        defaultTemplateShouldBeFound("templateName.doesNotContain=" + UPDATED_TEMPLATE_NAME);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdAt equals to DEFAULT_CREATED_AT
        defaultTemplateShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the templateList where createdAt equals to UPDATED_CREATED_AT
        defaultTemplateShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTemplateShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the templateList where createdAt equals to UPDATED_CREATED_AT
        defaultTemplateShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdAt is not null
        defaultTemplateShouldBeFound("createdAt.specified=true");

        // Get all the templateList where createdAt is null
        defaultTemplateShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdBy equals to DEFAULT_CREATED_BY
        defaultTemplateShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the templateList where createdBy equals to UPDATED_CREATED_BY
        defaultTemplateShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTemplateShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the templateList where createdBy equals to UPDATED_CREATED_BY
        defaultTemplateShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdBy is not null
        defaultTemplateShouldBeFound("createdBy.specified=true");

        // Get all the templateList where createdBy is null
        defaultTemplateShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTemplateShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the templateList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTemplateShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTemplateShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the templateList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTemplateShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdBy is less than DEFAULT_CREATED_BY
        defaultTemplateShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the templateList where createdBy is less than UPDATED_CREATED_BY
        defaultTemplateShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTemplateShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the templateList where createdBy is greater than SMALLER_CREATED_BY
        defaultTemplateShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            templateRepository.saveAndFlush(template);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        template.setCompany(company);
        templateRepository.saveAndFlush(template);
        Long companyId = company.getId();

        // Get all the templateList where company equals to companyId
        defaultTemplateShouldBeFound("companyId.equals=" + companyId);

        // Get all the templateList where company equals to (companyId + 1)
        defaultTemplateShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateFieldIsEqualToSomething() throws Exception {
        TemplateField templateField;
        if (TestUtil.findAll(em, TemplateField.class).isEmpty()) {
            templateRepository.saveAndFlush(template);
            templateField = TemplateFieldResourceIT.createEntity(em);
        } else {
            templateField = TestUtil.findAll(em, TemplateField.class).get(0);
        }
        em.persist(templateField);
        em.flush();
        template.addTemplateField(templateField);
        templateRepository.saveAndFlush(template);
        Long templateFieldId = templateField.getId();

        // Get all the templateList where templateField equals to templateFieldId
        defaultTemplateShouldBeFound("templateFieldId.equals=" + templateFieldId);

        // Get all the templateList where templateField equals to (templateFieldId + 1)
        defaultTemplateShouldNotBeFound("templateFieldId.equals=" + (templateFieldId + 1));
    }

    @Test
    @Transactional
    void getAllTemplatesByProjectDefaulttemplateIsEqualToSomething() throws Exception {
        Project projectDefaulttemplate;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            templateRepository.saveAndFlush(template);
            projectDefaulttemplate = ProjectResourceIT.createEntity(em);
        } else {
            projectDefaulttemplate = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(projectDefaulttemplate);
        em.flush();
        template.addProjectDefaulttemplate(projectDefaulttemplate);
        templateRepository.saveAndFlush(template);
        Long projectDefaulttemplateId = projectDefaulttemplate.getId();

        // Get all the templateList where projectDefaulttemplate equals to projectDefaulttemplateId
        defaultTemplateShouldBeFound("projectDefaulttemplateId.equals=" + projectDefaulttemplateId);

        // Get all the templateList where projectDefaulttemplate equals to (projectDefaulttemplateId + 1)
        defaultTemplateShouldNotBeFound("projectDefaulttemplateId.equals=" + (projectDefaulttemplateId + 1));
    }

    @Test
    @Transactional
    void getAllTemplatesByTestcaseTemplateIsEqualToSomething() throws Exception {
        TestCase testcaseTemplate;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            templateRepository.saveAndFlush(template);
            testcaseTemplate = TestCaseResourceIT.createEntity(em);
        } else {
            testcaseTemplate = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testcaseTemplate);
        em.flush();
        template.addTestcaseTemplate(testcaseTemplate);
        templateRepository.saveAndFlush(template);
        Long testcaseTemplateId = testcaseTemplate.getId();

        // Get all the templateList where testcaseTemplate equals to testcaseTemplateId
        defaultTemplateShouldBeFound("testcaseTemplateId.equals=" + testcaseTemplateId);

        // Get all the templateList where testcaseTemplate equals to (testcaseTemplateId + 1)
        defaultTemplateShouldNotBeFound("testcaseTemplateId.equals=" + (testcaseTemplateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTemplateShouldBeFound(String filter) throws Exception {
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(template.getId().intValue())))
            .andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTemplateShouldNotBeFound(String filter) throws Exception {
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTemplate() throws Exception {
        // Get the template
        restTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        int databaseSizeBeforeUpdate = templateRepository.findAll().size();

        // Update the template
        Template updatedTemplate = templateRepository.findById(template.getId()).get();
        // Disconnect from session so that the updates on updatedTemplate are not directly saved in db
        em.detach(updatedTemplate);
        updatedTemplate.templateName(UPDATED_TEMPLATE_NAME).createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);

        restTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTemplate))
            )
            .andExpect(status().isOk());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
        Template testTemplate = templateList.get(templateList.size() - 1);
        assertThat(testTemplate.getTemplateName()).isEqualTo(UPDATED_TEMPLATE_NAME);
        assertThat(testTemplate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTemplate.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, template.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(template))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(template))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(template)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemplateWithPatch() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        int databaseSizeBeforeUpdate = templateRepository.findAll().size();

        // Update the template using partial update
        Template partialUpdatedTemplate = new Template();
        partialUpdatedTemplate.setId(template.getId());

        partialUpdatedTemplate.createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);

        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplate))
            )
            .andExpect(status().isOk());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
        Template testTemplate = templateList.get(templateList.size() - 1);
        assertThat(testTemplate.getTemplateName()).isEqualTo(DEFAULT_TEMPLATE_NAME);
        assertThat(testTemplate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTemplate.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateTemplateWithPatch() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        int databaseSizeBeforeUpdate = templateRepository.findAll().size();

        // Update the template using partial update
        Template partialUpdatedTemplate = new Template();
        partialUpdatedTemplate.setId(template.getId());

        partialUpdatedTemplate.templateName(UPDATED_TEMPLATE_NAME).createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);

        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplate))
            )
            .andExpect(status().isOk());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
        Template testTemplate = templateList.get(templateList.size() - 1);
        assertThat(testTemplate.getTemplateName()).isEqualTo(UPDATED_TEMPLATE_NAME);
        assertThat(testTemplate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTemplate.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, template.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(template))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(template))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(template)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        int databaseSizeBeforeDelete = templateRepository.findAll().size();

        // Delete the template
        restTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, template.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
