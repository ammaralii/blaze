package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.Project;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {}
