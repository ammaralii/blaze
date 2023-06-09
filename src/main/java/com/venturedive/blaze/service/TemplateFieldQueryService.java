package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TemplateField;
import com.venturedive.blaze.repository.TemplateFieldRepository;
import com.venturedive.blaze.service.criteria.TemplateFieldCriteria;
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
 * Service for executing complex queries for {@link TemplateField} entities in the database.
 * The main input is a {@link TemplateFieldCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TemplateField} or a {@link Page} of {@link TemplateField} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemplateFieldQueryService extends QueryService<TemplateField> {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldQueryService.class);

    private final TemplateFieldRepository templateFieldRepository;

    public TemplateFieldQueryService(TemplateFieldRepository templateFieldRepository) {
        this.templateFieldRepository = templateFieldRepository;
    }

    /**
     * Return a {@link List} of {@link TemplateField} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TemplateField> findByCriteria(TemplateFieldCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TemplateField> specification = createSpecification(criteria);
        return templateFieldRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TemplateField} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TemplateField> findByCriteria(TemplateFieldCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TemplateField> specification = createSpecification(criteria);
        return templateFieldRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemplateFieldCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TemplateField> specification = createSpecification(criteria);
        return templateFieldRepository.count(specification);
    }

    /**
     * Function to convert {@link TemplateFieldCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TemplateField> createSpecification(TemplateFieldCriteria criteria) {
        Specification<TemplateField> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TemplateField_.id));
            }
            if (criteria.getFieldName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFieldName(), TemplateField_.fieldName));
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyId(),
                            root -> root.join(TemplateField_.company, JoinType.LEFT).get(Company_.id)
                        )
                    );
            }
            if (criteria.getTemplateFieldTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateFieldTypeId(),
                            root -> root.join(TemplateField_.templateFieldType, JoinType.LEFT).get(TemplateFieldType_.id)
                        )
                    );
            }
            if (criteria.getTestcasefieldTemplatefieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasefieldTemplatefieldId(),
                            root -> root.join(TemplateField_.testcasefieldTemplatefields, JoinType.LEFT).get(TestCaseField_.id)
                        )
                    );
            }
            if (criteria.getTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateId(),
                            root -> root.join(TemplateField_.templates, JoinType.LEFT).get(Template_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
