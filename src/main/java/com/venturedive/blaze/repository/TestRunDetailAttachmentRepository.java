package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestRunDetailAttachment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestRunDetailAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestRunDetailAttachmentRepository
    extends JpaRepository<TestRunDetailAttachment, Long>, JpaSpecificationExecutor<TestRunDetailAttachment> {}
