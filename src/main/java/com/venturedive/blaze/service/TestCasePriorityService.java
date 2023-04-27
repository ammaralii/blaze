package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestCasePriority;
import com.venturedive.blaze.repository.TestCasePriorityRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCasePriority}.
 */
@Service
@Transactional
public class TestCasePriorityService {

    private final Logger log = LoggerFactory.getLogger(TestCasePriorityService.class);

    private final TestCasePriorityRepository testCasePriorityRepository;

    public TestCasePriorityService(TestCasePriorityRepository testCasePriorityRepository) {
        this.testCasePriorityRepository = testCasePriorityRepository;
    }

    /**
     * Save a testCasePriority.
     *
     * @param testCasePriority the entity to save.
     * @return the persisted entity.
     */
    public TestCasePriority save(TestCasePriority testCasePriority) {
        log.debug("Request to save TestCasePriority : {}", testCasePriority);
        return testCasePriorityRepository.save(testCasePriority);
    }

    /**
     * Update a testCasePriority.
     *
     * @param testCasePriority the entity to save.
     * @return the persisted entity.
     */
    public TestCasePriority update(TestCasePriority testCasePriority) {
        log.debug("Request to update TestCasePriority : {}", testCasePriority);
        return testCasePriorityRepository.save(testCasePriority);
    }

    /**
     * Partially update a testCasePriority.
     *
     * @param testCasePriority the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCasePriority> partialUpdate(TestCasePriority testCasePriority) {
        log.debug("Request to partially update TestCasePriority : {}", testCasePriority);

        return testCasePriorityRepository
            .findById(testCasePriority.getId())
            .map(existingTestCasePriority -> {
                if (testCasePriority.getName() != null) {
                    existingTestCasePriority.setName(testCasePriority.getName());
                }

                return existingTestCasePriority;
            })
            .map(testCasePriorityRepository::save);
    }

    /**
     * Get all the testCasePriorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCasePriority> findAll(Pageable pageable) {
        log.debug("Request to get all TestCasePriorities");
        return testCasePriorityRepository.findAll(pageable);
    }

    /**
     * Get one testCasePriority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCasePriority> findOne(Long id) {
        log.debug("Request to get TestCasePriority : {}", id);
        return testCasePriorityRepository.findById(id);
    }

    /**
     * Delete the testCasePriority by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCasePriority : {}", id);
        testCasePriorityRepository.deleteById(id);
    }
}
