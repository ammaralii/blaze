package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.Company;
import com.venturedive.blaze.domain.Template;
import com.venturedive.blaze.domain.TemplateField;
import com.venturedive.blaze.domain.TemplateFieldType;
import com.venturedive.blaze.domain.TestCaseField;
import com.venturedive.blaze.repository.TemplateFieldRepository;
import com.venturedive.blaze.service.TemplateFieldService;
import com.venturedive.blaze.service.criteria.TemplateFieldCriteria;
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
 * Integration tests for the {@link TemplateFieldResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TemplateFieldResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/template-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemplateFieldRepository templateFieldRepository;

    @Mock
    private TemplateFieldRepository templateFieldRepositoryMock;

    @Mock
    private TemplateFieldService templateFieldServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemplateFieldMockMvc;

    private TemplateField templateField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateField createEntity(EntityManager em) {
        TemplateField templateField = new TemplateField().fieldName(DEFAULT_FIELD_NAME);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        templateField.setCompany(company);
        // Add required entity
        TemplateFieldType templateFieldType;
        if (TestUtil.findAll(em, TemplateFieldType.class).isEmpty()) {
            templateFieldType = TemplateFieldTypeResourceIT.createEntity(em);
            em.persist(templateFieldType);
            em.flush();
        } else {
            templateFieldType = TestUtil.findAll(em, TemplateFieldType.class).get(0);
        }
        templateField.setTemplateFieldType(templateFieldType);
        return templateField;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateField createUpdatedEntity(EntityManager em) {
        TemplateField templateField = new TemplateField().fieldName(UPDATED_FIELD_NAME);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        templateField.setCompany(company);
        // Add required entity
        TemplateFieldType templateFieldType;
        if (TestUtil.findAll(em, TemplateFieldType.class).isEmpty()) {
            templateFieldType = TemplateFieldTypeResourceIT.createUpdatedEntity(em);
            em.persist(templateFieldType);
            em.flush();
        } else {
            templateFieldType = TestUtil.findAll(em, TemplateFieldType.class).get(0);
        }
        templateField.setTemplateFieldType(templateFieldType);
        return templateField;
    }

    @BeforeEach
    public void initTest() {
        templateField = createEntity(em);
    }

    @Test
    @Transactional
    void createTemplateField() throws Exception {
        int databaseSizeBeforeCreate = templateFieldRepository.findAll().size();
        // Create the TemplateField
        restTemplateFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateField)))
            .andExpect(status().isCreated());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeCreate + 1);
        TemplateField testTemplateField = templateFieldList.get(templateFieldList.size() - 1);
        assertThat(testTemplateField.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    void createTemplateFieldWithExistingId() throws Exception {
        // Create the TemplateField with an existing ID
        templateField.setId(1L);

        int databaseSizeBeforeCreate = templateFieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplateFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateField)))
            .andExpect(status().isBadRequest());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTemplateFields() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        // Get all the templateFieldList
        restTemplateFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateField.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplateFieldsWithEagerRelationshipsIsEnabled() throws Exception {
        when(templateFieldServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateFieldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(templateFieldServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplateFieldsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(templateFieldServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateFieldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(templateFieldRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTemplateField() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        // Get the templateField
        restTemplateFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, templateField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(templateField.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getTemplateFieldsByIdFiltering() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        Long id = templateField.getId();

        defaultTemplateFieldShouldBeFound("id.equals=" + id);
        defaultTemplateFieldShouldNotBeFound("id.notEquals=" + id);

        defaultTemplateFieldShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTemplateFieldShouldNotBeFound("id.greaterThan=" + id);

        defaultTemplateFieldShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTemplateFieldShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameIsEqualToSomething() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        // Get all the templateFieldList where fieldName equals to DEFAULT_FIELD_NAME
        defaultTemplateFieldShouldBeFound("fieldName.equals=" + DEFAULT_FIELD_NAME);

        // Get all the templateFieldList where fieldName equals to UPDATED_FIELD_NAME
        defaultTemplateFieldShouldNotBeFound("fieldName.equals=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameIsInShouldWork() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        // Get all the templateFieldList where fieldName in DEFAULT_FIELD_NAME or UPDATED_FIELD_NAME
        defaultTemplateFieldShouldBeFound("fieldName.in=" + DEFAULT_FIELD_NAME + "," + UPDATED_FIELD_NAME);

        // Get all the templateFieldList where fieldName equals to UPDATED_FIELD_NAME
        defaultTemplateFieldShouldNotBeFound("fieldName.in=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        // Get all the templateFieldList where fieldName is not null
        defaultTemplateFieldShouldBeFound("fieldName.specified=true");

        // Get all the templateFieldList where fieldName is null
        defaultTemplateFieldShouldNotBeFound("fieldName.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameContainsSomething() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        // Get all the templateFieldList where fieldName contains DEFAULT_FIELD_NAME
        defaultTemplateFieldShouldBeFound("fieldName.contains=" + DEFAULT_FIELD_NAME);

        // Get all the templateFieldList where fieldName contains UPDATED_FIELD_NAME
        defaultTemplateFieldShouldNotBeFound("fieldName.contains=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameNotContainsSomething() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        // Get all the templateFieldList where fieldName does not contain DEFAULT_FIELD_NAME
        defaultTemplateFieldShouldNotBeFound("fieldName.doesNotContain=" + DEFAULT_FIELD_NAME);

        // Get all the templateFieldList where fieldName does not contain UPDATED_FIELD_NAME
        defaultTemplateFieldShouldBeFound("fieldName.doesNotContain=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            templateFieldRepository.saveAndFlush(templateField);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        templateField.setCompany(company);
        templateFieldRepository.saveAndFlush(templateField);
        Long companyId = company.getId();

        // Get all the templateFieldList where company equals to companyId
        defaultTemplateFieldShouldBeFound("companyId.equals=" + companyId);

        // Get all the templateFieldList where company equals to (companyId + 1)
        defaultTemplateFieldShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByTemplateFieldTypeIsEqualToSomething() throws Exception {
        TemplateFieldType templateFieldType;
        if (TestUtil.findAll(em, TemplateFieldType.class).isEmpty()) {
            templateFieldRepository.saveAndFlush(templateField);
            templateFieldType = TemplateFieldTypeResourceIT.createEntity(em);
        } else {
            templateFieldType = TestUtil.findAll(em, TemplateFieldType.class).get(0);
        }
        em.persist(templateFieldType);
        em.flush();
        templateField.setTemplateFieldType(templateFieldType);
        templateFieldRepository.saveAndFlush(templateField);
        Long templateFieldTypeId = templateFieldType.getId();

        // Get all the templateFieldList where templateFieldType equals to templateFieldTypeId
        defaultTemplateFieldShouldBeFound("templateFieldTypeId.equals=" + templateFieldTypeId);

        // Get all the templateFieldList where templateFieldType equals to (templateFieldTypeId + 1)
        defaultTemplateFieldShouldNotBeFound("templateFieldTypeId.equals=" + (templateFieldTypeId + 1));
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByTestcasefieldTemplatefieldIsEqualToSomething() throws Exception {
        TestCaseField testcasefieldTemplatefield;
        if (TestUtil.findAll(em, TestCaseField.class).isEmpty()) {
            templateFieldRepository.saveAndFlush(templateField);
            testcasefieldTemplatefield = TestCaseFieldResourceIT.createEntity(em);
        } else {
            testcasefieldTemplatefield = TestUtil.findAll(em, TestCaseField.class).get(0);
        }
        em.persist(testcasefieldTemplatefield);
        em.flush();
        templateField.addTestcasefieldTemplatefield(testcasefieldTemplatefield);
        templateFieldRepository.saveAndFlush(templateField);
        Long testcasefieldTemplatefieldId = testcasefieldTemplatefield.getId();

        // Get all the templateFieldList where testcasefieldTemplatefield equals to testcasefieldTemplatefieldId
        defaultTemplateFieldShouldBeFound("testcasefieldTemplatefieldId.equals=" + testcasefieldTemplatefieldId);

        // Get all the templateFieldList where testcasefieldTemplatefield equals to (testcasefieldTemplatefieldId + 1)
        defaultTemplateFieldShouldNotBeFound("testcasefieldTemplatefieldId.equals=" + (testcasefieldTemplatefieldId + 1));
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByTemplateIsEqualToSomething() throws Exception {
        Template template;
        if (TestUtil.findAll(em, Template.class).isEmpty()) {
            templateFieldRepository.saveAndFlush(templateField);
            template = TemplateResourceIT.createEntity(em);
        } else {
            template = TestUtil.findAll(em, Template.class).get(0);
        }
        em.persist(template);
        em.flush();
        templateField.addTemplate(template);
        templateFieldRepository.saveAndFlush(templateField);
        Long templateId = template.getId();

        // Get all the templateFieldList where template equals to templateId
        defaultTemplateFieldShouldBeFound("templateId.equals=" + templateId);

        // Get all the templateFieldList where template equals to (templateId + 1)
        defaultTemplateFieldShouldNotBeFound("templateId.equals=" + (templateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTemplateFieldShouldBeFound(String filter) throws Exception {
        restTemplateFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateField.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));

        // Check, that the count call also returns 1
        restTemplateFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTemplateFieldShouldNotBeFound(String filter) throws Exception {
        restTemplateFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTemplateFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTemplateField() throws Exception {
        // Get the templateField
        restTemplateFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemplateField() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        int databaseSizeBeforeUpdate = templateFieldRepository.findAll().size();

        // Update the templateField
        TemplateField updatedTemplateField = templateFieldRepository.findById(templateField.getId()).get();
        // Disconnect from session so that the updates on updatedTemplateField are not directly saved in db
        em.detach(updatedTemplateField);
        updatedTemplateField.fieldName(UPDATED_FIELD_NAME);

        restTemplateFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemplateField.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTemplateField))
            )
            .andExpect(status().isOk());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeUpdate);
        TemplateField testTemplateField = templateFieldList.get(templateFieldList.size() - 1);
        assertThat(testTemplateField.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTemplateField() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldRepository.findAll().size();
        templateField.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, templateField.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateField))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemplateField() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldRepository.findAll().size();
        templateField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateField))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemplateField() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldRepository.findAll().size();
        templateField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateField)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemplateFieldWithPatch() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        int databaseSizeBeforeUpdate = templateFieldRepository.findAll().size();

        // Update the templateField using partial update
        TemplateField partialUpdatedTemplateField = new TemplateField();
        partialUpdatedTemplateField.setId(templateField.getId());

        partialUpdatedTemplateField.fieldName(UPDATED_FIELD_NAME);

        restTemplateFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateField))
            )
            .andExpect(status().isOk());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeUpdate);
        TemplateField testTemplateField = templateFieldList.get(templateFieldList.size() - 1);
        assertThat(testTemplateField.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTemplateFieldWithPatch() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        int databaseSizeBeforeUpdate = templateFieldRepository.findAll().size();

        // Update the templateField using partial update
        TemplateField partialUpdatedTemplateField = new TemplateField();
        partialUpdatedTemplateField.setId(templateField.getId());

        partialUpdatedTemplateField.fieldName(UPDATED_FIELD_NAME);

        restTemplateFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateField))
            )
            .andExpect(status().isOk());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeUpdate);
        TemplateField testTemplateField = templateFieldList.get(templateFieldList.size() - 1);
        assertThat(testTemplateField.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTemplateField() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldRepository.findAll().size();
        templateField.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, templateField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateField))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemplateField() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldRepository.findAll().size();
        templateField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateField))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemplateField() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldRepository.findAll().size();
        templateField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(templateField))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateField in the database
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemplateField() throws Exception {
        // Initialize the database
        templateFieldRepository.saveAndFlush(templateField);

        int databaseSizeBeforeDelete = templateFieldRepository.findAll().size();

        // Delete the templateField
        restTemplateFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, templateField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TemplateField> templateFieldList = templateFieldRepository.findAll();
        assertThat(templateFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
