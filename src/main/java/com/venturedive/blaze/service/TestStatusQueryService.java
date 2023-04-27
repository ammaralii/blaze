package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestStatus;
import com.venturedive.blaze.repository.TestStatusRepository;
import com.venturedive.blaze.service.criteria.TestStatusCriteria;
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
 * Service for executing complex queries for {@link TestStatus} entities in the database.
 * The main input is a {@link TestStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestStatus} or a {@link Page} of {@link TestStatus} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestStatusQueryService extends QueryService<TestStatus> {

    private final Logger log = LoggerFactory.getLogger(TestStatusQueryService.class);

    private final TestStatusRepository testStatusRepository;

    public TestStatusQueryService(TestStatusRepository testStatusRepository) {
        this.testStatusRepository = testStatusRepository;
    }

    /**
     * Return a {@link List} of {@link TestStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestStatus> findByCriteria(TestStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestStatus> specification = createSpecification(criteria);
        return testStatusRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestStatus> findByCriteria(TestStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestStatus> specification = createSpecification(criteria);
        return testStatusRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestStatus> specification = createSpecification(criteria);
        return testStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link TestStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestStatus> createSpecification(TestStatusCriteria criteria) {
        Specification<TestStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestStatus_.id));
            }
            if (criteria.getStatusName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatusName(), TestStatus_.statusName));
            }
            if (criteria.getTestrundetailsStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrundetailsStatusId(),
                            root -> root.join(TestStatus_.testrundetailsStatuses, JoinType.LEFT).get(TestRunDetails_.id)
                        )
                    );
            }
            if (criteria.getTestrunstepdetailsStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunstepdetailsStatusId(),
                            root -> root.join(TestStatus_.testrunstepdetailsStatuses, JoinType.LEFT).get(TestRunStepDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
