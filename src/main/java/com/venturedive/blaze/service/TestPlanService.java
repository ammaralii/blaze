package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestPlan;
import com.venturedive.blaze.repository.TestPlanRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestPlan}.
 */
@Service
@Transactional
public class TestPlanService {

    private final Logger log = LoggerFactory.getLogger(TestPlanService.class);

    private final TestPlanRepository testPlanRepository;

    public TestPlanService(TestPlanRepository testPlanRepository) {
        this.testPlanRepository = testPlanRepository;
    }

    /**
     * Save a testPlan.
     *
     * @param testPlan the entity to save.
     * @return the persisted entity.
     */
    public TestPlan save(TestPlan testPlan) {
        log.debug("Request to save TestPlan : {}", testPlan);
        return testPlanRepository.save(testPlan);
    }

    /**
     * Update a testPlan.
     *
     * @param testPlan the entity to save.
     * @return the persisted entity.
     */
    public TestPlan update(TestPlan testPlan) {
        log.debug("Request to update TestPlan : {}", testPlan);
        return testPlanRepository.save(testPlan);
    }

    /**
     * Partially update a testPlan.
     *
     * @param testPlan the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestPlan> partialUpdate(TestPlan testPlan) {
        log.debug("Request to partially update TestPlan : {}", testPlan);

        return testPlanRepository
            .findById(testPlan.getId())
            .map(existingTestPlan -> {
                if (testPlan.getName() != null) {
                    existingTestPlan.setName(testPlan.getName());
                }
                if (testPlan.getDescription() != null) {
                    existingTestPlan.setDescription(testPlan.getDescription());
                }
                if (testPlan.getCreatedBy() != null) {
                    existingTestPlan.setCreatedBy(testPlan.getCreatedBy());
                }
                if (testPlan.getCreatedAt() != null) {
                    existingTestPlan.setCreatedAt(testPlan.getCreatedAt());
                }

                return existingTestPlan;
            })
            .map(testPlanRepository::save);
    }

    /**
     * Get all the testPlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestPlan> findAll(Pageable pageable) {
        log.debug("Request to get all TestPlans");
        return testPlanRepository.findAll(pageable);
    }

    /**
     * Get one testPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestPlan> findOne(Long id) {
        log.debug("Request to get TestPlan : {}", id);
        return testPlanRepository.findById(id);
    }

    /**
     * Delete the testPlan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestPlan : {}", id);
        testPlanRepository.deleteById(id);
    }
}
