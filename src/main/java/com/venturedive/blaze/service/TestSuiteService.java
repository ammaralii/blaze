package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestSuite;
import com.venturedive.blaze.repository.TestSuiteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestSuite}.
 */
@Service
@Transactional
public class TestSuiteService {

    private final Logger log = LoggerFactory.getLogger(TestSuiteService.class);

    private final TestSuiteRepository testSuiteRepository;

    public TestSuiteService(TestSuiteRepository testSuiteRepository) {
        this.testSuiteRepository = testSuiteRepository;
    }

    /**
     * Save a testSuite.
     *
     * @param testSuite the entity to save.
     * @return the persisted entity.
     */
    public TestSuite save(TestSuite testSuite) {
        log.debug("Request to save TestSuite : {}", testSuite);
        return testSuiteRepository.save(testSuite);
    }

    /**
     * Update a testSuite.
     *
     * @param testSuite the entity to save.
     * @return the persisted entity.
     */
    public TestSuite update(TestSuite testSuite) {
        log.debug("Request to update TestSuite : {}", testSuite);
        return testSuiteRepository.save(testSuite);
    }

    /**
     * Partially update a testSuite.
     *
     * @param testSuite the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestSuite> partialUpdate(TestSuite testSuite) {
        log.debug("Request to partially update TestSuite : {}", testSuite);

        return testSuiteRepository
            .findById(testSuite.getId())
            .map(existingTestSuite -> {
                if (testSuite.getTestSuiteName() != null) {
                    existingTestSuite.setTestSuiteName(testSuite.getTestSuiteName());
                }
                if (testSuite.getDescription() != null) {
                    existingTestSuite.setDescription(testSuite.getDescription());
                }
                if (testSuite.getCreatedBy() != null) {
                    existingTestSuite.setCreatedBy(testSuite.getCreatedBy());
                }
                if (testSuite.getCreatedAt() != null) {
                    existingTestSuite.setCreatedAt(testSuite.getCreatedAt());
                }
                if (testSuite.getUpdatedBy() != null) {
                    existingTestSuite.setUpdatedBy(testSuite.getUpdatedBy());
                }
                if (testSuite.getUpdatedAt() != null) {
                    existingTestSuite.setUpdatedAt(testSuite.getUpdatedAt());
                }

                return existingTestSuite;
            })
            .map(testSuiteRepository::save);
    }

    /**
     * Get all the testSuites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestSuite> findAll(Pageable pageable) {
        log.debug("Request to get all TestSuites");
        return testSuiteRepository.findAll(pageable);
    }

    /**
     * Get one testSuite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestSuite> findOne(Long id) {
        log.debug("Request to get TestSuite : {}", id);
        return testSuiteRepository.findById(id);
    }

    /**
     * Delete the testSuite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestSuite : {}", id);
        testSuiteRepository.deleteById(id);
    }
}
