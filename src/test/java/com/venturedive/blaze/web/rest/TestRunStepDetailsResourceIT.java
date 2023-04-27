package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TestCaseField;
import com.venturedive.blaze.domain.TestRunDetails;
import com.venturedive.blaze.domain.TestRunStepDetailAttachment;
import com.venturedive.blaze.domain.TestRunStepDetails;
import com.venturedive.blaze.domain.TestStatus;
import com.venturedive.blaze.repository.TestRunStepDetailsRepository;
import com.venturedive.blaze.service.criteria.TestRunStepDetailsCriteria;
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
 * Integration tests for the {@link TestRunStepDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestRunStepDetailsResourceIT {

    private static final String DEFAULT_ACTUAL_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_ACTUAL_RESULT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-run-step-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestRunStepDetailsRepository testRunStepDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestRunStepDetailsMockMvc;

    private TestRunStepDetails testRunStepDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunStepDetails createEntity(EntityManager em) {
        TestRunStepDetails testRunStepDetails = new TestRunStepDetails().actualResult(DEFAULT_ACTUAL_RESULT);
        return testRunStepDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunStepDetails createUpdatedEntity(EntityManager em) {
        TestRunStepDetails testRunStepDetails = new TestRunStepDetails().actualResult(UPDATED_ACTUAL_RESULT);
        return testRunStepDetails;
    }

    @BeforeEach
    public void initTest() {
        testRunStepDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createTestRunStepDetails() throws Exception {
        int databaseSizeBeforeCreate = testRunStepDetailsRepository.findAll().size();
        // Create the TestRunStepDetails
        restTestRunStepDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRunStepDetails))
            )
            .andExpect(status().isCreated());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TestRunStepDetails testTestRunStepDetails = testRunStepDetailsList.get(testRunStepDetailsList.size() - 1);
        assertThat(testTestRunStepDetails.getActualResult()).isEqualTo(DEFAULT_ACTUAL_RESULT);
    }

    @Test
    @Transactional
    void createTestRunStepDetailsWithExistingId() throws Exception {
        // Create the TestRunStepDetails with an existing ID
        testRunStepDetails.setId(1L);

        int databaseSizeBeforeCreate = testRunStepDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestRunStepDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRunStepDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetails() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        // Get all the testRunStepDetailsList
        restTestRunStepDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunStepDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualResult").value(hasItem(DEFAULT_ACTUAL_RESULT)));
    }

    @Test
    @Transactional
    void getTestRunStepDetails() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        // Get the testRunStepDetails
        restTestRunStepDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, testRunStepDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testRunStepDetails.getId().intValue()))
            .andExpect(jsonPath("$.actualResult").value(DEFAULT_ACTUAL_RESULT));
    }

    @Test
    @Transactional
    void getTestRunStepDetailsByIdFiltering() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        Long id = testRunStepDetails.getId();

        defaultTestRunStepDetailsShouldBeFound("id.equals=" + id);
        defaultTestRunStepDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultTestRunStepDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestRunStepDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestRunStepDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestRunStepDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailsByActualResultIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        // Get all the testRunStepDetailsList where actualResult equals to DEFAULT_ACTUAL_RESULT
        defaultTestRunStepDetailsShouldBeFound("actualResult.equals=" + DEFAULT_ACTUAL_RESULT);

        // Get all the testRunStepDetailsList where actualResult equals to UPDATED_ACTUAL_RESULT
        defaultTestRunStepDetailsShouldNotBeFound("actualResult.equals=" + UPDATED_ACTUAL_RESULT);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailsByActualResultIsInShouldWork() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        // Get all the testRunStepDetailsList where actualResult in DEFAULT_ACTUAL_RESULT or UPDATED_ACTUAL_RESULT
        defaultTestRunStepDetailsShouldBeFound("actualResult.in=" + DEFAULT_ACTUAL_RESULT + "," + UPDATED_ACTUAL_RESULT);

        // Get all the testRunStepDetailsList where actualResult equals to UPDATED_ACTUAL_RESULT
        defaultTestRunStepDetailsShouldNotBeFound("actualResult.in=" + UPDATED_ACTUAL_RESULT);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailsByActualResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        // Get all the testRunStepDetailsList where actualResult is not null
        defaultTestRunStepDetailsShouldBeFound("actualResult.specified=true");

        // Get all the testRunStepDetailsList where actualResult is null
        defaultTestRunStepDetailsShouldNotBeFound("actualResult.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailsByActualResultContainsSomething() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        // Get all the testRunStepDetailsList where actualResult contains DEFAULT_ACTUAL_RESULT
        defaultTestRunStepDetailsShouldBeFound("actualResult.contains=" + DEFAULT_ACTUAL_RESULT);

        // Get all the testRunStepDetailsList where actualResult contains UPDATED_ACTUAL_RESULT
        defaultTestRunStepDetailsShouldNotBeFound("actualResult.contains=" + UPDATED_ACTUAL_RESULT);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailsByActualResultNotContainsSomething() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        // Get all the testRunStepDetailsList where actualResult does not contain DEFAULT_ACTUAL_RESULT
        defaultTestRunStepDetailsShouldNotBeFound("actualResult.doesNotContain=" + DEFAULT_ACTUAL_RESULT);

        // Get all the testRunStepDetailsList where actualResult does not contain UPDATED_ACTUAL_RESULT
        defaultTestRunStepDetailsShouldBeFound("actualResult.doesNotContain=" + UPDATED_ACTUAL_RESULT);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailsByTestRunDetailIsEqualToSomething() throws Exception {
        TestRunDetails testRunDetail;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);
            testRunDetail = TestRunDetailsResourceIT.createEntity(em);
        } else {
            testRunDetail = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        em.persist(testRunDetail);
        em.flush();
        testRunStepDetails.setTestRunDetail(testRunDetail);
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);
        Long testRunDetailId = testRunDetail.getId();

        // Get all the testRunStepDetailsList where testRunDetail equals to testRunDetailId
        defaultTestRunStepDetailsShouldBeFound("testRunDetailId.equals=" + testRunDetailId);

        // Get all the testRunStepDetailsList where testRunDetail equals to (testRunDetailId + 1)
        defaultTestRunStepDetailsShouldNotBeFound("testRunDetailId.equals=" + (testRunDetailId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailsByStepDetailIsEqualToSomething() throws Exception {
        TestCaseField stepDetail;
        if (TestUtil.findAll(em, TestCaseField.class).isEmpty()) {
            testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);
            stepDetail = TestCaseFieldResourceIT.createEntity(em);
        } else {
            stepDetail = TestUtil.findAll(em, TestCaseField.class).get(0);
        }
        em.persist(stepDetail);
        em.flush();
        testRunStepDetails.setStepDetail(stepDetail);
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);
        Long stepDetailId = stepDetail.getId();

        // Get all the testRunStepDetailsList where stepDetail equals to stepDetailId
        defaultTestRunStepDetailsShouldBeFound("stepDetailId.equals=" + stepDetailId);

        // Get all the testRunStepDetailsList where stepDetail equals to (stepDetailId + 1)
        defaultTestRunStepDetailsShouldNotBeFound("stepDetailId.equals=" + (stepDetailId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailsByStatusIsEqualToSomething() throws Exception {
        TestStatus status;
        if (TestUtil.findAll(em, TestStatus.class).isEmpty()) {
            testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);
            status = TestStatusResourceIT.createEntity(em);
        } else {
            status = TestUtil.findAll(em, TestStatus.class).get(0);
        }
        em.persist(status);
        em.flush();
        testRunStepDetails.setStatus(status);
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);
        Long statusId = status.getId();

        // Get all the testRunStepDetailsList where status equals to statusId
        defaultTestRunStepDetailsShouldBeFound("statusId.equals=" + statusId);

        // Get all the testRunStepDetailsList where status equals to (statusId + 1)
        defaultTestRunStepDetailsShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailsByTestrunstepdetailattachmentTestrunstepdetailIsEqualToSomething() throws Exception {
        TestRunStepDetailAttachment testrunstepdetailattachmentTestrunstepdetail;
        if (TestUtil.findAll(em, TestRunStepDetailAttachment.class).isEmpty()) {
            testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);
            testrunstepdetailattachmentTestrunstepdetail = TestRunStepDetailAttachmentResourceIT.createEntity(em);
        } else {
            testrunstepdetailattachmentTestrunstepdetail = TestUtil.findAll(em, TestRunStepDetailAttachment.class).get(0);
        }
        em.persist(testrunstepdetailattachmentTestrunstepdetail);
        em.flush();
        testRunStepDetails.addTestrunstepdetailattachmentTestrunstepdetail(testrunstepdetailattachmentTestrunstepdetail);
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);
        Long testrunstepdetailattachmentTestrunstepdetailId = testrunstepdetailattachmentTestrunstepdetail.getId();

        // Get all the testRunStepDetailsList where testrunstepdetailattachmentTestrunstepdetail equals to testrunstepdetailattachmentTestrunstepdetailId
        defaultTestRunStepDetailsShouldBeFound(
            "testrunstepdetailattachmentTestrunstepdetailId.equals=" + testrunstepdetailattachmentTestrunstepdetailId
        );

        // Get all the testRunStepDetailsList where testrunstepdetailattachmentTestrunstepdetail equals to (testrunstepdetailattachmentTestrunstepdetailId + 1)
        defaultTestRunStepDetailsShouldNotBeFound(
            "testrunstepdetailattachmentTestrunstepdetailId.equals=" + (testrunstepdetailattachmentTestrunstepdetailId + 1)
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestRunStepDetailsShouldBeFound(String filter) throws Exception {
        restTestRunStepDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunStepDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualResult").value(hasItem(DEFAULT_ACTUAL_RESULT)));

        // Check, that the count call also returns 1
        restTestRunStepDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestRunStepDetailsShouldNotBeFound(String filter) throws Exception {
        restTestRunStepDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestRunStepDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestRunStepDetails() throws Exception {
        // Get the testRunStepDetails
        restTestRunStepDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestRunStepDetails() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        int databaseSizeBeforeUpdate = testRunStepDetailsRepository.findAll().size();

        // Update the testRunStepDetails
        TestRunStepDetails updatedTestRunStepDetails = testRunStepDetailsRepository.findById(testRunStepDetails.getId()).get();
        // Disconnect from session so that the updates on updatedTestRunStepDetails are not directly saved in db
        em.detach(updatedTestRunStepDetails);
        updatedTestRunStepDetails.actualResult(UPDATED_ACTUAL_RESULT);

        restTestRunStepDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestRunStepDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestRunStepDetails))
            )
            .andExpect(status().isOk());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeUpdate);
        TestRunStepDetails testTestRunStepDetails = testRunStepDetailsList.get(testRunStepDetailsList.size() - 1);
        assertThat(testTestRunStepDetails.getActualResult()).isEqualTo(UPDATED_ACTUAL_RESULT);
    }

    @Test
    @Transactional
    void putNonExistingTestRunStepDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailsRepository.findAll().size();
        testRunStepDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunStepDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testRunStepDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestRunStepDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailsRepository.findAll().size();
        testRunStepDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestRunStepDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailsRepository.findAll().size();
        testRunStepDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRunStepDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestRunStepDetailsWithPatch() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        int databaseSizeBeforeUpdate = testRunStepDetailsRepository.findAll().size();

        // Update the testRunStepDetails using partial update
        TestRunStepDetails partialUpdatedTestRunStepDetails = new TestRunStepDetails();
        partialUpdatedTestRunStepDetails.setId(testRunStepDetails.getId());

        partialUpdatedTestRunStepDetails.actualResult(UPDATED_ACTUAL_RESULT);

        restTestRunStepDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunStepDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunStepDetails))
            )
            .andExpect(status().isOk());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeUpdate);
        TestRunStepDetails testTestRunStepDetails = testRunStepDetailsList.get(testRunStepDetailsList.size() - 1);
        assertThat(testTestRunStepDetails.getActualResult()).isEqualTo(UPDATED_ACTUAL_RESULT);
    }

    @Test
    @Transactional
    void fullUpdateTestRunStepDetailsWithPatch() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        int databaseSizeBeforeUpdate = testRunStepDetailsRepository.findAll().size();

        // Update the testRunStepDetails using partial update
        TestRunStepDetails partialUpdatedTestRunStepDetails = new TestRunStepDetails();
        partialUpdatedTestRunStepDetails.setId(testRunStepDetails.getId());

        partialUpdatedTestRunStepDetails.actualResult(UPDATED_ACTUAL_RESULT);

        restTestRunStepDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunStepDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunStepDetails))
            )
            .andExpect(status().isOk());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeUpdate);
        TestRunStepDetails testTestRunStepDetails = testRunStepDetailsList.get(testRunStepDetailsList.size() - 1);
        assertThat(testTestRunStepDetails.getActualResult()).isEqualTo(UPDATED_ACTUAL_RESULT);
    }

    @Test
    @Transactional
    void patchNonExistingTestRunStepDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailsRepository.findAll().size();
        testRunStepDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunStepDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testRunStepDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestRunStepDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailsRepository.findAll().size();
        testRunStepDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestRunStepDetails() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailsRepository.findAll().size();
        testRunStepDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunStepDetails in the database
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestRunStepDetails() throws Exception {
        // Initialize the database
        testRunStepDetailsRepository.saveAndFlush(testRunStepDetails);

        int databaseSizeBeforeDelete = testRunStepDetailsRepository.findAll().size();

        // Delete the testRunStepDetails
        restTestRunStepDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testRunStepDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestRunStepDetails> testRunStepDetailsList = testRunStepDetailsRepository.findAll();
        assertThat(testRunStepDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
