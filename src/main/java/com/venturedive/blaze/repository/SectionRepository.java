package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.Section;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Section entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectionRepository extends JpaRepository<Section, Long>, JpaSpecificationExecutor<Section> {}
