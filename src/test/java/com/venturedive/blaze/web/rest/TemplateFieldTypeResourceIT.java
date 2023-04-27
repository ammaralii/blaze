package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TemplateField;
import com.venturedive.blaze.domain.TemplateFieldType;
import com.venturedive.blaze.repository.TemplateFieldTypeRepository;
import com.venturedive.blaze.service.criteria.TemplateFieldTypeCriteria;
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
 * Integration tests for the {@link TemplateFieldTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TemplateFieldTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LIST = false;
    private static final Boolean UPDATED_IS_LIST = true;

    private static final Boolean DEFAULT_ATTACHMENTS = false;
    private static final Boolean UPDATED_ATTACHMENTS = true;

    private static final String ENTITY_API_URL = "/api/template-field-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemplateFieldTypeRepository templateFieldTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemplateFieldTypeMockMvc;

    private TemplateFieldType templateFieldType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateFieldType createEntity(EntityManager em) {
        TemplateFieldType templateFieldType = new TemplateFieldType()
            .type(DEFAULT_TYPE)
            .isList(DEFAULT_IS_LIST)
            .attachments(DEFAULT_ATTACHMENTS);
        return templateFieldType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateFieldType createUpdatedEntity(EntityManager em) {
        TemplateFieldType templateFieldType = new TemplateFieldType()
            .type(UPDATED_TYPE)
            .isList(UPDATED_IS_LIST)
            .attachments(UPDATED_ATTACHMENTS);
        return templateFieldType;
    }

    @BeforeEach
    public void initTest() {
        templateFieldType = createEntity(em);
    }

    @Test
    @Transactional
    void createTemplateFieldType() throws Exception {
        int databaseSizeBeforeCreate = templateFieldTypeRepository.findAll().size();
        // Create the TemplateFieldType
        restTemplateFieldTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isCreated());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TemplateFieldType testTemplateFieldType = templateFieldTypeList.get(templateFieldTypeList.size() - 1);
        assertThat(testTemplateFieldType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTemplateFieldType.getIsList()).isEqualTo(DEFAULT_IS_LIST);
        assertThat(testTemplateFieldType.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
    }

    @Test
    @Transactional
    void createTemplateFieldTypeWithExistingId() throws Exception {
        // Create the TemplateFieldType with an existing ID
        templateFieldType.setId(1L);

        int databaseSizeBeforeCreate = templateFieldTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplateFieldTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = templateFieldTypeRepository.findAll().size();
        // set the field null
        templateFieldType.setType(null);

        // Create the TemplateFieldType, which fails.

        restTemplateFieldTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isBadRequest());

        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsListIsRequired() throws Exception {
        int databaseSizeBeforeTest = templateFieldTypeRepository.findAll().size();
        // set the field null
        templateFieldType.setIsList(null);

        // Create the TemplateFieldType, which fails.

        restTemplateFieldTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isBadRequest());

        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAttachmentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = templateFieldTypeRepository.findAll().size();
        // set the field null
        templateFieldType.setAttachments(null);

        // Create the TemplateFieldType, which fails.

        restTemplateFieldTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isBadRequest());

        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypes() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList
        restTemplateFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateFieldType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isList").value(hasItem(DEFAULT_IS_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS.booleanValue())));
    }

    @Test
    @Transactional
    void getTemplateFieldType() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get the templateFieldType
        restTemplateFieldTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, templateFieldType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(templateFieldType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.isList").value(DEFAULT_IS_LIST.booleanValue()))
            .andExpect(jsonPath("$.attachments").value(DEFAULT_ATTACHMENTS.booleanValue()));
    }

    @Test
    @Transactional
    void getTemplateFieldTypesByIdFiltering() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        Long id = templateFieldType.getId();

        defaultTemplateFieldTypeShouldBeFound("id.equals=" + id);
        defaultTemplateFieldTypeShouldNotBeFound("id.notEquals=" + id);

        defaultTemplateFieldTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTemplateFieldTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultTemplateFieldTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTemplateFieldTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where type equals to DEFAULT_TYPE
        defaultTemplateFieldTypeShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the templateFieldTypeList where type equals to UPDATED_TYPE
        defaultTemplateFieldTypeShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTemplateFieldTypeShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the templateFieldTypeList where type equals to UPDATED_TYPE
        defaultTemplateFieldTypeShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where type is not null
        defaultTemplateFieldTypeShouldBeFound("type.specified=true");

        // Get all the templateFieldTypeList where type is null
        defaultTemplateFieldTypeShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeContainsSomething() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where type contains DEFAULT_TYPE
        defaultTemplateFieldTypeShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the templateFieldTypeList where type contains UPDATED_TYPE
        defaultTemplateFieldTypeShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where type does not contain DEFAULT_TYPE
        defaultTemplateFieldTypeShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the templateFieldTypeList where type does not contain UPDATED_TYPE
        defaultTemplateFieldTypeShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByIsListIsEqualToSomething() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where isList equals to DEFAULT_IS_LIST
        defaultTemplateFieldTypeShouldBeFound("isList.equals=" + DEFAULT_IS_LIST);

        // Get all the templateFieldTypeList where isList equals to UPDATED_IS_LIST
        defaultTemplateFieldTypeShouldNotBeFound("isList.equals=" + UPDATED_IS_LIST);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByIsListIsInShouldWork() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where isList in DEFAULT_IS_LIST or UPDATED_IS_LIST
        defaultTemplateFieldTypeShouldBeFound("isList.in=" + DEFAULT_IS_LIST + "," + UPDATED_IS_LIST);

        // Get all the templateFieldTypeList where isList equals to UPDATED_IS_LIST
        defaultTemplateFieldTypeShouldNotBeFound("isList.in=" + UPDATED_IS_LIST);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByIsListIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where isList is not null
        defaultTemplateFieldTypeShouldBeFound("isList.specified=true");

        // Get all the templateFieldTypeList where isList is null
        defaultTemplateFieldTypeShouldNotBeFound("isList.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByAttachmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where attachments equals to DEFAULT_ATTACHMENTS
        defaultTemplateFieldTypeShouldBeFound("attachments.equals=" + DEFAULT_ATTACHMENTS);

        // Get all the templateFieldTypeList where attachments equals to UPDATED_ATTACHMENTS
        defaultTemplateFieldTypeShouldNotBeFound("attachments.equals=" + UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByAttachmentsIsInShouldWork() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where attachments in DEFAULT_ATTACHMENTS or UPDATED_ATTACHMENTS
        defaultTemplateFieldTypeShouldBeFound("attachments.in=" + DEFAULT_ATTACHMENTS + "," + UPDATED_ATTACHMENTS);

        // Get all the templateFieldTypeList where attachments equals to UPDATED_ATTACHMENTS
        defaultTemplateFieldTypeShouldNotBeFound("attachments.in=" + UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByAttachmentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        // Get all the templateFieldTypeList where attachments is not null
        defaultTemplateFieldTypeShouldBeFound("attachments.specified=true");

        // Get all the templateFieldTypeList where attachments is null
        defaultTemplateFieldTypeShouldNotBeFound("attachments.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTemplatefieldTemplatefieldtypeIsEqualToSomething() throws Exception {
        TemplateField templatefieldTemplatefieldtype;
        if (TestUtil.findAll(em, TemplateField.class).isEmpty()) {
            templateFieldTypeRepository.saveAndFlush(templateFieldType);
            templatefieldTemplatefieldtype = TemplateFieldResourceIT.createEntity(em);
        } else {
            templatefieldTemplatefieldtype = TestUtil.findAll(em, TemplateField.class).get(0);
        }
        em.persist(templatefieldTemplatefieldtype);
        em.flush();
        templateFieldType.addTemplatefieldTemplatefieldtype(templatefieldTemplatefieldtype);
        templateFieldTypeRepository.saveAndFlush(templateFieldType);
        Long templatefieldTemplatefieldtypeId = templatefieldTemplatefieldtype.getId();

        // Get all the templateFieldTypeList where templatefieldTemplatefieldtype equals to templatefieldTemplatefieldtypeId
        defaultTemplateFieldTypeShouldBeFound("templatefieldTemplatefieldtypeId.equals=" + templatefieldTemplatefieldtypeId);

        // Get all the templateFieldTypeList where templatefieldTemplatefieldtype equals to (templatefieldTemplatefieldtypeId + 1)
        defaultTemplateFieldTypeShouldNotBeFound("templatefieldTemplatefieldtypeId.equals=" + (templatefieldTemplatefieldtypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTemplateFieldTypeShouldBeFound(String filter) throws Exception {
        restTemplateFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateFieldType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isList").value(hasItem(DEFAULT_IS_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS.booleanValue())));

        // Check, that the count call also returns 1
        restTemplateFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTemplateFieldTypeShouldNotBeFound(String filter) throws Exception {
        restTemplateFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTemplateFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTemplateFieldType() throws Exception {
        // Get the templateFieldType
        restTemplateFieldTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemplateFieldType() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        int databaseSizeBeforeUpdate = templateFieldTypeRepository.findAll().size();

        // Update the templateFieldType
        TemplateFieldType updatedTemplateFieldType = templateFieldTypeRepository.findById(templateFieldType.getId()).get();
        // Disconnect from session so that the updates on updatedTemplateFieldType are not directly saved in db
        em.detach(updatedTemplateFieldType);
        updatedTemplateFieldType.type(UPDATED_TYPE).isList(UPDATED_IS_LIST).attachments(UPDATED_ATTACHMENTS);

        restTemplateFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemplateFieldType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTemplateFieldType))
            )
            .andExpect(status().isOk());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeUpdate);
        TemplateFieldType testTemplateFieldType = templateFieldTypeList.get(templateFieldTypeList.size() - 1);
        assertThat(testTemplateFieldType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTemplateFieldType.getIsList()).isEqualTo(UPDATED_IS_LIST);
        assertThat(testTemplateFieldType.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void putNonExistingTemplateFieldType() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypeRepository.findAll().size();
        templateFieldType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, templateFieldType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemplateFieldType() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypeRepository.findAll().size();
        templateFieldType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemplateFieldType() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypeRepository.findAll().size();
        templateFieldType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemplateFieldTypeWithPatch() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        int databaseSizeBeforeUpdate = templateFieldTypeRepository.findAll().size();

        // Update the templateFieldType using partial update
        TemplateFieldType partialUpdatedTemplateFieldType = new TemplateFieldType();
        partialUpdatedTemplateFieldType.setId(templateFieldType.getId());

        partialUpdatedTemplateFieldType.type(UPDATED_TYPE).attachments(UPDATED_ATTACHMENTS);

        restTemplateFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateFieldType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateFieldType))
            )
            .andExpect(status().isOk());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeUpdate);
        TemplateFieldType testTemplateFieldType = templateFieldTypeList.get(templateFieldTypeList.size() - 1);
        assertThat(testTemplateFieldType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTemplateFieldType.getIsList()).isEqualTo(DEFAULT_IS_LIST);
        assertThat(testTemplateFieldType.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void fullUpdateTemplateFieldTypeWithPatch() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        int databaseSizeBeforeUpdate = templateFieldTypeRepository.findAll().size();

        // Update the templateFieldType using partial update
        TemplateFieldType partialUpdatedTemplateFieldType = new TemplateFieldType();
        partialUpdatedTemplateFieldType.setId(templateFieldType.getId());

        partialUpdatedTemplateFieldType.type(UPDATED_TYPE).isList(UPDATED_IS_LIST).attachments(UPDATED_ATTACHMENTS);

        restTemplateFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateFieldType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateFieldType))
            )
            .andExpect(status().isOk());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeUpdate);
        TemplateFieldType testTemplateFieldType = templateFieldTypeList.get(templateFieldTypeList.size() - 1);
        assertThat(testTemplateFieldType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTemplateFieldType.getIsList()).isEqualTo(UPDATED_IS_LIST);
        assertThat(testTemplateFieldType.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingTemplateFieldType() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypeRepository.findAll().size();
        templateFieldType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, templateFieldType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemplateFieldType() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypeRepository.findAll().size();
        templateFieldType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemplateFieldType() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypeRepository.findAll().size();
        templateFieldType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateFieldType in the database
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemplateFieldType() throws Exception {
        // Initialize the database
        templateFieldTypeRepository.saveAndFlush(templateFieldType);

        int databaseSizeBeforeDelete = templateFieldTypeRepository.findAll().size();

        // Delete the templateFieldType
        restTemplateFieldTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, templateFieldType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TemplateFieldType> templateFieldTypeList = templateFieldTypeRepository.findAll();
        assertThat(templateFieldTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
