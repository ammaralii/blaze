package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TestRunDetailAttachment;
import com.venturedive.blaze.domain.TestRunDetails;
import com.venturedive.blaze.repository.TestRunDetailAttachmentRepository;
import com.venturedive.blaze.service.criteria.TestRunDetailAttachmentCriteria;
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
 * Integration tests for the {@link TestRunDetailAttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestRunDetailAttachmentResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-run-detail-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestRunDetailAttachmentRepository testRunDetailAttachmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestRunDetailAttachmentMockMvc;

    private TestRunDetailAttachment testRunDetailAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunDetailAttachment createEntity(EntityManager em) {
        TestRunDetailAttachment testRunDetailAttachment = new TestRunDetailAttachment().url(DEFAULT_URL);
        // Add required entity
        TestRunDetails testRunDetails;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testRunDetails = TestRunDetailsResourceIT.createEntity(em);
            em.persist(testRunDetails);
            em.flush();
        } else {
            testRunDetails = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        testRunDetailAttachment.setTestRunDetail(testRunDetails);
        return testRunDetailAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunDetailAttachment createUpdatedEntity(EntityManager em) {
        TestRunDetailAttachment testRunDetailAttachment = new TestRunDetailAttachment().url(UPDATED_URL);
        // Add required entity
        TestRunDetails testRunDetails;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testRunDetails = TestRunDetailsResourceIT.createUpdatedEntity(em);
            em.persist(testRunDetails);
            em.flush();
        } else {
            testRunDetails = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        testRunDetailAttachment.setTestRunDetail(testRunDetails);
        return testRunDetailAttachment;
    }

    @BeforeEach
    public void initTest() {
        testRunDetailAttachment = createEntity(em);
    }

    @Test
    @Transactional
    void createTestRunDetailAttachment() throws Exception {
        int databaseSizeBeforeCreate = testRunDetailAttachmentRepository.findAll().size();
        // Create the TestRunDetailAttachment
        restTestRunDetailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachment))
            )
            .andExpect(status().isCreated());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeCreate + 1);
        TestRunDetailAttachment testTestRunDetailAttachment = testRunDetailAttachmentList.get(testRunDetailAttachmentList.size() - 1);
        assertThat(testTestRunDetailAttachment.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createTestRunDetailAttachmentWithExistingId() throws Exception {
        // Create the TestRunDetailAttachment with an existing ID
        testRunDetailAttachment.setId(1L);

        int databaseSizeBeforeCreate = testRunDetailAttachmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestRunDetailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = testRunDetailAttachmentRepository.findAll().size();
        // set the field null
        testRunDetailAttachment.setUrl(null);

        // Create the TestRunDetailAttachment, which fails.

        restTestRunDetailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestRunDetailAttachments() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        // Get all the testRunDetailAttachmentList
        restTestRunDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunDetailAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getTestRunDetailAttachment() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        // Get the testRunDetailAttachment
        restTestRunDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, testRunDetailAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testRunDetailAttachment.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getTestRunDetailAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        Long id = testRunDetailAttachment.getId();

        defaultTestRunDetailAttachmentShouldBeFound("id.equals=" + id);
        defaultTestRunDetailAttachmentShouldNotBeFound("id.notEquals=" + id);

        defaultTestRunDetailAttachmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestRunDetailAttachmentShouldNotBeFound("id.greaterThan=" + id);

        defaultTestRunDetailAttachmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestRunDetailAttachmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestRunDetailAttachmentsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        // Get all the testRunDetailAttachmentList where url equals to DEFAULT_URL
        defaultTestRunDetailAttachmentShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the testRunDetailAttachmentList where url equals to UPDATED_URL
        defaultTestRunDetailAttachmentShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestRunDetailAttachmentsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        // Get all the testRunDetailAttachmentList where url in DEFAULT_URL or UPDATED_URL
        defaultTestRunDetailAttachmentShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the testRunDetailAttachmentList where url equals to UPDATED_URL
        defaultTestRunDetailAttachmentShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestRunDetailAttachmentsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        // Get all the testRunDetailAttachmentList where url is not null
        defaultTestRunDetailAttachmentShouldBeFound("url.specified=true");

        // Get all the testRunDetailAttachmentList where url is null
        defaultTestRunDetailAttachmentShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunDetailAttachmentsByUrlContainsSomething() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        // Get all the testRunDetailAttachmentList where url contains DEFAULT_URL
        defaultTestRunDetailAttachmentShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the testRunDetailAttachmentList where url contains UPDATED_URL
        defaultTestRunDetailAttachmentShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestRunDetailAttachmentsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        // Get all the testRunDetailAttachmentList where url does not contain DEFAULT_URL
        defaultTestRunDetailAttachmentShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the testRunDetailAttachmentList where url does not contain UPDATED_URL
        defaultTestRunDetailAttachmentShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestRunDetailAttachmentsByTestRunDetailIsEqualToSomething() throws Exception {
        TestRunDetails testRunDetail;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);
            testRunDetail = TestRunDetailsResourceIT.createEntity(em);
        } else {
            testRunDetail = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        em.persist(testRunDetail);
        em.flush();
        testRunDetailAttachment.setTestRunDetail(testRunDetail);
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);
        Long testRunDetailId = testRunDetail.getId();

        // Get all the testRunDetailAttachmentList where testRunDetail equals to testRunDetailId
        defaultTestRunDetailAttachmentShouldBeFound("testRunDetailId.equals=" + testRunDetailId);

        // Get all the testRunDetailAttachmentList where testRunDetail equals to (testRunDetailId + 1)
        defaultTestRunDetailAttachmentShouldNotBeFound("testRunDetailId.equals=" + (testRunDetailId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestRunDetailAttachmentShouldBeFound(String filter) throws Exception {
        restTestRunDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunDetailAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restTestRunDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestRunDetailAttachmentShouldNotBeFound(String filter) throws Exception {
        restTestRunDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestRunDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestRunDetailAttachment() throws Exception {
        // Get the testRunDetailAttachment
        restTestRunDetailAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestRunDetailAttachment() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        int databaseSizeBeforeUpdate = testRunDetailAttachmentRepository.findAll().size();

        // Update the testRunDetailAttachment
        TestRunDetailAttachment updatedTestRunDetailAttachment = testRunDetailAttachmentRepository
            .findById(testRunDetailAttachment.getId())
            .get();
        // Disconnect from session so that the updates on updatedTestRunDetailAttachment are not directly saved in db
        em.detach(updatedTestRunDetailAttachment);
        updatedTestRunDetailAttachment.url(UPDATED_URL);

        restTestRunDetailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestRunDetailAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestRunDetailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestRunDetailAttachment testTestRunDetailAttachment = testRunDetailAttachmentList.get(testRunDetailAttachmentList.size() - 1);
        assertThat(testTestRunDetailAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingTestRunDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentRepository.findAll().size();
        testRunDetailAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testRunDetailAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestRunDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentRepository.findAll().size();
        testRunDetailAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestRunDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentRepository.findAll().size();
        testRunDetailAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestRunDetailAttachmentWithPatch() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        int databaseSizeBeforeUpdate = testRunDetailAttachmentRepository.findAll().size();

        // Update the testRunDetailAttachment using partial update
        TestRunDetailAttachment partialUpdatedTestRunDetailAttachment = new TestRunDetailAttachment();
        partialUpdatedTestRunDetailAttachment.setId(testRunDetailAttachment.getId());

        partialUpdatedTestRunDetailAttachment.url(UPDATED_URL);

        restTestRunDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunDetailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunDetailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestRunDetailAttachment testTestRunDetailAttachment = testRunDetailAttachmentList.get(testRunDetailAttachmentList.size() - 1);
        assertThat(testTestRunDetailAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void fullUpdateTestRunDetailAttachmentWithPatch() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        int databaseSizeBeforeUpdate = testRunDetailAttachmentRepository.findAll().size();

        // Update the testRunDetailAttachment using partial update
        TestRunDetailAttachment partialUpdatedTestRunDetailAttachment = new TestRunDetailAttachment();
        partialUpdatedTestRunDetailAttachment.setId(testRunDetailAttachment.getId());

        partialUpdatedTestRunDetailAttachment.url(UPDATED_URL);

        restTestRunDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunDetailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunDetailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestRunDetailAttachment testTestRunDetailAttachment = testRunDetailAttachmentList.get(testRunDetailAttachmentList.size() - 1);
        assertThat(testTestRunDetailAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingTestRunDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentRepository.findAll().size();
        testRunDetailAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testRunDetailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestRunDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentRepository.findAll().size();
        testRunDetailAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestRunDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentRepository.findAll().size();
        testRunDetailAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunDetailAttachment in the database
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestRunDetailAttachment() throws Exception {
        // Initialize the database
        testRunDetailAttachmentRepository.saveAndFlush(testRunDetailAttachment);

        int databaseSizeBeforeDelete = testRunDetailAttachmentRepository.findAll().size();

        // Delete the testRunDetailAttachment
        restTestRunDetailAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, testRunDetailAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestRunDetailAttachment> testRunDetailAttachmentList = testRunDetailAttachmentRepository.findAll();
        assertThat(testRunDetailAttachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
