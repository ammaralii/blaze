package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TestCaseAttachment;
import com.venturedive.blaze.repository.TestCaseAttachmentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCaseAttachment}.
 */
@Service
@Transactional
public class TestCaseAttachmentService {

    private final Logger log = LoggerFactory.getLogger(TestCaseAttachmentService.class);

    private final TestCaseAttachmentRepository testCaseAttachmentRepository;

    public TestCaseAttachmentService(TestCaseAttachmentRepository testCaseAttachmentRepository) {
        this.testCaseAttachmentRepository = testCaseAttachmentRepository;
    }

    /**
     * Save a testCaseAttachment.
     *
     * @param testCaseAttachment the entity to save.
     * @return the persisted entity.
     */
    public TestCaseAttachment save(TestCaseAttachment testCaseAttachment) {
        log.debug("Request to save TestCaseAttachment : {}", testCaseAttachment);
        return testCaseAttachmentRepository.save(testCaseAttachment);
    }

    /**
     * Update a testCaseAttachment.
     *
     * @param testCaseAttachment the entity to save.
     * @return the persisted entity.
     */
    public TestCaseAttachment update(TestCaseAttachment testCaseAttachment) {
        log.debug("Request to update TestCaseAttachment : {}", testCaseAttachment);
        return testCaseAttachmentRepository.save(testCaseAttachment);
    }

    /**
     * Partially update a testCaseAttachment.
     *
     * @param testCaseAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCaseAttachment> partialUpdate(TestCaseAttachment testCaseAttachment) {
        log.debug("Request to partially update TestCaseAttachment : {}", testCaseAttachment);

        return testCaseAttachmentRepository
            .findById(testCaseAttachment.getId())
            .map(existingTestCaseAttachment -> {
                if (testCaseAttachment.getUrl() != null) {
                    existingTestCaseAttachment.setUrl(testCaseAttachment.getUrl());
                }

                return existingTestCaseAttachment;
            })
            .map(testCaseAttachmentRepository::save);
    }

    /**
     * Get all the testCaseAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseAttachment> findAll(Pageable pageable) {
        log.debug("Request to get all TestCaseAttachments");
        return testCaseAttachmentRepository.findAll(pageable);
    }

    /**
     * Get one testCaseAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCaseAttachment> findOne(Long id) {
        log.debug("Request to get TestCaseAttachment : {}", id);
        return testCaseAttachmentRepository.findById(id);
    }

    /**
     * Delete the testCaseAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCaseAttachment : {}", id);
        testCaseAttachmentRepository.deleteById(id);
    }
}
