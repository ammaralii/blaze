package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.repository.TestCaseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCase}.
 */
@Service
@Transactional
public class TestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCaseService.class);

    private final TestCaseRepository testCaseRepository;

    public TestCaseService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    /**
     * Save a testCase.
     *
     * @param testCase the entity to save.
     * @return the persisted entity.
     */
    public TestCase save(TestCase testCase) {
        log.debug("Request to save TestCase : {}", testCase);
        return testCaseRepository.save(testCase);
    }

    /**
     * Update a testCase.
     *
     * @param testCase the entity to save.
     * @return the persisted entity.
     */
    public TestCase update(TestCase testCase) {
        log.debug("Request to update TestCase : {}", testCase);
        return testCaseRepository.save(testCase);
    }

    /**
     * Partially update a testCase.
     *
     * @param testCase the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCase> partialUpdate(TestCase testCase) {
        log.debug("Request to partially update TestCase : {}", testCase);

        return testCaseRepository
            .findById(testCase.getId())
            .map(existingTestCase -> {
                if (testCase.getTitle() != null) {
                    existingTestCase.setTitle(testCase.getTitle());
                }
                if (testCase.getEstimate() != null) {
                    existingTestCase.setEstimate(testCase.getEstimate());
                }
                if (testCase.getCreatedBy() != null) {
                    existingTestCase.setCreatedBy(testCase.getCreatedBy());
                }
                if (testCase.getCreatedAt() != null) {
                    existingTestCase.setCreatedAt(testCase.getCreatedAt());
                }
                if (testCase.getUpdatedBy() != null) {
                    existingTestCase.setUpdatedBy(testCase.getUpdatedBy());
                }
                if (testCase.getUpdatedAt() != null) {
                    existingTestCase.setUpdatedAt(testCase.getUpdatedAt());
                }
                if (testCase.getPrecondition() != null) {
                    existingTestCase.setPrecondition(testCase.getPrecondition());
                }
                if (testCase.getDescription() != null) {
                    existingTestCase.setDescription(testCase.getDescription());
                }
                if (testCase.getIsAutomated() != null) {
                    existingTestCase.setIsAutomated(testCase.getIsAutomated());
                }

                return existingTestCase;
            })
            .map(testCaseRepository::save);
    }

    /**
     * Get all the testCases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCase> findAll(Pageable pageable) {
        log.debug("Request to get all TestCases");
        return testCaseRepository.findAll(pageable);
    }

    /**
     * Get all the testCases with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TestCase> findAllWithEagerRelationships(Pageable pageable) {
        return testCaseRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one testCase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCase> findOne(Long id) {
        log.debug("Request to get TestCase : {}", id);
        return testCaseRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the testCase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCase : {}", id);
        testCaseRepository.deleteById(id);
    }
}
