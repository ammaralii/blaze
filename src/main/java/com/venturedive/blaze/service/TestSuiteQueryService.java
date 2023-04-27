package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestSuite;
import com.venturedive.blaze.repository.TestSuiteRepository;
import com.venturedive.blaze.service.criteria.TestSuiteCriteria;
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
 * Service for executing complex queries for {@link TestSuite} entities in the database.
 * The main input is a {@link TestSuiteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestSuite} or a {@link Page} of {@link TestSuite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestSuiteQueryService extends QueryService<TestSuite> {

    private final Logger log = LoggerFactory.getLogger(TestSuiteQueryService.class);

    private final TestSuiteRepository testSuiteRepository;

    public TestSuiteQueryService(TestSuiteRepository testSuiteRepository) {
        this.testSuiteRepository = testSuiteRepository;
    }

    /**
     * Return a {@link List} of {@link TestSuite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestSuite> findByCriteria(TestSuiteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestSuite> specification = createSpecification(criteria);
        return testSuiteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestSuite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestSuite> findByCriteria(TestSuiteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestSuite> specification = createSpecification(criteria);
        return testSuiteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestSuiteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestSuite> specification = createSpecification(criteria);
        return testSuiteRepository.count(specification);
    }

    /**
     * Function to convert {@link TestSuiteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestSuite> createSpecification(TestSuiteCriteria criteria) {
        Specification<TestSuite> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestSuite_.id));
            }
            if (criteria.getTestSuiteName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTestSuiteName(), TestSuite_.testSuiteName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TestSuite_.description));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TestSuite_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TestSuite_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), TestSuite_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), TestSuite_.updatedAt));
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(TestSuite_.project, JoinType.LEFT).get(Project_.id))
                    );
            }
            if (criteria.getSectionTestsuiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSectionTestsuiteId(),
                            root -> root.join(TestSuite_.sectionTestsuites, JoinType.LEFT).get(Section_.id)
                        )
                    );
            }
            if (criteria.getTestcaseTestsuiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcaseTestsuiteId(),
                            root -> root.join(TestSuite_.testcaseTestsuites, JoinType.LEFT).get(TestCase_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
