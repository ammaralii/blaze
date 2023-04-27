package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestStatus;
import com.venturedive.blaze.repository.TestStatusRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestStatus}.
 */
@Service
@Transactional
public class TestStatusService {

    private final Logger log = LoggerFactory.getLogger(TestStatusService.class);

    private final TestStatusRepository testStatusRepository;

    public TestStatusService(TestStatusRepository testStatusRepository) {
        this.testStatusRepository = testStatusRepository;
    }

    /**
     * Save a testStatus.
     *
     * @param testStatus the entity to save.
     * @return the persisted entity.
     */
    public TestStatus save(TestStatus testStatus) {
        log.debug("Request to save TestStatus : {}", testStatus);
        return testStatusRepository.save(testStatus);
    }

    /**
     * Update a testStatus.
     *
     * @param testStatus the entity to save.
     * @return the persisted entity.
     */
    public TestStatus update(TestStatus testStatus) {
        log.debug("Request to update TestStatus : {}", testStatus);
        return testStatusRepository.save(testStatus);
    }

    /**
     * Partially update a testStatus.
     *
     * @param testStatus the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestStatus> partialUpdate(TestStatus testStatus) {
        log.debug("Request to partially update TestStatus : {}", testStatus);

        return testStatusRepository
            .findById(testStatus.getId())
            .map(existingTestStatus -> {
                if (testStatus.getStatusName() != null) {
                    existingTestStatus.setStatusName(testStatus.getStatusName());
                }

                return existingTestStatus;
            })
            .map(testStatusRepository::save);
    }

    /**
     * Get all the testStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestStatus> findAll(Pageable pageable) {
        log.debug("Request to get all TestStatuses");
        return testStatusRepository.findAll(pageable);
    }

    /**
     * Get one testStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestStatus> findOne(Long id) {
        log.debug("Request to get TestStatus : {}", id);
        return testStatusRepository.findById(id);
    }

    /**
     * Delete the testStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestStatus : {}", id);
        testStatusRepository.deleteById(id);
    }
}
