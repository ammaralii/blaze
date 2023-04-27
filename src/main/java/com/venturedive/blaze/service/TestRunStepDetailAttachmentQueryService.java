package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestRunStepDetailAttachment;
import com.venturedive.blaze.repository.TestRunStepDetailAttachmentRepository;
import com.venturedive.blaze.service.criteria.TestRunStepDetailAttachmentCriteria;
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
 * Service for executing complex queries for {@link TestRunStepDetailAttachment} entities in the database.
 * The main input is a {@link TestRunStepDetailAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestRunStepDetailAttachment} or a {@link Page} of {@link TestRunStepDetailAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestRunStepDetailAttachmentQueryService extends QueryService<TestRunStepDetailAttachment> {

    private final Logger log = LoggerFactory.getLogger(TestRunStepDetailAttachmentQueryService.class);

    private final TestRunStepDetailAttachmentRepository testRunStepDetailAttachmentRepository;

    public TestRunStepDetailAttachmentQueryService(TestRunStepDetailAttachmentRepository testRunStepDetailAttachmentRepository) {
        this.testRunStepDetailAttachmentRepository = testRunStepDetailAttachmentRepository;
    }

    /**
     * Return a {@link List} of {@link TestRunStepDetailAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestRunStepDetailAttachment> findByCriteria(TestRunStepDetailAttachmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestRunStepDetailAttachment> specification = createSpecification(criteria);
        return testRunStepDetailAttachmentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestRunStepDetailAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunStepDetailAttachment> findByCriteria(TestRunStepDetailAttachmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestRunStepDetailAttachment> specification = createSpecification(criteria);
        return testRunStepDetailAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestRunStepDetailAttachmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestRunStepDetailAttachment> specification = createSpecification(criteria);
        return testRunStepDetailAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link TestRunStepDetailAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestRunStepDetailAttachment> createSpecification(TestRunStepDetailAttachmentCriteria criteria) {
        Specification<TestRunStepDetailAttachment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestRunStepDetailAttachment_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), TestRunStepDetailAttachment_.url));
            }
            if (criteria.getTestRunStepDetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestRunStepDetailId(),
                            root -> root.join(TestRunStepDetailAttachment_.testRunStepDetail, JoinType.LEFT).get(TestRunStepDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
