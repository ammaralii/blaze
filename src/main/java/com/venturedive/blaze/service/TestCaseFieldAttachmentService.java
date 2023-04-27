package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestCaseFieldAttachment;
import com.venturedive.blaze.repository.TestCaseFieldAttachmentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCaseFieldAttachment}.
 */
@Service
@Transactional
public class TestCaseFieldAttachmentService {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldAttachmentService.class);

    private final TestCaseFieldAttachmentRepository testCaseFieldAttachmentRepository;

    public TestCaseFieldAttachmentService(TestCaseFieldAttachmentRepository testCaseFieldAttachmentRepository) {
        this.testCaseFieldAttachmentRepository = testCaseFieldAttachmentRepository;
    }

    /**
     * Save a testCaseFieldAttachment.
     *
     * @param testCaseFieldAttachment the entity to save.
     * @return the persisted entity.
     */
    public TestCaseFieldAttachment save(TestCaseFieldAttachment testCaseFieldAttachment) {
        log.debug("Request to save TestCaseFieldAttachment : {}", testCaseFieldAttachment);
        return testCaseFieldAttachmentRepository.save(testCaseFieldAttachment);
    }

    /**
     * Update a testCaseFieldAttachment.
     *
     * @param testCaseFieldAttachment the entity to save.
     * @return the persisted entity.
     */
    public TestCaseFieldAttachment update(TestCaseFieldAttachment testCaseFieldAttachment) {
        log.debug("Request to update TestCaseFieldAttachment : {}", testCaseFieldAttachment);
        return testCaseFieldAttachmentRepository.save(testCaseFieldAttachment);
    }

    /**
     * Partially update a testCaseFieldAttachment.
     *
     * @param testCaseFieldAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCaseFieldAttachment> partialUpdate(TestCaseFieldAttachment testCaseFieldAttachment) {
        log.debug("Request to partially update TestCaseFieldAttachment : {}", testCaseFieldAttachment);

        return testCaseFieldAttachmentRepository
            .findById(testCaseFieldAttachment.getId())
            .map(existingTestCaseFieldAttachment -> {
                if (testCaseFieldAttachment.getUrl() != null) {
                    existingTestCaseFieldAttachment.setUrl(testCaseFieldAttachment.getUrl());
                }

                return existingTestCaseFieldAttachment;
            })
            .map(testCaseFieldAttachmentRepository::save);
    }

    /**
     * Get all the testCaseFieldAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseFieldAttachment> findAll(Pageable pageable) {
        log.debug("Request to get all TestCaseFieldAttachments");
        return testCaseFieldAttachmentRepository.findAll(pageable);
    }

    /**
     * Get one testCaseFieldAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCaseFieldAttachment> findOne(Long id) {
        log.debug("Request to get TestCaseFieldAttachment : {}", id);
        return testCaseFieldAttachmentRepository.findById(id);
    }

    /**
     * Delete the testCaseFieldAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCaseFieldAttachment : {}", id);
        testCaseFieldAttachmentRepository.deleteById(id);
    }
}
