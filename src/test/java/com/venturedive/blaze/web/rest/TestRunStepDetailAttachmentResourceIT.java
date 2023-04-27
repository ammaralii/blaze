package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TestRunStepDetailAttachment;
import com.venturedive.blaze.domain.TestRunStepDetails;
import com.venturedive.blaze.repository.TestRunStepDetailAttachmentRepository;
import com.venturedive.blaze.service.criteria.TestRunStepDetailAttachmentCriteria;
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
 * Integration tests for the {@link TestRunStepDetailAttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestRunStepDetailAttachmentResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-run-step-detail-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestRunStepDetailAttachmentRepository testRunStepDetailAttachmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestRunStepDetailAttachmentMockMvc;

    private TestRunStepDetailAttachment testRunStepDetailAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunStepDetailAttachment createEntity(EntityManager em) {
        TestRunStepDetailAttachment testRunStepDetailAttachment = new TestRunStepDetailAttachment().url(DEFAULT_URL);
        // Add required entity
        TestRunStepDetails testRunStepDetails;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testRunStepDetails = TestRunStepDetailsResourceIT.createEntity(em);
            em.persist(testRunStepDetails);
            em.flush();
        } else {
            testRunStepDetails = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        testRunStepDetailAttachment.setTestRunStepDetail(testRunStepDetails);
        return testRunStepDetailAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunStepDetailAttachment createUpdatedEntity(EntityManager em) {
        TestRunStepDetailAttachment testRunStepDetailAttachment = new TestRunStepDetailAttachment().url(UPDATED_URL);
        // Add required entity
        TestRunStepDetails testRunStepDetails;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testRunStepDetails = TestRunStepDetailsResourceIT.createUpdatedEntity(em);
            em.persist(testRunStepDetails);
            em.flush();
        } else {
            testRunStepDetails = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        testRunStepDetailAttachment.setTestRunStepDetail(testRunStepDetails);
        return testRunStepDetailAttachment;
    }

    @BeforeEach
    public void initTest() {
        testRunStepDetailAttachment = createEntity(em);
    }

    @Test
    @Transactional
    void createTestRunStepDetailAttachment() throws Exception {
        int databaseSizeBeforeCreate = testRunStepDetailAttachmentRepository.findAll().size();
        // Create the TestRunStepDetailAttachment
        restTestRunStepDetailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachment))
            )
            .andExpect(status().isCreated());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeCreate + 1);
        TestRunStepDetailAttachment testTestRunStepDetailAttachment = testRunStepDetailAttachmentList.get(
            testRunStepDetailAttachmentList.size() - 1
        );
        assertThat(testTestRunStepDetailAttachment.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createTestRunStepDetailAttachmentWithExistingId() throws Exception {
        // Create the TestRunStepDetailAttachment with an existing ID
        testRunStepDetailAttachment.setId(1L);

        int databaseSizeBeforeCreate = testRunStepDetailAttachmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestRunStepDetailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = testRunStepDetailAttachmentRepository.findAll().size();
        // set the field null
        testRunStepDetailAttachment.setUrl(null);

        // Create the TestRunStepDetailAttachment, which fails.

        restTestRunStepDetailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailAttachments() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        // Get all the testRunStepDetailAttachmentList
        restTestRunStepDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunStepDetailAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getTestRunStepDetailAttachment() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        // Get the testRunStepDetailAttachment
        restTestRunStepDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, testRunStepDetailAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testRunStepDetailAttachment.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getTestRunStepDetailAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        Long id = testRunStepDetailAttachment.getId();

        defaultTestRunStepDetailAttachmentShouldBeFound("id.equals=" + id);
        defaultTestRunStepDetailAttachmentShouldNotBeFound("id.notEquals=" + id);

        defaultTestRunStepDetailAttachmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestRunStepDetailAttachmentShouldNotBeFound("id.greaterThan=" + id);

        defaultTestRunStepDetailAttachmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestRunStepDetailAttachmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailAttachmentsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        // Get all the testRunStepDetailAttachmentList where url equals to DEFAULT_URL
        defaultTestRunStepDetailAttachmentShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the testRunStepDetailAttachmentList where url equals to UPDATED_URL
        defaultTestRunStepDetailAttachmentShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailAttachmentsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        // Get all the testRunStepDetailAttachmentList where url in DEFAULT_URL or UPDATED_URL
        defaultTestRunStepDetailAttachmentShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the testRunStepDetailAttachmentList where url equals to UPDATED_URL
        defaultTestRunStepDetailAttachmentShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailAttachmentsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        // Get all the testRunStepDetailAttachmentList where url is not null
        defaultTestRunStepDetailAttachmentShouldBeFound("url.specified=true");

        // Get all the testRunStepDetailAttachmentList where url is null
        defaultTestRunStepDetailAttachmentShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailAttachmentsByUrlContainsSomething() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        // Get all the testRunStepDetailAttachmentList where url contains DEFAULT_URL
        defaultTestRunStepDetailAttachmentShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the testRunStepDetailAttachmentList where url contains UPDATED_URL
        defaultTestRunStepDetailAttachmentShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailAttachmentsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        // Get all the testRunStepDetailAttachmentList where url does not contain DEFAULT_URL
        defaultTestRunStepDetailAttachmentShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the testRunStepDetailAttachmentList where url does not contain UPDATED_URL
        defaultTestRunStepDetailAttachmentShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailAttachmentsByTestRunStepDetailIsEqualToSomething() throws Exception {
        TestRunStepDetails testRunStepDetail;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);
            testRunStepDetail = TestRunStepDetailsResourceIT.createEntity(em);
        } else {
            testRunStepDetail = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        em.persist(testRunStepDetail);
        em.flush();
        testRunStepDetailAttachment.setTestRunStepDetail(testRunStepDetail);
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);
        Long testRunStepDetailId = testRunStepDetail.getId();

        // Get all the testRunStepDetailAttachmentList where testRunStepDetail equals to testRunStepDetailId
        defaultTestRunStepDetailAttachmentShouldBeFound("testRunStepDetailId.equals=" + testRunStepDetailId);

        // Get all the testRunStepDetailAttachmentList where testRunStepDetail equals to (testRunStepDetailId + 1)
        defaultTestRunStepDetailAttachmentShouldNotBeFound("testRunStepDetailId.equals=" + (testRunStepDetailId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestRunStepDetailAttachmentShouldBeFound(String filter) throws Exception {
        restTestRunStepDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunStepDetailAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restTestRunStepDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestRunStepDetailAttachmentShouldNotBeFound(String filter) throws Exception {
        restTestRunStepDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestRunStepDetailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestRunStepDetailAttachment() throws Exception {
        // Get the testRunStepDetailAttachment
        restTestRunStepDetailAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestRunStepDetailAttachment() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentRepository.findAll().size();

        // Update the testRunStepDetailAttachment
        TestRunStepDetailAttachment updatedTestRunStepDetailAttachment = testRunStepDetailAttachmentRepository
            .findById(testRunStepDetailAttachment.getId())
            .get();
        // Disconnect from session so that the updates on updatedTestRunStepDetailAttachment are not directly saved in db
        em.detach(updatedTestRunStepDetailAttachment);
        updatedTestRunStepDetailAttachment.url(UPDATED_URL);

        restTestRunStepDetailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestRunStepDetailAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestRunStepDetailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestRunStepDetailAttachment testTestRunStepDetailAttachment = testRunStepDetailAttachmentList.get(
            testRunStepDetailAttachmentList.size() - 1
        );
        assertThat(testTestRunStepDetailAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingTestRunStepDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentRepository.findAll().size();
        testRunStepDetailAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testRunStepDetailAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestRunStepDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentRepository.findAll().size();
        testRunStepDetailAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestRunStepDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentRepository.findAll().size();
        testRunStepDetailAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestRunStepDetailAttachmentWithPatch() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentRepository.findAll().size();

        // Update the testRunStepDetailAttachment using partial update
        TestRunStepDetailAttachment partialUpdatedTestRunStepDetailAttachment = new TestRunStepDetailAttachment();
        partialUpdatedTestRunStepDetailAttachment.setId(testRunStepDetailAttachment.getId());

        restTestRunStepDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunStepDetailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunStepDetailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestRunStepDetailAttachment testTestRunStepDetailAttachment = testRunStepDetailAttachmentList.get(
            testRunStepDetailAttachmentList.size() - 1
        );
        assertThat(testTestRunStepDetailAttachment.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void fullUpdateTestRunStepDetailAttachmentWithPatch() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentRepository.findAll().size();

        // Update the testRunStepDetailAttachment using partial update
        TestRunStepDetailAttachment partialUpdatedTestRunStepDetailAttachment = new TestRunStepDetailAttachment();
        partialUpdatedTestRunStepDetailAttachment.setId(testRunStepDetailAttachment.getId());

        partialUpdatedTestRunStepDetailAttachment.url(UPDATED_URL);

        restTestRunStepDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunStepDetailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunStepDetailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestRunStepDetailAttachment testTestRunStepDetailAttachment = testRunStepDetailAttachmentList.get(
            testRunStepDetailAttachmentList.size() - 1
        );
        assertThat(testTestRunStepDetailAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingTestRunStepDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentRepository.findAll().size();
        testRunStepDetailAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testRunStepDetailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestRunStepDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentRepository.findAll().size();
        testRunStepDetailAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestRunStepDetailAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentRepository.findAll().size();
        testRunStepDetailAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunStepDetailAttachment in the database
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestRunStepDetailAttachment() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentRepository.saveAndFlush(testRunStepDetailAttachment);

        int databaseSizeBeforeDelete = testRunStepDetailAttachmentRepository.findAll().size();

        // Delete the testRunStepDetailAttachment
        restTestRunStepDetailAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, testRunStepDetailAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestRunStepDetailAttachment> testRunStepDetailAttachmentList = testRunStepDetailAttachmentRepository.findAll();
        assertThat(testRunStepDetailAttachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
