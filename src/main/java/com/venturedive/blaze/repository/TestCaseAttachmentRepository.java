package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestCaseAttachment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCaseAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseAttachmentRepository
    extends JpaRepository<TestCaseAttachment, Long>, JpaSpecificationExecutor<TestCaseAttachment> {}
