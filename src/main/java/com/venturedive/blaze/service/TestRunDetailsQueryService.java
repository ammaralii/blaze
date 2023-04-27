package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestRunDetails;
import com.venturedive.blaze.repository.TestRunDetailsRepository;
import com.venturedive.blaze.service.criteria.TestRunDetailsCriteria;
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
 * Service for executing complex queries for {@link TestRunDetails} entities in the database.
 * The main input is a {@link TestRunDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestRunDetails} or a {@link Page} of {@link TestRunDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestRunDetailsQueryService extends QueryService<TestRunDetails> {

    private final Logger log = LoggerFactory.getLogger(TestRunDetailsQueryService.class);

    private final TestRunDetailsRepository testRunDetailsRepository;

    public TestRunDetailsQueryService(TestRunDetailsRepository testRunDetailsRepository) {
        this.testRunDetailsRepository = testRunDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link TestRunDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestRunDetails> findByCriteria(TestRunDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestRunDetails> specification = createSpecification(criteria);
        return testRunDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestRunDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunDetails> findByCriteria(TestRunDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestRunDetails> specification = createSpecification(criteria);
        return testRunDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestRunDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestRunDetails> specification = createSpecification(criteria);
        return testRunDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link TestRunDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestRunDetails> createSpecification(TestRunDetailsCriteria criteria) {
        Specification<TestRunDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestRunDetails_.id));
            }
            if (criteria.getResultDetail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResultDetail(), TestRunDetails_.resultDetail));
            }
            if (criteria.getJiraId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJiraId(), TestRunDetails_.jiraId));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TestRunDetails_.createdBy));
            }
            if (criteria.getExecutedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExecutedBy(), TestRunDetails_.executedBy));
            }
            if (criteria.getTestRunId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestRunId(),
                            root -> root.join(TestRunDetails_.testRun, JoinType.LEFT).get(TestRun_.id)
                        )
                    );
            }
            if (criteria.getTestCaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestCaseId(),
                            root -> root.join(TestRunDetails_.testCase, JoinType.LEFT).get(TestCase_.id)
                        )
                    );
            }
            if (criteria.getStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStatusId(),
                            root -> root.join(TestRunDetails_.status, JoinType.LEFT).get(TestStatus_.id)
                        )
                    );
            }
            if (criteria.getTestrundetailattachmentTestrundetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrundetailattachmentTestrundetailId(),
                            root ->
                                root
                                    .join(TestRunDetails_.testrundetailattachmentTestrundetails, JoinType.LEFT)
                                    .get(TestRunDetailAttachment_.id)
                        )
                    );
            }
            if (criteria.getTestrunstepdetailsTestrundetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunstepdetailsTestrundetailId(),
                            root -> root.join(TestRunDetails_.testrunstepdetailsTestrundetails, JoinType.LEFT).get(TestRunStepDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
