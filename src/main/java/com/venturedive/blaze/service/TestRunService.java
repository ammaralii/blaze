package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestRun;
import com.venturedive.blaze.repository.TestRunRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestRun}.
 */
@Service
@Transactional
public class TestRunService {

    private final Logger log = LoggerFactory.getLogger(TestRunService.class);

    private final TestRunRepository testRunRepository;

    public TestRunService(TestRunRepository testRunRepository) {
        this.testRunRepository = testRunRepository;
    }

    /**
     * Save a testRun.
     *
     * @param testRun the entity to save.
     * @return the persisted entity.
     */
    public TestRun save(TestRun testRun) {
        log.debug("Request to save TestRun : {}", testRun);
        return testRunRepository.save(testRun);
    }

    /**
     * Update a testRun.
     *
     * @param testRun the entity to save.
     * @return the persisted entity.
     */
    public TestRun update(TestRun testRun) {
        log.debug("Request to update TestRun : {}", testRun);
        return testRunRepository.save(testRun);
    }

    /**
     * Partially update a testRun.
     *
     * @param testRun the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestRun> partialUpdate(TestRun testRun) {
        log.debug("Request to partially update TestRun : {}", testRun);

        return testRunRepository
            .findById(testRun.getId())
            .map(existingTestRun -> {
                if (testRun.getName() != null) {
                    existingTestRun.setName(testRun.getName());
                }
                if (testRun.getDescription() != null) {
                    existingTestRun.setDescription(testRun.getDescription());
                }
                if (testRun.getCreatedAt() != null) {
                    existingTestRun.setCreatedAt(testRun.getCreatedAt());
                }
                if (testRun.getCreatedBy() != null) {
                    existingTestRun.setCreatedBy(testRun.getCreatedBy());
                }

                return existingTestRun;
            })
            .map(testRunRepository::save);
    }

    /**
     * Get all the testRuns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRun> findAll(Pageable pageable) {
        log.debug("Request to get all TestRuns");
        return testRunRepository.findAll(pageable);
    }

    /**
     * Get one testRun by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestRun> findOne(Long id) {
        log.debug("Request to get TestRun : {}", id);
        return testRunRepository.findById(id);
    }

    /**
     * Delete the testRun by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestRun : {}", id);
        testRunRepository.deleteById(id);
    }
}
