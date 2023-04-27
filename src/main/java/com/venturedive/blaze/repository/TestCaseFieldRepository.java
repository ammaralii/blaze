package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestCaseField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCaseField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseFieldRepository extends JpaRepository<TestCaseField, Long>, JpaSpecificationExecutor<TestCaseField> {}
