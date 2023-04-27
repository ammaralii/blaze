package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestPlanRepository extends JpaRepository<TestPlan, Long>, JpaSpecificationExecutor<TestPlan> {}
