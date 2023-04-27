package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TemplateField;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TemplateField entity.
 */
@Repository
public interface TemplateFieldRepository extends JpaRepository<TemplateField, Long>, JpaSpecificationExecutor<TemplateField> {
    default Optional<TemplateField> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TemplateField> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TemplateField> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct templateField from TemplateField templateField left join fetch templateField.templateFieldType",
        countQuery = "select count(distinct templateField) from TemplateField templateField"
    )
    Page<TemplateField> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct templateField from TemplateField templateField left join fetch templateField.templateFieldType")
    List<TemplateField> findAllWithToOneRelationships();

    @Query(
        "select templateField from TemplateField templateField left join fetch templateField.templateFieldType where templateField.id =:id"
    )
    Optional<TemplateField> findOneWithToOneRelationships(@Param("id") Long id);
}
