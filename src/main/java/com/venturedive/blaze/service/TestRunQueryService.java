package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestRun;
import com.venturedive.blaze.repository.TestRunRepository;
import com.venturedive.blaze.service.criteria.TestRunCriteria;
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
 * Service for executing complex queries for {@link TestRun} entities in the database.
 * The main input is a {@link TestRunCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestRun} or a {@link Page} of {@link TestRun} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestRunQueryService extends QueryService<TestRun> {

    private final Logger log = LoggerFactory.getLogger(TestRunQueryService.class);

    private final TestRunRepository testRunRepository;

    public TestRunQueryService(TestRunRepository testRunRepository) {
        this.testRunRepository = testRunRepository;
    }

    /**
     * Return a {@link List} of {@link TestRun} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestRun> findByCriteria(TestRunCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestRun> specification = createSpecification(criteria);
        return testRunRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestRun} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRun> findByCriteria(TestRunCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestRun> specification = createSpecification(criteria);
        return testRunRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestRunCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestRun> specification = createSpecification(criteria);
        return testRunRepository.count(specification);
    }

    /**
     * Function to convert {@link TestRunCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestRun> createSpecification(TestRunCriteria criteria) {
        Specification<TestRun> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestRun_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TestRun_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TestRun_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TestRun_.createdAt));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TestRun_.createdBy));
            }
            if (criteria.getTestLevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestLevelId(),
                            root -> root.join(TestRun_.testLevel, JoinType.LEFT).get(TestLevel_.id)
                        )
                    );
            }
            if (criteria.getMileStoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMileStoneId(),
                            root -> root.join(TestRun_.mileStone, JoinType.LEFT).get(Milestone_.id)
                        )
                    );
            }
            if (criteria.getTestrundetailsTestrunId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrundetailsTestrunId(),
                            root -> root.join(TestRun_.testrundetailsTestruns, JoinType.LEFT).get(TestRunDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
