package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestPlan;
import com.venturedive.blaze.repository.TestPlanRepository;
import com.venturedive.blaze.service.criteria.TestPlanCriteria;
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
 * Service for executing complex queries for {@link TestPlan} entities in the database.
 * The main input is a {@link TestPlanCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestPlan} or a {@link Page} of {@link TestPlan} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestPlanQueryService extends QueryService<TestPlan> {

    private final Logger log = LoggerFactory.getLogger(TestPlanQueryService.class);

    private final TestPlanRepository testPlanRepository;

    public TestPlanQueryService(TestPlanRepository testPlanRepository) {
        this.testPlanRepository = testPlanRepository;
    }

    /**
     * Return a {@link List} of {@link TestPlan} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestPlan> findByCriteria(TestPlanCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestPlan> specification = createSpecification(criteria);
        return testPlanRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestPlan} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestPlan> findByCriteria(TestPlanCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestPlan> specification = createSpecification(criteria);
        return testPlanRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestPlanCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestPlan> specification = createSpecification(criteria);
        return testPlanRepository.count(specification);
    }

    /**
     * Function to convert {@link TestPlanCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestPlan> createSpecification(TestPlanCriteria criteria) {
        Specification<TestPlan> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestPlan_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TestPlan_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TestPlan_.description));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TestPlan_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TestPlan_.createdAt));
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(TestPlan_.project, JoinType.LEFT).get(Project_.id))
                    );
            }
        }
        return specification;
    }
}
