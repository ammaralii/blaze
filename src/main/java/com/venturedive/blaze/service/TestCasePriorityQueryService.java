package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestCasePriority;
import com.venturedive.blaze.repository.TestCasePriorityRepository;
import com.venturedive.blaze.service.criteria.TestCasePriorityCriteria;
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
 * Service for executing complex queries for {@link TestCasePriority} entities in the database.
 * The main input is a {@link TestCasePriorityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCasePriority} or a {@link Page} of {@link TestCasePriority} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCasePriorityQueryService extends QueryService<TestCasePriority> {

    private final Logger log = LoggerFactory.getLogger(TestCasePriorityQueryService.class);

    private final TestCasePriorityRepository testCasePriorityRepository;

    public TestCasePriorityQueryService(TestCasePriorityRepository testCasePriorityRepository) {
        this.testCasePriorityRepository = testCasePriorityRepository;
    }

    /**
     * Return a {@link List} of {@link TestCasePriority} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCasePriority> findByCriteria(TestCasePriorityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCasePriority> specification = createSpecification(criteria);
        return testCasePriorityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCasePriority} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCasePriority> findByCriteria(TestCasePriorityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCasePriority> specification = createSpecification(criteria);
        return testCasePriorityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCasePriorityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCasePriority> specification = createSpecification(criteria);
        return testCasePriorityRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCasePriorityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCasePriority> createSpecification(TestCasePriorityCriteria criteria) {
        Specification<TestCasePriority> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCasePriority_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TestCasePriority_.name));
            }
            if (criteria.getTestcasePriorityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasePriorityId(),
                            root -> root.join(TestCasePriority_.testcasePriorities, JoinType.LEFT).get(TestCase_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
