package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestRun;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestRun entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestRunRepository extends JpaRepository<TestRun, Long>, JpaSpecificationExecutor<TestRun> {}
