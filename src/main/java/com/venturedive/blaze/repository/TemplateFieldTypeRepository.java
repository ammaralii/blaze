package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TemplateFieldType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TemplateFieldType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemplateFieldTypeRepository extends JpaRepository<TemplateFieldType, Long>, JpaSpecificationExecutor<TemplateFieldType> {}
