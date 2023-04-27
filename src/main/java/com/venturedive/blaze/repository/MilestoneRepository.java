package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.Milestone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Milestone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long>, JpaSpecificationExecutor<Milestone> {}
