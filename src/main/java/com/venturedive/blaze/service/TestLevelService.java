package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestLevel;
import com.venturedive.blaze.repository.TestLevelRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestLevel}.
 */
@Service
@Transactional
public class TestLevelService {

    private final Logger log = LoggerFactory.getLogger(TestLevelService.class);

    private final TestLevelRepository testLevelRepository;

    public TestLevelService(TestLevelRepository testLevelRepository) {
        this.testLevelRepository = testLevelRepository;
    }

    /**
     * Save a testLevel.
     *
     * @param testLevel the entity to save.
     * @return the persisted entity.
     */
    public TestLevel save(TestLevel testLevel) {
        log.debug("Request to save TestLevel : {}", testLevel);
        return testLevelRepository.save(testLevel);
    }

    /**
     * Update a testLevel.
     *
     * @param testLevel the entity to save.
     * @return the persisted entity.
     */
    public TestLevel update(TestLevel testLevel) {
        log.debug("Request to update TestLevel : {}", testLevel);
        return testLevelRepository.save(testLevel);
    }

    /**
     * Partially update a testLevel.
     *
     * @param testLevel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestLevel> partialUpdate(TestLevel testLevel) {
        log.debug("Request to partially update TestLevel : {}", testLevel);

        return testLevelRepository
            .findById(testLevel.getId())
            .map(existingTestLevel -> {
                if (testLevel.getName() != null) {
                    existingTestLevel.setName(testLevel.getName());
                }

                return existingTestLevel;
            })
            .map(testLevelRepository::save);
    }

    /**
     * Get all the testLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestLevel> findAll(Pageable pageable) {
        log.debug("Request to get all TestLevels");
        return testLevelRepository.findAll(pageable);
    }

    /**
     * Get one testLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestLevel> findOne(Long id) {
        log.debug("Request to get TestLevel : {}", id);
        return testLevelRepository.findById(id);
    }

    /**
     * Delete the testLevel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestLevel : {}", id);
        testLevelRepository.deleteById(id);
    }
}
