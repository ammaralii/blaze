package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestRunStepDetailAttachment;
import com.venturedive.blaze.repository.TestRunStepDetailAttachmentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestRunStepDetailAttachment}.
 */
@Service
@Transactional
public class TestRunStepDetailAttachmentService {

    private final Logger log = LoggerFactory.getLogger(TestRunStepDetailAttachmentService.class);

    private final TestRunStepDetailAttachmentRepository testRunStepDetailAttachmentRepository;

    public TestRunStepDetailAttachmentService(TestRunStepDetailAttachmentRepository testRunStepDetailAttachmentRepository) {
        this.testRunStepDetailAttachmentRepository = testRunStepDetailAttachmentRepository;
    }

    /**
     * Save a testRunStepDetailAttachment.
     *
     * @param testRunStepDetailAttachment the entity to save.
     * @return the persisted entity.
     */
    public TestRunStepDetailAttachment save(TestRunStepDetailAttachment testRunStepDetailAttachment) {
        log.debug("Request to save TestRunStepDetailAttachment : {}", testRunStepDetailAttachment);
        return testRunStepDetailAttachmentRepository.save(testRunStepDetailAttachment);
    }

    /**
     * Update a testRunStepDetailAttachment.
     *
     * @param testRunStepDetailAttachment the entity to save.
     * @return the persisted entity.
     */
    public TestRunStepDetailAttachment update(TestRunStepDetailAttachment testRunStepDetailAttachment) {
        log.debug("Request to update TestRunStepDetailAttachment : {}", testRunStepDetailAttachment);
        return testRunStepDetailAttachmentRepository.save(testRunStepDetailAttachment);
    }

    /**
     * Partially update a testRunStepDetailAttachment.
     *
     * @param testRunStepDetailAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestRunStepDetailAttachment> partialUpdate(TestRunStepDetailAttachment testRunStepDetailAttachment) {
        log.debug("Request to partially update TestRunStepDetailAttachment : {}", testRunStepDetailAttachment);

        return testRunStepDetailAttachmentRepository
            .findById(testRunStepDetailAttachment.getId())
            .map(existingTestRunStepDetailAttachment -> {
                if (testRunStepDetailAttachment.getUrl() != null) {
                    existingTestRunStepDetailAttachment.setUrl(testRunStepDetailAttachment.getUrl());
                }

                return existingTestRunStepDetailAttachment;
            })
            .map(testRunStepDetailAttachmentRepository::save);
    }

    /**
     * Get all the testRunStepDetailAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunStepDetailAttachment> findAll(Pageable pageable) {
        log.debug("Request to get all TestRunStepDetailAttachments");
        return testRunStepDetailAttachmentRepository.findAll(pageable);
    }

    /**
     * Get one testRunStepDetailAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestRunStepDetailAttachment> findOne(Long id) {
        log.debug("Request to get TestRunStepDetailAttachment : {}", id);
        return testRunStepDetailAttachmentRepository.findById(id);
    }

    /**
     * Delete the testRunStepDetailAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestRunStepDetailAttachment : {}", id);
        testRunStepDetailAttachmentRepository.deleteById(id);
    }
}
