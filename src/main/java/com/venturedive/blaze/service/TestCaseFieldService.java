package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestCaseField;
import com.venturedive.blaze.repository.TestCaseFieldRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCaseField}.
 */
@Service
@Transactional
public class TestCaseFieldService {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldService.class);

    private final TestCaseFieldRepository testCaseFieldRepository;

    public TestCaseFieldService(TestCaseFieldRepository testCaseFieldRepository) {
        this.testCaseFieldRepository = testCaseFieldRepository;
    }

    /**
     * Save a testCaseField.
     *
     * @param testCaseField the entity to save.
     * @return the persisted entity.
     */
    public TestCaseField save(TestCaseField testCaseField) {
        log.debug("Request to save TestCaseField : {}", testCaseField);
        return testCaseFieldRepository.save(testCaseField);
    }

    /**
     * Update a testCaseField.
     *
     * @param testCaseField the entity to save.
     * @return the persisted entity.
     */
    public TestCaseField update(TestCaseField testCaseField) {
        log.debug("Request to update TestCaseField : {}", testCaseField);
        return testCaseFieldRepository.save(testCaseField);
    }

    /**
     * Partially update a testCaseField.
     *
     * @param testCaseField the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCaseField> partialUpdate(TestCaseField testCaseField) {
        log.debug("Request to partially update TestCaseField : {}", testCaseField);

        return testCaseFieldRepository
            .findById(testCaseField.getId())
            .map(existingTestCaseField -> {
                if (testCaseField.getExpectedResult() != null) {
                    existingTestCaseField.setExpectedResult(testCaseField.getExpectedResult());
                }
                if (testCaseField.getValue() != null) {
                    existingTestCaseField.setValue(testCaseField.getValue());
                }

                return existingTestCaseField;
            })
            .map(testCaseFieldRepository::save);
    }

    /**
     * Get all the testCaseFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseField> findAll(Pageable pageable) {
        log.debug("Request to get all TestCaseFields");
        return testCaseFieldRepository.findAll(pageable);
    }

    /**
     * Get one testCaseField by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCaseField> findOne(Long id) {
        log.debug("Request to get TestCaseField : {}", id);
        return testCaseFieldRepository.findById(id);
    }

    /**
     * Delete the testCaseField by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCaseField : {}", id);
        testCaseFieldRepository.deleteById(id);
    }
}
