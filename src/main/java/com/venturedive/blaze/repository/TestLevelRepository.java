package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestLevelRepository extends JpaRepository<TestLevel, Long>, JpaSpecificationExecutor<TestLevel> {}
