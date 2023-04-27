package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestRunStepDetails;
import com.venturedive.blaze.repository.TestRunStepDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestRunStepDetails}.
 */
@Service
@Transactional
public class TestRunStepDetailsService {

    private final Logger log = LoggerFactory.getLogger(TestRunStepDetailsService.class);

    private final TestRunStepDetailsRepository testRunStepDetailsRepository;

    public TestRunStepDetailsService(TestRunStepDetailsRepository testRunStepDetailsRepository) {
        this.testRunStepDetailsRepository = testRunStepDetailsRepository;
    }

    /**
     * Save a testRunStepDetails.
     *
     * @param testRunStepDetails the entity to save.
     * @return the persisted entity.
     */
    public TestRunStepDetails save(TestRunStepDetails testRunStepDetails) {
        log.debug("Request to save TestRunStepDetails : {}", testRunStepDetails);
        return testRunStepDetailsRepository.save(testRunStepDetails);
    }

    /**
     * Update a testRunStepDetails.
     *
     * @param testRunStepDetails the entity to save.
     * @return the persisted entity.
     */
    public TestRunStepDetails update(TestRunStepDetails testRunStepDetails) {
        log.debug("Request to update TestRunStepDetails : {}", testRunStepDetails);
        return testRunStepDetailsRepository.save(testRunStepDetails);
    }

    /**
     * Partially update a testRunStepDetails.
     *
     * @param testRunStepDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestRunStepDetails> partialUpdate(TestRunStepDetails testRunStepDetails) {
        log.debug("Request to partially update TestRunStepDetails : {}", testRunStepDetails);

        return testRunStepDetailsRepository
            .findById(testRunStepDetails.getId())
            .map(existingTestRunStepDetails -> {
                if (testRunStepDetails.getActualResult() != null) {
                    existingTestRunStepDetails.setActualResult(testRunStepDetails.getActualResult());
                }

                return existingTestRunStepDetails;
            })
            .map(testRunStepDetailsRepository::save);
    }

    /**
     * Get all the testRunStepDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunStepDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TestRunStepDetails");
        return testRunStepDetailsRepository.findAll(pageable);
    }

    /**
     * Get one testRunStepDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestRunStepDetails> findOne(Long id) {
        log.debug("Request to get TestRunStepDetails : {}", id);
        return testRunStepDetailsRepository.findById(id);
    }

    /**
     * Delete the testRunStepDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestRunStepDetails : {}", id);
        testRunStepDetailsRepository.deleteById(id);
    }
}
