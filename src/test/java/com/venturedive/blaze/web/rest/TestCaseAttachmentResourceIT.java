package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.domain.TestCaseAttachment;
import com.venturedive.blaze.repository.TestCaseAttachmentRepository;
import com.venturedive.blaze.service.criteria.TestCaseAttachmentCriteria;
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
 * Integration tests for the {@link TestCaseAttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCaseAttachmentResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-case-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCaseAttachmentRepository testCaseAttachmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseAttachmentMockMvc;

    private TestCaseAttachment testCaseAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseAttachment createEntity(EntityManager em) {
        TestCaseAttachment testCaseAttachment = new TestCaseAttachment().url(DEFAULT_URL);
        // Add required entity
        TestCase testCase;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testCase = TestCaseResourceIT.createEntity(em);
            em.persist(testCase);
            em.flush();
        } else {
            testCase = TestUtil.findAll(em, TestCase.class).get(0);
        }
        testCaseAttachment.setTestCase(testCase);
        return testCaseAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseAttachment createUpdatedEntity(EntityManager em) {
        TestCaseAttachment testCaseAttachment = new TestCaseAttachment().url(UPDATED_URL);
        // Add required entity
        TestCase testCase;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testCase = TestCaseResourceIT.createUpdatedEntity(em);
            em.persist(testCase);
            em.flush();
        } else {
            testCase = TestUtil.findAll(em, TestCase.class).get(0);
        }
        testCaseAttachment.setTestCase(testCase);
        return testCaseAttachment;
    }

    @BeforeEach
    public void initTest() {
        testCaseAttachment = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCaseAttachment() throws Exception {
        int databaseSizeBeforeCreate = testCaseAttachmentRepository.findAll().size();
        // Create the TestCaseAttachment
        restTestCaseAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseAttachment))
            )
            .andExpect(status().isCreated());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeCreate + 1);
        TestCaseAttachment testTestCaseAttachment = testCaseAttachmentList.get(testCaseAttachmentList.size() - 1);
        assertThat(testTestCaseAttachment.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createTestCaseAttachmentWithExistingId() throws Exception {
        // Create the TestCaseAttachment with an existing ID
        testCaseAttachment.setId(1L);

        int databaseSizeBeforeCreate = testCaseAttachmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseAttachmentRepository.findAll().size();
        // set the field null
        testCaseAttachment.setUrl(null);

        // Create the TestCaseAttachment, which fails.

        restTestCaseAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseAttachment))
            )
            .andExpect(status().isBadRequest());

        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestCaseAttachments() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        // Get all the testCaseAttachmentList
        restTestCaseAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getTestCaseAttachment() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        // Get the testCaseAttachment
        restTestCaseAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, testCaseAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCaseAttachment.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getTestCaseAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        Long id = testCaseAttachment.getId();

        defaultTestCaseAttachmentShouldBeFound("id.equals=" + id);
        defaultTestCaseAttachmentShouldNotBeFound("id.notEquals=" + id);

        defaultTestCaseAttachmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCaseAttachmentShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCaseAttachmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCaseAttachmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCaseAttachmentsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        // Get all the testCaseAttachmentList where url equals to DEFAULT_URL
        defaultTestCaseAttachmentShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the testCaseAttachmentList where url equals to UPDATED_URL
        defaultTestCaseAttachmentShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestCaseAttachmentsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        // Get all the testCaseAttachmentList where url in DEFAULT_URL or UPDATED_URL
        defaultTestCaseAttachmentShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the testCaseAttachmentList where url equals to UPDATED_URL
        defaultTestCaseAttachmentShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestCaseAttachmentsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        // Get all the testCaseAttachmentList where url is not null
        defaultTestCaseAttachmentShouldBeFound("url.specified=true");

        // Get all the testCaseAttachmentList where url is null
        defaultTestCaseAttachmentShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCaseAttachmentsByUrlContainsSomething() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        // Get all the testCaseAttachmentList where url contains DEFAULT_URL
        defaultTestCaseAttachmentShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the testCaseAttachmentList where url contains UPDATED_URL
        defaultTestCaseAttachmentShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestCaseAttachmentsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        // Get all the testCaseAttachmentList where url does not contain DEFAULT_URL
        defaultTestCaseAttachmentShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the testCaseAttachmentList where url does not contain UPDATED_URL
        defaultTestCaseAttachmentShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTestCaseAttachmentsByTestCaseIsEqualToSomething() throws Exception {
        TestCase testCase;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);
            testCase = TestCaseResourceIT.createEntity(em);
        } else {
            testCase = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testCase);
        em.flush();
        testCaseAttachment.setTestCase(testCase);
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);
        Long testCaseId = testCase.getId();

        // Get all the testCaseAttachmentList where testCase equals to testCaseId
        defaultTestCaseAttachmentShouldBeFound("testCaseId.equals=" + testCaseId);

        // Get all the testCaseAttachmentList where testCase equals to (testCaseId + 1)
        defaultTestCaseAttachmentShouldNotBeFound("testCaseId.equals=" + (testCaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCaseAttachmentShouldBeFound(String filter) throws Exception {
        restTestCaseAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restTestCaseAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCaseAttachmentShouldNotBeFound(String filter) throws Exception {
        restTestCaseAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCaseAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCaseAttachment() throws Exception {
        // Get the testCaseAttachment
        restTestCaseAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCaseAttachment() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        int databaseSizeBeforeUpdate = testCaseAttachmentRepository.findAll().size();

        // Update the testCaseAttachment
        TestCaseAttachment updatedTestCaseAttachment = testCaseAttachmentRepository.findById(testCaseAttachment.getId()).get();
        // Disconnect from session so that the updates on updatedTestCaseAttachment are not directly saved in db
        em.detach(updatedTestCaseAttachment);
        updatedTestCaseAttachment.url(UPDATED_URL);

        restTestCaseAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCaseAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCaseAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestCaseAttachment testTestCaseAttachment = testCaseAttachmentList.get(testCaseAttachmentList.size() - 1);
        assertThat(testTestCaseAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingTestCaseAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentRepository.findAll().size();
        testCaseAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCaseAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentRepository.findAll().size();
        testCaseAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCaseAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentRepository.findAll().size();
        testCaseAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseAttachmentWithPatch() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        int databaseSizeBeforeUpdate = testCaseAttachmentRepository.findAll().size();

        // Update the testCaseAttachment using partial update
        TestCaseAttachment partialUpdatedTestCaseAttachment = new TestCaseAttachment();
        partialUpdatedTestCaseAttachment.setId(testCaseAttachment.getId());

        partialUpdatedTestCaseAttachment.url(UPDATED_URL);

        restTestCaseAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestCaseAttachment testTestCaseAttachment = testCaseAttachmentList.get(testCaseAttachmentList.size() - 1);
        assertThat(testTestCaseAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void fullUpdateTestCaseAttachmentWithPatch() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        int databaseSizeBeforeUpdate = testCaseAttachmentRepository.findAll().size();

        // Update the testCaseAttachment using partial update
        TestCaseAttachment partialUpdatedTestCaseAttachment = new TestCaseAttachment();
        partialUpdatedTestCaseAttachment.setId(testCaseAttachment.getId());

        partialUpdatedTestCaseAttachment.url(UPDATED_URL);

        restTestCaseAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseAttachment))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
        TestCaseAttachment testTestCaseAttachment = testCaseAttachmentList.get(testCaseAttachmentList.size() - 1);
        assertThat(testTestCaseAttachment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingTestCaseAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentRepository.findAll().size();
        testCaseAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCaseAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCaseAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentRepository.findAll().size();
        testCaseAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCaseAttachment() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentRepository.findAll().size();
        testCaseAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseAttachment in the database
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCaseAttachment() throws Exception {
        // Initialize the database
        testCaseAttachmentRepository.saveAndFlush(testCaseAttachment);

        int databaseSizeBeforeDelete = testCaseAttachmentRepository.findAll().size();

        // Delete the testCaseAttachment
        restTestCaseAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCaseAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCaseAttachment> testCaseAttachmentList = testCaseAttachmentRepository.findAll();
        assertThat(testCaseAttachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
