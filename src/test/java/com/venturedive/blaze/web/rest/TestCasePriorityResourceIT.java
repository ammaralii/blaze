package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.domain.TestCasePriority;
import com.venturedive.blaze.repository.TestCasePriorityRepository;
import com.venturedive.blaze.service.criteria.TestCasePriorityCriteria;
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
 * Integration tests for the {@link TestCasePriorityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCasePriorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-case-priorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCasePriorityRepository testCasePriorityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCasePriorityMockMvc;

    private TestCasePriority testCasePriority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCasePriority createEntity(EntityManager em) {
        TestCasePriority testCasePriority = new TestCasePriority().name(DEFAULT_NAME);
        return testCasePriority;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCasePriority createUpdatedEntity(EntityManager em) {
        TestCasePriority testCasePriority = new TestCasePriority().name(UPDATED_NAME);
        return testCasePriority;
    }

    @BeforeEach
    public void initTest() {
        testCasePriority = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCasePriority() throws Exception {
        int databaseSizeBeforeCreate = testCasePriorityRepository.findAll().size();
        // Create the TestCasePriority
        restTestCasePriorityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCasePriority))
            )
            .andExpect(status().isCreated());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeCreate + 1);
        TestCasePriority testTestCasePriority = testCasePriorityList.get(testCasePriorityList.size() - 1);
        assertThat(testTestCasePriority.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTestCasePriorityWithExistingId() throws Exception {
        // Create the TestCasePriority with an existing ID
        testCasePriority.setId(1L);

        int databaseSizeBeforeCreate = testCasePriorityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCasePriorityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCasePriority))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCasePriorityRepository.findAll().size();
        // set the field null
        testCasePriority.setName(null);

        // Create the TestCasePriority, which fails.

        restTestCasePriorityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCasePriority))
            )
            .andExpect(status().isBadRequest());

        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestCasePriorities() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        // Get all the testCasePriorityList
        restTestCasePriorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCasePriority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTestCasePriority() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        // Get the testCasePriority
        restTestCasePriorityMockMvc
            .perform(get(ENTITY_API_URL_ID, testCasePriority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCasePriority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTestCasePrioritiesByIdFiltering() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        Long id = testCasePriority.getId();

        defaultTestCasePriorityShouldBeFound("id.equals=" + id);
        defaultTestCasePriorityShouldNotBeFound("id.notEquals=" + id);

        defaultTestCasePriorityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCasePriorityShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCasePriorityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCasePriorityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        // Get all the testCasePriorityList where name equals to DEFAULT_NAME
        defaultTestCasePriorityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the testCasePriorityList where name equals to UPDATED_NAME
        defaultTestCasePriorityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        // Get all the testCasePriorityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTestCasePriorityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the testCasePriorityList where name equals to UPDATED_NAME
        defaultTestCasePriorityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        // Get all the testCasePriorityList where name is not null
        defaultTestCasePriorityShouldBeFound("name.specified=true");

        // Get all the testCasePriorityList where name is null
        defaultTestCasePriorityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameContainsSomething() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        // Get all the testCasePriorityList where name contains DEFAULT_NAME
        defaultTestCasePriorityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the testCasePriorityList where name contains UPDATED_NAME
        defaultTestCasePriorityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        // Get all the testCasePriorityList where name does not contain DEFAULT_NAME
        defaultTestCasePriorityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the testCasePriorityList where name does not contain UPDATED_NAME
        defaultTestCasePriorityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByTestcasePriorityIsEqualToSomething() throws Exception {
        TestCase testcasePriority;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testCasePriorityRepository.saveAndFlush(testCasePriority);
            testcasePriority = TestCaseResourceIT.createEntity(em);
        } else {
            testcasePriority = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testcasePriority);
        em.flush();
        testCasePriority.addTestcasePriority(testcasePriority);
        testCasePriorityRepository.saveAndFlush(testCasePriority);
        Long testcasePriorityId = testcasePriority.getId();

        // Get all the testCasePriorityList where testcasePriority equals to testcasePriorityId
        defaultTestCasePriorityShouldBeFound("testcasePriorityId.equals=" + testcasePriorityId);

        // Get all the testCasePriorityList where testcasePriority equals to (testcasePriorityId + 1)
        defaultTestCasePriorityShouldNotBeFound("testcasePriorityId.equals=" + (testcasePriorityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCasePriorityShouldBeFound(String filter) throws Exception {
        restTestCasePriorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCasePriority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTestCasePriorityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCasePriorityShouldNotBeFound(String filter) throws Exception {
        restTestCasePriorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCasePriorityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCasePriority() throws Exception {
        // Get the testCasePriority
        restTestCasePriorityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCasePriority() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        int databaseSizeBeforeUpdate = testCasePriorityRepository.findAll().size();

        // Update the testCasePriority
        TestCasePriority updatedTestCasePriority = testCasePriorityRepository.findById(testCasePriority.getId()).get();
        // Disconnect from session so that the updates on updatedTestCasePriority are not directly saved in db
        em.detach(updatedTestCasePriority);
        updatedTestCasePriority.name(UPDATED_NAME);

        restTestCasePriorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCasePriority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCasePriority))
            )
            .andExpect(status().isOk());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeUpdate);
        TestCasePriority testTestCasePriority = testCasePriorityList.get(testCasePriorityList.size() - 1);
        assertThat(testTestCasePriority.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTestCasePriority() throws Exception {
        int databaseSizeBeforeUpdate = testCasePriorityRepository.findAll().size();
        testCasePriority.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCasePriorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCasePriority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriority))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCasePriority() throws Exception {
        int databaseSizeBeforeUpdate = testCasePriorityRepository.findAll().size();
        testCasePriority.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasePriorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriority))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCasePriority() throws Exception {
        int databaseSizeBeforeUpdate = testCasePriorityRepository.findAll().size();
        testCasePriority.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasePriorityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCasePriority))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCasePriorityWithPatch() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        int databaseSizeBeforeUpdate = testCasePriorityRepository.findAll().size();

        // Update the testCasePriority using partial update
        TestCasePriority partialUpdatedTestCasePriority = new TestCasePriority();
        partialUpdatedTestCasePriority.setId(testCasePriority.getId());

        partialUpdatedTestCasePriority.name(UPDATED_NAME);

        restTestCasePriorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCasePriority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCasePriority))
            )
            .andExpect(status().isOk());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeUpdate);
        TestCasePriority testTestCasePriority = testCasePriorityList.get(testCasePriorityList.size() - 1);
        assertThat(testTestCasePriority.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTestCasePriorityWithPatch() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        int databaseSizeBeforeUpdate = testCasePriorityRepository.findAll().size();

        // Update the testCasePriority using partial update
        TestCasePriority partialUpdatedTestCasePriority = new TestCasePriority();
        partialUpdatedTestCasePriority.setId(testCasePriority.getId());

        partialUpdatedTestCasePriority.name(UPDATED_NAME);

        restTestCasePriorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCasePriority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCasePriority))
            )
            .andExpect(status().isOk());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeUpdate);
        TestCasePriority testTestCasePriority = testCasePriorityList.get(testCasePriorityList.size() - 1);
        assertThat(testTestCasePriority.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTestCasePriority() throws Exception {
        int databaseSizeBeforeUpdate = testCasePriorityRepository.findAll().size();
        testCasePriority.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCasePriorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCasePriority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriority))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCasePriority() throws Exception {
        int databaseSizeBeforeUpdate = testCasePriorityRepository.findAll().size();
        testCasePriority.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasePriorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriority))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCasePriority() throws Exception {
        int databaseSizeBeforeUpdate = testCasePriorityRepository.findAll().size();
        testCasePriority.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasePriorityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriority))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCasePriority in the database
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCasePriority() throws Exception {
        // Initialize the database
        testCasePriorityRepository.saveAndFlush(testCasePriority);

        int databaseSizeBeforeDelete = testCasePriorityRepository.findAll().size();

        // Delete the testCasePriority
        restTestCasePriorityMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCasePriority.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCasePriority> testCasePriorityList = testCasePriorityRepository.findAll();
        assertThat(testCasePriorityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
