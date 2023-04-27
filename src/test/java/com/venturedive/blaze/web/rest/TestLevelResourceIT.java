package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.domain.TestLevel;
import com.venturedive.blaze.domain.TestRun;
import com.venturedive.blaze.repository.TestLevelRepository;
import com.venturedive.blaze.service.criteria.TestLevelCriteria;
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
 * Integration tests for the {@link TestLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestLevelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestLevelRepository testLevelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestLevelMockMvc;

    private TestLevel testLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestLevel createEntity(EntityManager em) {
        TestLevel testLevel = new TestLevel().name(DEFAULT_NAME);
        return testLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestLevel createUpdatedEntity(EntityManager em) {
        TestLevel testLevel = new TestLevel().name(UPDATED_NAME);
        return testLevel;
    }

    @BeforeEach
    public void initTest() {
        testLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createTestLevel() throws Exception {
        int databaseSizeBeforeCreate = testLevelRepository.findAll().size();
        // Create the TestLevel
        restTestLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testLevel)))
            .andExpect(status().isCreated());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeCreate + 1);
        TestLevel testTestLevel = testLevelList.get(testLevelList.size() - 1);
        assertThat(testTestLevel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTestLevelWithExistingId() throws Exception {
        // Create the TestLevel with an existing ID
        testLevel.setId(1L);

        int databaseSizeBeforeCreate = testLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testLevel)))
            .andExpect(status().isBadRequest());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestLevels() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        // Get all the testLevelList
        restTestLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTestLevel() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        // Get the testLevel
        restTestLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, testLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testLevel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTestLevelsByIdFiltering() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        Long id = testLevel.getId();

        defaultTestLevelShouldBeFound("id.equals=" + id);
        defaultTestLevelShouldNotBeFound("id.notEquals=" + id);

        defaultTestLevelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestLevelShouldNotBeFound("id.greaterThan=" + id);

        defaultTestLevelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestLevelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        // Get all the testLevelList where name equals to DEFAULT_NAME
        defaultTestLevelShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the testLevelList where name equals to UPDATED_NAME
        defaultTestLevelShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        // Get all the testLevelList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTestLevelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the testLevelList where name equals to UPDATED_NAME
        defaultTestLevelShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        // Get all the testLevelList where name is not null
        defaultTestLevelShouldBeFound("name.specified=true");

        // Get all the testLevelList where name is null
        defaultTestLevelShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameContainsSomething() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        // Get all the testLevelList where name contains DEFAULT_NAME
        defaultTestLevelShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the testLevelList where name contains UPDATED_NAME
        defaultTestLevelShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        // Get all the testLevelList where name does not contain DEFAULT_NAME
        defaultTestLevelShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the testLevelList where name does not contain UPDATED_NAME
        defaultTestLevelShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestLevelsByTestrunTestlevelIsEqualToSomething() throws Exception {
        TestRun testrunTestlevel;
        if (TestUtil.findAll(em, TestRun.class).isEmpty()) {
            testLevelRepository.saveAndFlush(testLevel);
            testrunTestlevel = TestRunResourceIT.createEntity(em);
        } else {
            testrunTestlevel = TestUtil.findAll(em, TestRun.class).get(0);
        }
        em.persist(testrunTestlevel);
        em.flush();
        testLevel.addTestrunTestlevel(testrunTestlevel);
        testLevelRepository.saveAndFlush(testLevel);
        Long testrunTestlevelId = testrunTestlevel.getId();

        // Get all the testLevelList where testrunTestlevel equals to testrunTestlevelId
        defaultTestLevelShouldBeFound("testrunTestlevelId.equals=" + testrunTestlevelId);

        // Get all the testLevelList where testrunTestlevel equals to (testrunTestlevelId + 1)
        defaultTestLevelShouldNotBeFound("testrunTestlevelId.equals=" + (testrunTestlevelId + 1));
    }

    @Test
    @Transactional
    void getAllTestLevelsByTestCaseIsEqualToSomething() throws Exception {
        TestCase testCase;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testLevelRepository.saveAndFlush(testLevel);
            testCase = TestCaseResourceIT.createEntity(em);
        } else {
            testCase = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testCase);
        em.flush();
        testLevel.addTestCase(testCase);
        testLevelRepository.saveAndFlush(testLevel);
        Long testCaseId = testCase.getId();

        // Get all the testLevelList where testCase equals to testCaseId
        defaultTestLevelShouldBeFound("testCaseId.equals=" + testCaseId);

        // Get all the testLevelList where testCase equals to (testCaseId + 1)
        defaultTestLevelShouldNotBeFound("testCaseId.equals=" + (testCaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestLevelShouldBeFound(String filter) throws Exception {
        restTestLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTestLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestLevelShouldNotBeFound(String filter) throws Exception {
        restTestLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestLevel() throws Exception {
        // Get the testLevel
        restTestLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestLevel() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        int databaseSizeBeforeUpdate = testLevelRepository.findAll().size();

        // Update the testLevel
        TestLevel updatedTestLevel = testLevelRepository.findById(testLevel.getId()).get();
        // Disconnect from session so that the updates on updatedTestLevel are not directly saved in db
        em.detach(updatedTestLevel);
        updatedTestLevel.name(UPDATED_NAME);

        restTestLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestLevel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestLevel))
            )
            .andExpect(status().isOk());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeUpdate);
        TestLevel testTestLevel = testLevelList.get(testLevelList.size() - 1);
        assertThat(testTestLevel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTestLevel() throws Exception {
        int databaseSizeBeforeUpdate = testLevelRepository.findAll().size();
        testLevel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testLevel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestLevel() throws Exception {
        int databaseSizeBeforeUpdate = testLevelRepository.findAll().size();
        testLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestLevel() throws Exception {
        int databaseSizeBeforeUpdate = testLevelRepository.findAll().size();
        testLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLevelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testLevel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestLevelWithPatch() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        int databaseSizeBeforeUpdate = testLevelRepository.findAll().size();

        // Update the testLevel using partial update
        TestLevel partialUpdatedTestLevel = new TestLevel();
        partialUpdatedTestLevel.setId(testLevel.getId());

        restTestLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestLevel))
            )
            .andExpect(status().isOk());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeUpdate);
        TestLevel testTestLevel = testLevelList.get(testLevelList.size() - 1);
        assertThat(testTestLevel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTestLevelWithPatch() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        int databaseSizeBeforeUpdate = testLevelRepository.findAll().size();

        // Update the testLevel using partial update
        TestLevel partialUpdatedTestLevel = new TestLevel();
        partialUpdatedTestLevel.setId(testLevel.getId());

        partialUpdatedTestLevel.name(UPDATED_NAME);

        restTestLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestLevel))
            )
            .andExpect(status().isOk());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeUpdate);
        TestLevel testTestLevel = testLevelList.get(testLevelList.size() - 1);
        assertThat(testTestLevel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTestLevel() throws Exception {
        int databaseSizeBeforeUpdate = testLevelRepository.findAll().size();
        testLevel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestLevel() throws Exception {
        int databaseSizeBeforeUpdate = testLevelRepository.findAll().size();
        testLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestLevel() throws Exception {
        int databaseSizeBeforeUpdate = testLevelRepository.findAll().size();
        testLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLevelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestLevel in the database
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestLevel() throws Exception {
        // Initialize the database
        testLevelRepository.saveAndFlush(testLevel);

        int databaseSizeBeforeDelete = testLevelRepository.findAll().size();

        // Delete the testLevel
        restTestLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, testLevel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestLevel> testLevelList = testLevelRepository.findAll();
        assertThat(testLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
