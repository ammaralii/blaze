package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestCaseFieldAttachment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCaseFieldAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseFieldAttachmentRepository
    extends JpaRepository<TestCaseFieldAttachment, Long>, JpaSpecificationExecutor<TestCaseFieldAttachment> {}
