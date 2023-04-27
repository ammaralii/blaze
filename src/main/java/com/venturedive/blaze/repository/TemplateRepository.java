package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.Template;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Template entity.
 *
 * When extending this class, extend TemplateRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TemplateRepository
    extends TemplateRepositoryWithBagRelationships, JpaRepository<Template, Long>, JpaSpecificationExecutor<Template> {
    default Optional<Template> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Template> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Template> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
