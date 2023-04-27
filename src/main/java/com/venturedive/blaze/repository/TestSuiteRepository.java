package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestSuite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestSuite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestSuiteRepository extends JpaRepository<TestSuite, Long>, JpaSpecificationExecutor<TestSuite> {}
