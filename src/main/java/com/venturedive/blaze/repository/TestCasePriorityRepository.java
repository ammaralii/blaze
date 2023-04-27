package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestCasePriority;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCasePriority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCasePriorityRepository extends JpaRepository<TestCasePriority, Long>, JpaSpecificationExecutor<TestCasePriority> {}
