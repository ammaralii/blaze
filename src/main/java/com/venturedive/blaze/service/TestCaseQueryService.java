package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.repository.TestCaseRepository;
import com.venturedive.blaze.service.criteria.TestCaseCriteria;
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
 * Service for executing complex queries for {@link TestCase} entities in the database.
 * The main input is a {@link TestCaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCase} or a {@link Page} of {@link TestCase} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCaseQueryService extends QueryService<TestCase> {

    private final Logger log = LoggerFactory.getLogger(TestCaseQueryService.class);

    private final TestCaseRepository testCaseRepository;

    public TestCaseQueryService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    /**
     * Return a {@link List} of {@link TestCase} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCase> findByCriteria(TestCaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCase> specification = createSpecification(criteria);
        return testCaseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCase} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCase> findByCriteria(TestCaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCase> specification = createSpecification(criteria);
        return testCaseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCaseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCase> specification = createSpecification(criteria);
        return testCaseRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCaseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCase> createSpecification(TestCaseCriteria criteria) {
        Specification<TestCase> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCase_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), TestCase_.title));
            }
            if (criteria.getEstimate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstimate(), TestCase_.estimate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TestCase_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TestCase_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), TestCase_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), TestCase_.updatedAt));
            }
            if (criteria.getPrecondition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrecondition(), TestCase_.precondition));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TestCase_.description));
            }
            if (criteria.getIsAutomated() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAutomated(), TestCase_.isAutomated));
            }
            if (criteria.getTestSuiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestSuiteId(),
                            root -> root.join(TestCase_.testSuite, JoinType.LEFT).get(TestSuite_.id)
                        )
                    );
            }
            if (criteria.getSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSectionId(), root -> root.join(TestCase_.section, JoinType.LEFT).get(Section_.id))
                    );
            }
            if (criteria.getPriorityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPriorityId(),
                            root -> root.join(TestCase_.priority, JoinType.LEFT).get(TestCasePriority_.id)
                        )
                    );
            }
            if (criteria.getTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTemplateId(), root -> root.join(TestCase_.template, JoinType.LEFT).get(Template_.id))
                    );
            }
            if (criteria.getMilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMilestoneId(),
                            root -> root.join(TestCase_.milestone, JoinType.LEFT).get(Milestone_.id)
                        )
                    );
            }
            if (criteria.getTestLevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestLevelId(),
                            root -> root.join(TestCase_.testLevels, JoinType.LEFT).get(TestLevel_.id)
                        )
                    );
            }
            if (criteria.getTestcaseattachmentTestcaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcaseattachmentTestcaseId(),
                            root -> root.join(TestCase_.testcaseattachmentTestcases, JoinType.LEFT).get(TestCaseAttachment_.id)
                        )
                    );
            }
            if (criteria.getTestcasefieldTestcaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasefieldTestcaseId(),
                            root -> root.join(TestCase_.testcasefieldTestcases, JoinType.LEFT).get(TestCaseField_.id)
                        )
                    );
            }
            if (criteria.getTestrundetailsTestcaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrundetailsTestcaseId(),
                            root -> root.join(TestCase_.testrundetailsTestcases, JoinType.LEFT).get(TestRunDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
