package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestRunDetailAttachment;
import com.venturedive.blaze.repository.TestRunDetailAttachmentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestRunDetailAttachment}.
 */
@Service
@Transactional
public class TestRunDetailAttachmentService {

    private final Logger log = LoggerFactory.getLogger(TestRunDetailAttachmentService.class);

    private final TestRunDetailAttachmentRepository testRunDetailAttachmentRepository;

    public TestRunDetailAttachmentService(TestRunDetailAttachmentRepository testRunDetailAttachmentRepository) {
        this.testRunDetailAttachmentRepository = testRunDetailAttachmentRepository;
    }

    /**
     * Save a testRunDetailAttachment.
     *
     * @param testRunDetailAttachment the entity to save.
     * @return the persisted entity.
     */
    public TestRunDetailAttachment save(TestRunDetailAttachment testRunDetailAttachment) {
        log.debug("Request to save TestRunDetailAttachment : {}", testRunDetailAttachment);
        return testRunDetailAttachmentRepository.save(testRunDetailAttachment);
    }

    /**
     * Update a testRunDetailAttachment.
     *
     * @param testRunDetailAttachment the entity to save.
     * @return the persisted entity.
     */
    public TestRunDetailAttachment update(TestRunDetailAttachment testRunDetailAttachment) {
        log.debug("Request to update TestRunDetailAttachment : {}", testRunDetailAttachment);
        return testRunDetailAttachmentRepository.save(testRunDetailAttachment);
    }

    /**
     * Partially update a testRunDetailAttachment.
     *
     * @param testRunDetailAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestRunDetailAttachment> partialUpdate(TestRunDetailAttachment testRunDetailAttachment) {
        log.debug("Request to partially update TestRunDetailAttachment : {}", testRunDetailAttachment);

        return testRunDetailAttachmentRepository
            .findById(testRunDetailAttachment.getId())
            .map(existingTestRunDetailAttachment -> {
                if (testRunDetailAttachment.getUrl() != null) {
                    existingTestRunDetailAttachment.setUrl(testRunDetailAttachment.getUrl());
                }

                return existingTestRunDetailAttachment;
            })
            .map(testRunDetailAttachmentRepository::save);
    }

    /**
     * Get all the testRunDetailAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunDetailAttachment> findAll(Pageable pageable) {
        log.debug("Request to get all TestRunDetailAttachments");
        return testRunDetailAttachmentRepository.findAll(pageable);
    }

    /**
     * Get one testRunDetailAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestRunDetailAttachment> findOne(Long id) {
        log.debug("Request to get TestRunDetailAttachment : {}", id);
        return testRunDetailAttachmentRepository.findById(id);
    }

    /**
     * Delete the testRunDetailAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestRunDetailAttachment : {}", id);
        testRunDetailAttachmentRepository.deleteById(id);
    }
}
