package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestRunStepDetailAttachment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestRunStepDetailAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestRunStepDetailAttachmentRepository
    extends JpaRepository<TestRunStepDetailAttachment, Long>, JpaSpecificationExecutor<TestRunStepDetailAttachment> {}
