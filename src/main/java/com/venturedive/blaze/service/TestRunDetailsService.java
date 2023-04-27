package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestRunDetails;
import com.venturedive.blaze.repository.TestRunDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestRunDetails}.
 */
@Service
@Transactional
public class TestRunDetailsService {

    private final Logger log = LoggerFactory.getLogger(TestRunDetailsService.class);

    private final TestRunDetailsRepository testRunDetailsRepository;

    public TestRunDetailsService(TestRunDetailsRepository testRunDetailsRepository) {
        this.testRunDetailsRepository = testRunDetailsRepository;
    }

    /**
     * Save a testRunDetails.
     *
     * @param testRunDetails the entity to save.
     * @return the persisted entity.
     */
    public TestRunDetails save(TestRunDetails testRunDetails) {
        log.debug("Request to save TestRunDetails : {}", testRunDetails);
        return testRunDetailsRepository.save(testRunDetails);
    }

    /**
     * Update a testRunDetails.
     *
     * @param testRunDetails the entity to save.
     * @return the persisted entity.
     */
    public TestRunDetails update(TestRunDetails testRunDetails) {
        log.debug("Request to update TestRunDetails : {}", testRunDetails);
        return testRunDetailsRepository.save(testRunDetails);
    }

    /**
     * Partially update a testRunDetails.
     *
     * @param testRunDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestRunDetails> partialUpdate(TestRunDetails testRunDetails) {
        log.debug("Request to partially update TestRunDetails : {}", testRunDetails);

        return testRunDetailsRepository
            .findById(testRunDetails.getId())
            .map(existingTestRunDetails -> {
                if (testRunDetails.getResultDetail() != null) {
                    existingTestRunDetails.setResultDetail(testRunDetails.getResultDetail());
                }
                if (testRunDetails.getJiraId() != null) {
                    existingTestRunDetails.setJiraId(testRunDetails.getJiraId());
                }
                if (testRunDetails.getCreatedBy() != null) {
                    existingTestRunDetails.setCreatedBy(testRunDetails.getCreatedBy());
                }
                if (testRunDetails.getExecutedBy() != null) {
                    existingTestRunDetails.setExecutedBy(testRunDetails.getExecutedBy());
                }

                return existingTestRunDetails;
            })
            .map(testRunDetailsRepository::save);
    }

    /**
     * Get all the testRunDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TestRunDetails");
        return testRunDetailsRepository.findAll(pageable);
    }

    /**
     * Get one testRunDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestRunDetails> findOne(Long id) {
        log.debug("Request to get TestRunDetails : {}", id);
        return testRunDetailsRepository.findById(id);
    }

    /**
     * Delete the testRunDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestRunDetails : {}", id);
        testRunDetailsRepository.deleteById(id);
    }
}
