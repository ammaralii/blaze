package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TemplateFieldType;
import com.venturedive.blaze.repository.TemplateFieldTypeRepository;
import com.venturedive.blaze.service.criteria.TemplateFieldTypeCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TemplateFieldType} entities in the database.
 * The main input is a {@link TemplateFieldTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TemplateFieldType} or a {@link Page} of {@link TemplateFieldType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemplateFieldTypeQueryService extends QueryService<TemplateFieldType> {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldTypeQueryService.class);

    private final TemplateFieldTypeRepository templateFieldTypeRepository;

    public TemplateFieldTypeQueryService(TemplateFieldTypeRepository templateFieldTypeRepository) {
        this.templateFieldTypeRepository = templateFieldTypeRepository;
    }

    /**
     * Return a {@link List} of {@link TemplateFieldType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TemplateFieldType> findByCriteria(TemplateFieldTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TemplateFieldType> specification = createSpecification(criteria);
        return templateFieldTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TemplateFieldType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TemplateFieldType> findByCriteria(TemplateFieldTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TemplateFieldType> specification = createSpecification(criteria);
        return templateFieldTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemplateFieldTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TemplateFieldType> specification = createSpecification(criteria);
        return templateFieldTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link TemplateFieldTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TemplateFieldType> createSpecification(TemplateFieldTypeCriteria criteria) {
        Specification<TemplateFieldType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TemplateFieldType_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), TemplateFieldType_.type));
            }
            if (criteria.getIsList() != null) {
                specification = specification.and(buildSpecification(criteria.getIsList(), TemplateFieldType_.isList));
            }
            if (criteria.getAttachments() != null) {
                specification = specification.and(buildSpecification(criteria.getAttachments(), TemplateFieldType_.attachments));
            }
            if (criteria.getTemplatefieldTemplatefieldtypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplatefieldTemplatefieldtypeId(),
                            root -> root.join(TemplateFieldType_.templatefieldTemplatefieldtypes, JoinType.LEFT).get(TemplateField_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
