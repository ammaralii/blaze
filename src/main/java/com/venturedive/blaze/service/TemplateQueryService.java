package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.Template;
import com.venturedive.blaze.repository.TemplateRepository;
import com.venturedive.blaze.service.criteria.TemplateCriteria;
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
 * Service for executing complex queries for {@link Template} entities in the database.
 * The main input is a {@link TemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Template} or a {@link Page} of {@link Template} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemplateQueryService extends QueryService<Template> {

    private final Logger log = LoggerFactory.getLogger(TemplateQueryService.class);

    private final TemplateRepository templateRepository;

    public TemplateQueryService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    /**
     * Return a {@link List} of {@link Template} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Template> findByCriteria(TemplateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Template> specification = createSpecification(criteria);
        return templateRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Template} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Template> findByCriteria(TemplateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Template> specification = createSpecification(criteria);
        return templateRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Template> specification = createSpecification(criteria);
        return templateRepository.count(specification);
    }

    /**
     * Function to convert {@link TemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Template> createSpecification(TemplateCriteria criteria) {
        Specification<Template> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Template_.id));
            }
            if (criteria.getTemplateName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTemplateName(), Template_.templateName));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Template_.createdAt));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Template_.createdBy));
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompanyId(), root -> root.join(Template_.company, JoinType.LEFT).get(Company_.id))
                    );
            }
            if (criteria.getTemplateFieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateFieldId(),
                            root -> root.join(Template_.templateFields, JoinType.LEFT).get(TemplateField_.id)
                        )
                    );
            }
            if (criteria.getProjectDefaulttemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProjectDefaulttemplateId(),
                            root -> root.join(Template_.projectDefaulttemplates, JoinType.LEFT).get(Project_.id)
                        )
                    );
            }
            if (criteria.getTestcaseTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcaseTemplateId(),
                            root -> root.join(Template_.testcaseTemplates, JoinType.LEFT).get(TestCase_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
