package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TestRunDetails;
import com.venturedive.blaze.domain.TestRunStepDetails;
import com.venturedive.blaze.domain.TestStatus;
import com.venturedive.blaze.repository.TestStatusRepository;
import com.venturedive.blaze.service.criteria.TestStatusCriteria;
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
 * Integration tests for the {@link TestStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestStatusResourceIT {

    private static final String DEFAULT_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestStatusRepository testStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestStatusMockMvc;

    private TestStatus testStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestStatus createEntity(EntityManager em) {
        TestStatus testStatus = new TestStatus().statusName(DEFAULT_STATUS_NAME);
        return testStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestStatus createUpdatedEntity(EntityManager em) {
        TestStatus testStatus = new TestStatus().statusName(UPDATED_STATUS_NAME);
        return testStatus;
    }

    @BeforeEach
    public void initTest() {
        testStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createTestStatus() throws Exception {
        int databaseSizeBeforeCreate = testStatusRepository.findAll().size();
        // Create the TestStatus
        restTestStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testStatus)))
            .andExpect(status().isCreated());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeCreate + 1);
        TestStatus testTestStatus = testStatusList.get(testStatusList.size() - 1);
        assertThat(testTestStatus.getStatusName()).isEqualTo(DEFAULT_STATUS_NAME);
    }

    @Test
    @Transactional
    void createTestStatusWithExistingId() throws Exception {
        // Create the TestStatus with an existing ID
        testStatus.setId(1L);

        int databaseSizeBeforeCreate = testStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testStatus)))
            .andExpect(status().isBadRequest());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestStatuses() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        // Get all the testStatusList
        restTestStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)));
    }

    @Test
    @Transactional
    void getTestStatus() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        // Get the testStatus
        restTestStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, testStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testStatus.getId().intValue()))
            .andExpect(jsonPath("$.statusName").value(DEFAULT_STATUS_NAME));
    }

    @Test
    @Transactional
    void getTestStatusesByIdFiltering() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        Long id = testStatus.getId();

        defaultTestStatusShouldBeFound("id.equals=" + id);
        defaultTestStatusShouldNotBeFound("id.notEquals=" + id);

        defaultTestStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultTestStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        // Get all the testStatusList where statusName equals to DEFAULT_STATUS_NAME
        defaultTestStatusShouldBeFound("statusName.equals=" + DEFAULT_STATUS_NAME);

        // Get all the testStatusList where statusName equals to UPDATED_STATUS_NAME
        defaultTestStatusShouldNotBeFound("statusName.equals=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameIsInShouldWork() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        // Get all the testStatusList where statusName in DEFAULT_STATUS_NAME or UPDATED_STATUS_NAME
        defaultTestStatusShouldBeFound("statusName.in=" + DEFAULT_STATUS_NAME + "," + UPDATED_STATUS_NAME);

        // Get all the testStatusList where statusName equals to UPDATED_STATUS_NAME
        defaultTestStatusShouldNotBeFound("statusName.in=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        // Get all the testStatusList where statusName is not null
        defaultTestStatusShouldBeFound("statusName.specified=true");

        // Get all the testStatusList where statusName is null
        defaultTestStatusShouldNotBeFound("statusName.specified=false");
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameContainsSomething() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        // Get all the testStatusList where statusName contains DEFAULT_STATUS_NAME
        defaultTestStatusShouldBeFound("statusName.contains=" + DEFAULT_STATUS_NAME);

        // Get all the testStatusList where statusName contains UPDATED_STATUS_NAME
        defaultTestStatusShouldNotBeFound("statusName.contains=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameNotContainsSomething() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        // Get all the testStatusList where statusName does not contain DEFAULT_STATUS_NAME
        defaultTestStatusShouldNotBeFound("statusName.doesNotContain=" + DEFAULT_STATUS_NAME);

        // Get all the testStatusList where statusName does not contain UPDATED_STATUS_NAME
        defaultTestStatusShouldBeFound("statusName.doesNotContain=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTestStatusesByTestrundetailsStatusIsEqualToSomething() throws Exception {
        TestRunDetails testrundetailsStatus;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testStatusRepository.saveAndFlush(testStatus);
            testrundetailsStatus = TestRunDetailsResourceIT.createEntity(em);
        } else {
            testrundetailsStatus = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        em.persist(testrundetailsStatus);
        em.flush();
        testStatus.addTestrundetailsStatus(testrundetailsStatus);
        testStatusRepository.saveAndFlush(testStatus);
        Long testrundetailsStatusId = testrundetailsStatus.getId();

        // Get all the testStatusList where testrundetailsStatus equals to testrundetailsStatusId
        defaultTestStatusShouldBeFound("testrundetailsStatusId.equals=" + testrundetailsStatusId);

        // Get all the testStatusList where testrundetailsStatus equals to (testrundetailsStatusId + 1)
        defaultTestStatusShouldNotBeFound("testrundetailsStatusId.equals=" + (testrundetailsStatusId + 1));
    }

    @Test
    @Transactional
    void getAllTestStatusesByTestrunstepdetailsStatusIsEqualToSomething() throws Exception {
        TestRunStepDetails testrunstepdetailsStatus;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testStatusRepository.saveAndFlush(testStatus);
            testrunstepdetailsStatus = TestRunStepDetailsResourceIT.createEntity(em);
        } else {
            testrunstepdetailsStatus = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        em.persist(testrunstepdetailsStatus);
        em.flush();
        testStatus.addTestrunstepdetailsStatus(testrunstepdetailsStatus);
        testStatusRepository.saveAndFlush(testStatus);
        Long testrunstepdetailsStatusId = testrunstepdetailsStatus.getId();

        // Get all the testStatusList where testrunstepdetailsStatus equals to testrunstepdetailsStatusId
        defaultTestStatusShouldBeFound("testrunstepdetailsStatusId.equals=" + testrunstepdetailsStatusId);

        // Get all the testStatusList where testrunstepdetailsStatus equals to (testrunstepdetailsStatusId + 1)
        defaultTestStatusShouldNotBeFound("testrunstepdetailsStatusId.equals=" + (testrunstepdetailsStatusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestStatusShouldBeFound(String filter) throws Exception {
        restTestStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)));

        // Check, that the count call also returns 1
        restTestStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestStatusShouldNotBeFound(String filter) throws Exception {
        restTestStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestStatus() throws Exception {
        // Get the testStatus
        restTestStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestStatus() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        int databaseSizeBeforeUpdate = testStatusRepository.findAll().size();

        // Update the testStatus
        TestStatus updatedTestStatus = testStatusRepository.findById(testStatus.getId()).get();
        // Disconnect from session so that the updates on updatedTestStatus are not directly saved in db
        em.detach(updatedTestStatus);
        updatedTestStatus.statusName(UPDATED_STATUS_NAME);

        restTestStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestStatus))
            )
            .andExpect(status().isOk());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeUpdate);
        TestStatus testTestStatus = testStatusList.get(testStatusList.size() - 1);
        assertThat(testTestStatus.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTestStatus() throws Exception {
        int databaseSizeBeforeUpdate = testStatusRepository.findAll().size();
        testStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestStatus() throws Exception {
        int databaseSizeBeforeUpdate = testStatusRepository.findAll().size();
        testStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestStatus() throws Exception {
        int databaseSizeBeforeUpdate = testStatusRepository.findAll().size();
        testStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestStatusWithPatch() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        int databaseSizeBeforeUpdate = testStatusRepository.findAll().size();

        // Update the testStatus using partial update
        TestStatus partialUpdatedTestStatus = new TestStatus();
        partialUpdatedTestStatus.setId(testStatus.getId());

        partialUpdatedTestStatus.statusName(UPDATED_STATUS_NAME);

        restTestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestStatus))
            )
            .andExpect(status().isOk());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeUpdate);
        TestStatus testTestStatus = testStatusList.get(testStatusList.size() - 1);
        assertThat(testTestStatus.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTestStatusWithPatch() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        int databaseSizeBeforeUpdate = testStatusRepository.findAll().size();

        // Update the testStatus using partial update
        TestStatus partialUpdatedTestStatus = new TestStatus();
        partialUpdatedTestStatus.setId(testStatus.getId());

        partialUpdatedTestStatus.statusName(UPDATED_STATUS_NAME);

        restTestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestStatus))
            )
            .andExpect(status().isOk());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeUpdate);
        TestStatus testTestStatus = testStatusList.get(testStatusList.size() - 1);
        assertThat(testTestStatus.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTestStatus() throws Exception {
        int databaseSizeBeforeUpdate = testStatusRepository.findAll().size();
        testStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestStatus() throws Exception {
        int databaseSizeBeforeUpdate = testStatusRepository.findAll().size();
        testStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestStatus() throws Exception {
        int databaseSizeBeforeUpdate = testStatusRepository.findAll().size();
        testStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestStatus in the database
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestStatus() throws Exception {
        // Initialize the database
        testStatusRepository.saveAndFlush(testStatus);

        int databaseSizeBeforeDelete = testStatusRepository.findAll().size();

        // Delete the testStatus
        restTestStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, testStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestStatus> testStatusList = testStatusRepository.findAll();
        assertThat(testStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
