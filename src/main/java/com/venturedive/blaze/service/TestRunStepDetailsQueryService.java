package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestRunStepDetails;
import com.venturedive.blaze.repository.TestRunStepDetailsRepository;
import com.venturedive.blaze.service.criteria.TestRunStepDetailsCriteria;
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
 * Service for executing complex queries for {@link TestRunStepDetails} entities in the database.
 * The main input is a {@link TestRunStepDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestRunStepDetails} or a {@link Page} of {@link TestRunStepDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestRunStepDetailsQueryService extends QueryService<TestRunStepDetails> {

    private final Logger log = LoggerFactory.getLogger(TestRunStepDetailsQueryService.class);

    private final TestRunStepDetailsRepository testRunStepDetailsRepository;

    public TestRunStepDetailsQueryService(TestRunStepDetailsRepository testRunStepDetailsRepository) {
        this.testRunStepDetailsRepository = testRunStepDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link TestRunStepDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestRunStepDetails> findByCriteria(TestRunStepDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestRunStepDetails> specification = createSpecification(criteria);
        return testRunStepDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestRunStepDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunStepDetails> findByCriteria(TestRunStepDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestRunStepDetails> specification = createSpecification(criteria);
        return testRunStepDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestRunStepDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestRunStepDetails> specification = createSpecification(criteria);
        return testRunStepDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link TestRunStepDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestRunStepDetails> createSpecification(TestRunStepDetailsCriteria criteria) {
        Specification<TestRunStepDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestRunStepDetails_.id));
            }
            if (criteria.getActualResult() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActualResult(), TestRunStepDetails_.actualResult));
            }
            if (criteria.getTestRunDetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestRunDetailId(),
                            root -> root.join(TestRunStepDetails_.testRunDetail, JoinType.LEFT).get(TestRunDetails_.id)
                        )
                    );
            }
            if (criteria.getStepDetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStepDetailId(),
                            root -> root.join(TestRunStepDetails_.stepDetail, JoinType.LEFT).get(TestCaseField_.id)
                        )
                    );
            }
            if (criteria.getStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStatusId(),
                            root -> root.join(TestRunStepDetails_.status, JoinType.LEFT).get(TestStatus_.id)
                        )
                    );
            }
            if (criteria.getTestrunstepdetailattachmentTestrunstepdetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunstepdetailattachmentTestrunstepdetailId(),
                            root ->
                                root
                                    .join(TestRunStepDetails_.testrunstepdetailattachmentTestrunstepdetails, JoinType.LEFT)
                                    .get(TestRunStepDetailAttachment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
