package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TestCaseField;
import com.venturedive.blaze.domain.TestCaseFieldAttachment;
import com.venturedive.blaze.repository.TestCaseFieldAttachmentRepository;
import com.venturedive.blaze.service.criteria.TestCaseFieldAttachmentCriteria;
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
 * Integration tests for the {@link TestCaseFieldAttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCaseFieldAttachmentResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-case-field-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCaseFieldAttachmentRepository testCaseFieldAttachmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseFieldAttachmentMockMvc;

    private TestCaseFieldAttachment testCaseFieldAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseFieldAttachment createEntity(EntityManager em) {
        TestCaseFieldAttachment testCaseFieldAttachment = new TestCaseFieldAttachment().url(DEFAULT_URL);
        // Add required entity
        TestCaseField testCaseField;
        if (TestUtil.findAll(em, TestCaseField.class).isEmpty()) {
            testCaseField = TestCaseFieldResourceIT.createEntity(em);
            em.persist(testCaseField);
            em.flush();
        } else {
            testCaseField = TestUtil.findAll(em, TestCaseField.class).get(0);
        }
        testCaseFieldAttachment.setTestCaseField(testCaseField);
        return testCaseFieldAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseFieldAttachment createUpdatedEntity(EntityManager em) {
        TestCaseFieldAttachment testCaseFieldAttachment = new TestCaseFieldAttachment().url(UPDATED_URL);
        // Add required entity
        TestCaseField testCaseField;
        if (TestUtil.findAll(em, TestCaseField.class).isEmpty()) {
            testCaseField = TestCaseFieldResourceIT.createUpdatedEntity(em);
            em.persist(testCaseField);
            em.flush();
        } else {
            testCaseField = TestUtil.findAll(em, TestCaseField.class).get(0);
        }
        testCaseFieldAttachment.setTestCaseField(testCaseField);
        return testCaseFieldAttachment;
    }

    @BeforeEach
    public void initTest() {
        testCaseFieldAttachment = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCaseFieldAttachment() throws Exception {
        int databaseSizeBeforeCreate = testCaseFieldAttachmentRepository.findAll().size();
        // Create the TestCaseFieldAttachment
        restTestCaseFieldAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachment))
            )
            .andExpect(status().isCreated());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeCreate + 1);
        TestCaseFieldAttachment testTestCaseFieldAttachment = testCaseFieldAttachmentList.get(testCaseFieldAttachmentList.size() - 1);
        assertThat(testTestCaseFieldAttachment.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createTestCaseFieldAttachmentWithExistingId() throws Exception {
        // Create the TestCaseFieldAttachment with an existing ID
        testCaseFieldAttachment.setId(1L);

        int databaseSizeBeforeCreate = testCaseFieldAttachmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseFieldAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseFieldAttachmentRepository.findAll().size();
        // set the field null
        testCaseFieldAttachment.setUrl(null);

        // Create the TestCaseFieldAttachment, which fails.

        restTestCaseFieldAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachment))
            )
            .andExpect(status().isBadRequest());

        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldAttachments() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        // Get all the testCaseFieldAttachmentList
        restTestCaseFieldAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseFieldAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getTestCaseFieldAttachment() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        // Get the testCaseFieldAttachment
        restTestCaseFieldAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, testCaseFieldAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCaseFieldAttachment.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getTestCaseFieldAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        Long id = testCaseFieldAttachment.getId();

        defaultTestCaseFieldAttachmentShouldBeFound("id.equals=" + id);
        defaultTestCaseFieldAttachmentShouldNotBeFound("id.notEquals=" + id);

        defaultTestCaseFieldAttachmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCaseFieldAttachmentShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCaseFieldAttachmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCaseFieldAttachmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldAttachmentsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        // Get all the testCaseFieldAttachmentList where url equals to DEFAULT_URL
        defaultTestCaseFieldAttachmentShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the testCaseFieldAttachmentList where url equals to UPDATED_URL
        defaultTestCaseFieldAttachmentShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldAttachmentsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        // Get all the testCaseFieldAttachmentList where url in DEFAULT_URL or UPDATED_URL
        defaultTestCaseFieldAttachmentShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the testCaseFieldAttachmentList where url equals to UPDATED_URL
        defaultTestCaseFieldAttachmentShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldAttachmentsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        // Get all the testCaseFieldAttachmentList where url is not null
        defaultTestCaseFieldAttachmentShouldBeFound("url.specified=true");

        // Get all the testCaseFieldAttachmentList where url is null
        defaultTestCaseFieldAttachmentShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCaseFieldAttachmentsByUrlContainsSomething() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        // Get all the testCaseFieldAttachmentList where url contains DEFAULT_URL
        defaultTestCaseFieldAttachmentShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the testCaseFieldAttachmentList where url contains UPDATED_URL
        defaultTestCaseFieldAttachmentShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldAttachmentsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        // Get all the testCaseFieldAttachmentList where url does not contain DEFAULT_URL
        defaultTestCaseFieldAttachmentShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the testCaseFieldAttachmentList where url does not contain UPDATED_URL
        defaultTestCaseFieldAttachmentShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldAttachmentsByTestCaseFieldIsEqualToSomething() throws Exception {
        TestCaseField testCaseField;
        if (TestUtil.findAll(em, TestCaseField.class).isEmpty()) {
            testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);
            testCaseField = TestCaseFieldResourceIT.createEntity(em);
        } else {
            testCaseField = TestUtil.findAll(em, TestCaseField.class).get(0);
        }
        em.persist(testCaseField);
        em.flush();
        testCaseFieldAttachment.setTestCaseField(testCaseField);
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);
        Long testCaseFieldId = testCaseField.getId();

        // Get all the testCaseFieldAttachmentList where testCaseField equals to testCaseFieldId
        defaultTestCaseFieldAttachmentShouldBeFound("testCaseFieldId.equals=" + testCaseFieldId);

        // Get all the testCaseFieldAttachmentList where testCaseField equals to (testCaseFieldId + 1)
        defaultTestCaseFieldAttachmentShouldNotBeFound("testCaseFieldId.equals=" + (testCaseFieldId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCaseFieldAttachmentShouldBeFound(String filter) throws Exception {
        restTestCaseFieldAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseFieldAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restTestCaseFieldAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCaseFieldAttachmentShouldNotBeFound(String filter) throws Exception {
        restTestCaseFieldAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCaseFieldAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCaseFieldAttachment() throws Exception {
        // Get the testCaseFieldAttachment
        restTestCaseFieldAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCaseFieldAttachment() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        int databaseSizeBeforeUpdate = testCaseFieldAttachmentRepository.findAll().size();

        // Update the testCaseFieldAttachment
        TestCaseFieldAttachment updatedTestCaseFieldAttachment = testCaseFieldAttachmentRepository
            .findById(testCaseFieldAttachment.getId())
            .get();
        // Disconnect from session so that the updates on updatedTestCaseFieldAttachment are not directly saved in db
        em.detach(updatedTestCaseFieldAttachment);
        updatedTestCaseFieldAttachment.url(UPDATED_URL);

        restTestCaseFieldAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCaseFieldAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCaseFieldAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestCaseFieldAttachment testTestCaseFieldAttachment = testCaseFieldAttachmentList.get(testCaseFieldAttachmentList.size() - 1);
        assertThat(testTestCaseFieldAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingTestCaseFieldAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentRepository.findAll().size();
        testCaseFieldAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseFieldAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCaseFieldAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentRepository.findAll().size();
        testCaseFieldAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCaseFieldAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentRepository.findAll().size();
        testCaseFieldAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseFieldAttachmentWithPatch() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        int databaseSizeBeforeUpdate = testCaseFieldAttachmentRepository.findAll().size();

        // Update the testCaseFieldAttachment using partial update
        TestCaseFieldAttachment partialUpdatedTestCaseFieldAttachment = new TestCaseFieldAttachment();
        partialUpdatedTestCaseFieldAttachment.setId(testCaseFieldAttachment.getId());

        partialUpdatedTestCaseFieldAttachment.url(UPDATED_URL);

        restTestCaseFieldAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseFieldAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseFieldAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestCaseFieldAttachment testTestCaseFieldAttachment = testCaseFieldAttachmentList.get(testCaseFieldAttachmentList.size() - 1);
        assertThat(testTestCaseFieldAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void fullUpdateTestCaseFieldAttachmentWithPatch() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        int databaseSizeBeforeUpdate = testCaseFieldAttachmentRepository.findAll().size();

        // Update the testCaseFieldAttachment using partial update
        TestCaseFieldAttachment partialUpdatedTestCaseFieldAttachment = new TestCaseFieldAttachment();
        partialUpdatedTestCaseFieldAttachment.setId(testCaseFieldAttachment.getId());

        partialUpdatedTestCaseFieldAttachment.url(UPDATED_URL);

        restTestCaseFieldAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseFieldAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseFieldAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestCaseFieldAttachment testTestCaseFieldAttachment = testCaseFieldAttachmentList.get(testCaseFieldAttachmentList.size() - 1);
        assertThat(testTestCaseFieldAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingTestCaseFieldAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentRepository.findAll().size();
        testCaseFieldAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCaseFieldAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCaseFieldAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentRepository.findAll().size();
        testCaseFieldAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCaseFieldAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentRepository.findAll().size();
        testCaseFieldAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseFieldAttachment in the database
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCaseFieldAttachment() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentRepository.saveAndFlush(testCaseFieldAttachment);

        int databaseSizeBeforeDelete = testCaseFieldAttachmentRepository.findAll().size();

        // Delete the testCaseFieldAttachment
        restTestCaseFieldAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCaseFieldAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCaseFieldAttachment> testCaseFieldAttachmentList = testCaseFieldAttachmentRepository.findAll();
        assertThat(testCaseFieldAttachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
