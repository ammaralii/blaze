package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestRunDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestRunDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestRunDetailsRepository extends JpaRepository<TestRunDetails, Long>, JpaSpecificationExecutor<TestRunDetails> {}
