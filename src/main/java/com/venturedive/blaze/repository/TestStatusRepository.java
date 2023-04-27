package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestStatusRepository extends JpaRepository<TestStatus, Long>, JpaSpecificationExecutor<TestStatus> {}
