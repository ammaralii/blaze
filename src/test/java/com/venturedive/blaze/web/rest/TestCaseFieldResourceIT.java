package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.TemplateField;
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.domain.TestCaseField;
import com.venturedive.blaze.domain.TestCaseFieldAttachment;
import com.venturedive.blaze.domain.TestRunStepDetails;
import com.venturedive.blaze.repository.TestCaseFieldRepository;
import com.venturedive.blaze.service.criteria.TestCaseFieldCriteria;
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
 * Integration tests for the {@link TestCaseFieldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCaseFieldResourceIT {

    private static final String DEFAULT_EXPECTED_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_EXPECTED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-case-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCaseFieldRepository testCaseFieldRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseFieldMockMvc;

    private TestCaseField testCaseField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseField createEntity(EntityManager em) {
        TestCaseField testCaseField = new TestCaseField().expectedResult(DEFAULT_EXPECTED_RESULT).value(DEFAULT_VALUE);
        // Add required entity
        TemplateField templateField;
        if (TestUtil.findAll(em, TemplateField.class).isEmpty()) {
            templateField = TemplateFieldResourceIT.createEntity(em);
            em.persist(templateField);
            em.flush();
        } else {
            templateField = TestUtil.findAll(em, TemplateField.class).get(0);
        }
        testCaseField.setTemplateField(templateField);
        // Add required entity
        TestCase testCase;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testCase = TestCaseResourceIT.createEntity(em);
            em.persist(testCase);
            em.flush();
        } else {
            testCase = TestUtil.findAll(em, TestCase.class).get(0);
        }
        testCaseField.setTestCase(testCase);
        return testCaseField;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseField createUpdatedEntity(EntityManager em) {
        TestCaseField testCaseField = new TestCaseField().expectedResult(UPDATED_EXPECTED_RESULT).value(UPDATED_VALUE);
        // Add required entity
        TemplateField templateField;
        if (TestUtil.findAll(em, TemplateField.class).isEmpty()) {
            templateField = TemplateFieldResourceIT.createUpdatedEntity(em);
            em.persist(templateField);
            em.flush();
        } else {
            templateField = TestUtil.findAll(em, TemplateField.class).get(0);
        }
        testCaseField.setTemplateField(templateField);
        // Add required entity
        TestCase testCase;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testCase = TestCaseResourceIT.createUpdatedEntity(em);
            em.persist(testCase);
            em.flush();
        } else {
            testCase = TestUtil.findAll(em, TestCase.class).get(0);
        }
        testCaseField.setTestCase(testCase);
        return testCaseField;
    }

    @BeforeEach
    public void initTest() {
        testCaseField = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCaseField() throws Exception {
        int databaseSizeBeforeCreate = testCaseFieldRepository.findAll().size();
        // Create the TestCaseField
        restTestCaseFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseField)))
            .andExpect(status().isCreated());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeCreate + 1);
        TestCaseField testTestCaseField = testCaseFieldList.get(testCaseFieldList.size() - 1);
        assertThat(testTestCaseField.getExpectedResult()).isEqualTo(DEFAULT_EXPECTED_RESULT);
        assertThat(testTestCaseField.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createTestCaseFieldWithExistingId() throws Exception {
        // Create the TestCaseField with an existing ID
        testCaseField.setId(1L);

        int databaseSizeBeforeCreate = testCaseFieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseField)))
            .andExpect(status().isBadRequest());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCaseFields() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList
        restTestCaseFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseField.getId().intValue())))
            .andExpect(jsonPath("$.[*].expectedResult").value(hasItem(DEFAULT_EXPECTED_RESULT)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getTestCaseField() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get the testCaseField
        restTestCaseFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, testCaseField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCaseField.getId().intValue()))
            .andExpect(jsonPath("$.expectedResult").value(DEFAULT_EXPECTED_RESULT))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getTestCaseFieldsByIdFiltering() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        Long id = testCaseField.getId();

        defaultTestCaseFieldShouldBeFound("id.equals=" + id);
        defaultTestCaseFieldShouldNotBeFound("id.notEquals=" + id);

        defaultTestCaseFieldShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCaseFieldShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCaseFieldShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCaseFieldShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByExpectedResultIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where expectedResult equals to DEFAULT_EXPECTED_RESULT
        defaultTestCaseFieldShouldBeFound("expectedResult.equals=" + DEFAULT_EXPECTED_RESULT);

        // Get all the testCaseFieldList where expectedResult equals to UPDATED_EXPECTED_RESULT
        defaultTestCaseFieldShouldNotBeFound("expectedResult.equals=" + UPDATED_EXPECTED_RESULT);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByExpectedResultIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where expectedResult in DEFAULT_EXPECTED_RESULT or UPDATED_EXPECTED_RESULT
        defaultTestCaseFieldShouldBeFound("expectedResult.in=" + DEFAULT_EXPECTED_RESULT + "," + UPDATED_EXPECTED_RESULT);

        // Get all the testCaseFieldList where expectedResult equals to UPDATED_EXPECTED_RESULT
        defaultTestCaseFieldShouldNotBeFound("expectedResult.in=" + UPDATED_EXPECTED_RESULT);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByExpectedResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where expectedResult is not null
        defaultTestCaseFieldShouldBeFound("expectedResult.specified=true");

        // Get all the testCaseFieldList where expectedResult is null
        defaultTestCaseFieldShouldNotBeFound("expectedResult.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByExpectedResultContainsSomething() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where expectedResult contains DEFAULT_EXPECTED_RESULT
        defaultTestCaseFieldShouldBeFound("expectedResult.contains=" + DEFAULT_EXPECTED_RESULT);

        // Get all the testCaseFieldList where expectedResult contains UPDATED_EXPECTED_RESULT
        defaultTestCaseFieldShouldNotBeFound("expectedResult.contains=" + UPDATED_EXPECTED_RESULT);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByExpectedResultNotContainsSomething() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where expectedResult does not contain DEFAULT_EXPECTED_RESULT
        defaultTestCaseFieldShouldNotBeFound("expectedResult.doesNotContain=" + DEFAULT_EXPECTED_RESULT);

        // Get all the testCaseFieldList where expectedResult does not contain UPDATED_EXPECTED_RESULT
        defaultTestCaseFieldShouldBeFound("expectedResult.doesNotContain=" + UPDATED_EXPECTED_RESULT);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where value equals to DEFAULT_VALUE
        defaultTestCaseFieldShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the testCaseFieldList where value equals to UPDATED_VALUE
        defaultTestCaseFieldShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultTestCaseFieldShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the testCaseFieldList where value equals to UPDATED_VALUE
        defaultTestCaseFieldShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where value is not null
        defaultTestCaseFieldShouldBeFound("value.specified=true");

        // Get all the testCaseFieldList where value is null
        defaultTestCaseFieldShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueContainsSomething() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where value contains DEFAULT_VALUE
        defaultTestCaseFieldShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the testCaseFieldList where value contains UPDATED_VALUE
        defaultTestCaseFieldShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        // Get all the testCaseFieldList where value does not contain DEFAULT_VALUE
        defaultTestCaseFieldShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the testCaseFieldList where value does not contain UPDATED_VALUE
        defaultTestCaseFieldShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByTemplateFieldIsEqualToSomething() throws Exception {
        TemplateField templateField;
        if (TestUtil.findAll(em, TemplateField.class).isEmpty()) {
            testCaseFieldRepository.saveAndFlush(testCaseField);
            templateField = TemplateFieldResourceIT.createEntity(em);
        } else {
            templateField = TestUtil.findAll(em, TemplateField.class).get(0);
        }
        em.persist(templateField);
        em.flush();
        testCaseField.setTemplateField(templateField);
        testCaseFieldRepository.saveAndFlush(testCaseField);
        Long templateFieldId = templateField.getId();

        // Get all the testCaseFieldList where templateField equals to templateFieldId
        defaultTestCaseFieldShouldBeFound("templateFieldId.equals=" + templateFieldId);

        // Get all the testCaseFieldList where templateField equals to (templateFieldId + 1)
        defaultTestCaseFieldShouldNotBeFound("templateFieldId.equals=" + (templateFieldId + 1));
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByTestCaseIsEqualToSomething() throws Exception {
        TestCase testCase;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testCaseFieldRepository.saveAndFlush(testCaseField);
            testCase = TestCaseResourceIT.createEntity(em);
        } else {
            testCase = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testCase);
        em.flush();
        testCaseField.setTestCase(testCase);
        testCaseFieldRepository.saveAndFlush(testCaseField);
        Long testCaseId = testCase.getId();

        // Get all the testCaseFieldList where testCase equals to testCaseId
        defaultTestCaseFieldShouldBeFound("testCaseId.equals=" + testCaseId);

        // Get all the testCaseFieldList where testCase equals to (testCaseId + 1)
        defaultTestCaseFieldShouldNotBeFound("testCaseId.equals=" + (testCaseId + 1));
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByTestcasefieldattachmentTestcasefieldIsEqualToSomething() throws Exception {
        TestCaseFieldAttachment testcasefieldattachmentTestcasefield;
        if (TestUtil.findAll(em, TestCaseFieldAttachment.class).isEmpty()) {
            testCaseFieldRepository.saveAndFlush(testCaseField);
            testcasefieldattachmentTestcasefield = TestCaseFieldAttachmentResourceIT.createEntity(em);
        } else {
            testcasefieldattachmentTestcasefield = TestUtil.findAll(em, TestCaseFieldAttachment.class).get(0);
        }
        em.persist(testcasefieldattachmentTestcasefield);
        em.flush();
        testCaseField.addTestcasefieldattachmentTestcasefield(testcasefieldattachmentTestcasefield);
        testCaseFieldRepository.saveAndFlush(testCaseField);
        Long testcasefieldattachmentTestcasefieldId = testcasefieldattachmentTestcasefield.getId();

        // Get all the testCaseFieldList where testcasefieldattachmentTestcasefield equals to testcasefieldattachmentTestcasefieldId
        defaultTestCaseFieldShouldBeFound("testcasefieldattachmentTestcasefieldId.equals=" + testcasefieldattachmentTestcasefieldId);

        // Get all the testCaseFieldList where testcasefieldattachmentTestcasefield equals to (testcasefieldattachmentTestcasefieldId + 1)
        defaultTestCaseFieldShouldNotBeFound(
            "testcasefieldattachmentTestcasefieldId.equals=" + (testcasefieldattachmentTestcasefieldId + 1)
        );
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByTestrunstepdetailsStepdetailIsEqualToSomething() throws Exception {
        TestRunStepDetails testrunstepdetailsStepdetail;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testCaseFieldRepository.saveAndFlush(testCaseField);
            testrunstepdetailsStepdetail = TestRunStepDetailsResourceIT.createEntity(em);
        } else {
            testrunstepdetailsStepdetail = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        em.persist(testrunstepdetailsStepdetail);
        em.flush();
        testCaseField.addTestrunstepdetailsStepdetail(testrunstepdetailsStepdetail);
        testCaseFieldRepository.saveAndFlush(testCaseField);
        Long testrunstepdetailsStepdetailId = testrunstepdetailsStepdetail.getId();

        // Get all the testCaseFieldList where testrunstepdetailsStepdetail equals to testrunstepdetailsStepdetailId
        defaultTestCaseFieldShouldBeFound("testrunstepdetailsStepdetailId.equals=" + testrunstepdetailsStepdetailId);

        // Get all the testCaseFieldList where testrunstepdetailsStepdetail equals to (testrunstepdetailsStepdetailId + 1)
        defaultTestCaseFieldShouldNotBeFound("testrunstepdetailsStepdetailId.equals=" + (testrunstepdetailsStepdetailId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCaseFieldShouldBeFound(String filter) throws Exception {
        restTestCaseFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseField.getId().intValue())))
            .andExpect(jsonPath("$.[*].expectedResult").value(hasItem(DEFAULT_EXPECTED_RESULT)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restTestCaseFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCaseFieldShouldNotBeFound(String filter) throws Exception {
        restTestCaseFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCaseFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCaseField() throws Exception {
        // Get the testCaseField
        restTestCaseFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCaseField() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        int databaseSizeBeforeUpdate = testCaseFieldRepository.findAll().size();

        // Update the testCaseField
        TestCaseField updatedTestCaseField = testCaseFieldRepository.findById(testCaseField.getId()).get();
        // Disconnect from session so that the updates on updatedTestCaseField are not directly saved in db
        em.detach(updatedTestCaseField);
        updatedTestCaseField.expectedResult(UPDATED_EXPECTED_RESULT).value(UPDATED_VALUE);

        restTestCaseFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCaseField.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCaseField))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeUpdate);
        TestCaseField testTestCaseField = testCaseFieldList.get(testCaseFieldList.size() - 1);
        assertThat(testTestCaseField.getExpectedResult()).isEqualTo(UPDATED_EXPECTED_RESULT);
        assertThat(testTestCaseField.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingTestCaseField() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldRepository.findAll().size();
        testCaseField.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseField.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseField))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCaseField() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldRepository.findAll().size();
        testCaseField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseField))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCaseField() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldRepository.findAll().size();
        testCaseField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseField)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseFieldWithPatch() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        int databaseSizeBeforeUpdate = testCaseFieldRepository.findAll().size();

        // Update the testCaseField using partial update
        TestCaseField partialUpdatedTestCaseField = new TestCaseField();
        partialUpdatedTestCaseField.setId(testCaseField.getId());

        partialUpdatedTestCaseField.expectedResult(UPDATED_EXPECTED_RESULT);

        restTestCaseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseField))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeUpdate);
        TestCaseField testTestCaseField = testCaseFieldList.get(testCaseFieldList.size() - 1);
        assertThat(testTestCaseField.getExpectedResult()).isEqualTo(UPDATED_EXPECTED_RESULT);
        assertThat(testTestCaseField.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTestCaseFieldWithPatch() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        int databaseSizeBeforeUpdate = testCaseFieldRepository.findAll().size();

        // Update the testCaseField using partial update
        TestCaseField partialUpdatedTestCaseField = new TestCaseField();
        partialUpdatedTestCaseField.setId(testCaseField.getId());

        partialUpdatedTestCaseField.expectedResult(UPDATED_EXPECTED_RESULT).value(UPDATED_VALUE);

        restTestCaseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseField))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeUpdate);
        TestCaseField testTestCaseField = testCaseFieldList.get(testCaseFieldList.size() - 1);
        assertThat(testTestCaseField.getExpectedResult()).isEqualTo(UPDATED_EXPECTED_RESULT);
        assertThat(testTestCaseField.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTestCaseField() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldRepository.findAll().size();
        testCaseField.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCaseField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseField))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCaseField() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldRepository.findAll().size();
        testCaseField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseField))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCaseField() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldRepository.findAll().size();
        testCaseField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testCaseField))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseField in the database
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCaseField() throws Exception {
        // Initialize the database
        testCaseFieldRepository.saveAndFlush(testCaseField);

        int databaseSizeBeforeDelete = testCaseFieldRepository.findAll().size();

        // Delete the testCaseField
        restTestCaseFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCaseField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCaseField> testCaseFieldList = testCaseFieldRepository.findAll();
        assertThat(testCaseFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
