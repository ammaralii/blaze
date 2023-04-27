package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.Section;
import com.venturedive.blaze.repository.SectionRepository;
import com.venturedive.blaze.service.criteria.SectionCriteria;
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
 * Service for executing complex queries for {@link Section} entities in the database.
 * The main input is a {@link SectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Section} or a {@link Page} of {@link Section} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SectionQueryService extends QueryService<Section> {

    private final Logger log = LoggerFactory.getLogger(SectionQueryService.class);

    private final SectionRepository sectionRepository;

    public SectionQueryService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    /**
     * Return a {@link List} of {@link Section} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Section> findByCriteria(SectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Section> specification = createSpecification(criteria);
        return sectionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Section} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Section> findByCriteria(SectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Section> specification = createSpecification(criteria);
        return sectionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Section> specification = createSpecification(criteria);
        return sectionRepository.count(specification);
    }

    /**
     * Function to convert {@link SectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Section> createSpecification(SectionCriteria criteria) {
        Specification<Section> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Section_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Section_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Section_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Section_.createdAt));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Section_.createdBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Section_.updatedAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), Section_.updatedBy));
            }
            if (criteria.getTestSuiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestSuiteId(),
                            root -> root.join(Section_.testSuite, JoinType.LEFT).get(TestSuite_.id)
                        )
                    );
            }
            if (criteria.getParentSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentSectionId(),
                            root -> root.join(Section_.parentSection, JoinType.LEFT).get(Section_.id)
                        )
                    );
            }
            if (criteria.getSectionParentsectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSectionParentsectionId(),
                            root -> root.join(Section_.sectionParentsections, JoinType.LEFT).get(Section_.id)
                        )
                    );
            }
            if (criteria.getTestcaseSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcaseSectionId(),
                            root -> root.join(Section_.testcaseSections, JoinType.LEFT).get(TestCase_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
