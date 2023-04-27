package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestLevel;
import com.venturedive.blaze.repository.TestLevelRepository;
import com.venturedive.blaze.service.criteria.TestLevelCriteria;
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
 * Service for executing complex queries for {@link TestLevel} entities in the database.
 * The main input is a {@link TestLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestLevel} or a {@link Page} of {@link TestLevel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestLevelQueryService extends QueryService<TestLevel> {

    private final Logger log = LoggerFactory.getLogger(TestLevelQueryService.class);

    private final TestLevelRepository testLevelRepository;

    public TestLevelQueryService(TestLevelRepository testLevelRepository) {
        this.testLevelRepository = testLevelRepository;
    }

    /**
     * Return a {@link List} of {@link TestLevel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestLevel> findByCriteria(TestLevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestLevel> specification = createSpecification(criteria);
        return testLevelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestLevel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestLevel> findByCriteria(TestLevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestLevel> specification = createSpecification(criteria);
        return testLevelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestLevelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestLevel> specification = createSpecification(criteria);
        return testLevelRepository.count(specification);
    }

    /**
     * Function to convert {@link TestLevelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestLevel> createSpecification(TestLevelCriteria criteria) {
        Specification<TestLevel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestLevel_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TestLevel_.name));
            }
            if (criteria.getTestrunTestlevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunTestlevelId(),
                            root -> root.join(TestLevel_.testrunTestlevels, JoinType.LEFT).get(TestRun_.id)
                        )
                    );
            }
            if (criteria.getTestCaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestCaseId(),
                            root -> root.join(TestLevel_.testCases, JoinType.LEFT).get(TestCase_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
