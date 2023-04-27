package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestRunDetailAttachment;
import com.venturedive.blaze.repository.TestRunDetailAttachmentRepository;
import com.venturedive.blaze.service.criteria.TestRunDetailAttachmentCriteria;
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
 * Service for executing complex queries for {@link TestRunDetailAttachment} entities in the database.
 * The main input is a {@link TestRunDetailAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestRunDetailAttachment} or a {@link Page} of {@link TestRunDetailAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestRunDetailAttachmentQueryService extends QueryService<TestRunDetailAttachment> {

    private final Logger log = LoggerFactory.getLogger(TestRunDetailAttachmentQueryService.class);

    private final TestRunDetailAttachmentRepository testRunDetailAttachmentRepository;

    public TestRunDetailAttachmentQueryService(TestRunDetailAttachmentRepository testRunDetailAttachmentRepository) {
        this.testRunDetailAttachmentRepository = testRunDetailAttachmentRepository;
    }

    /**
     * Return a {@link List} of {@link TestRunDetailAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestRunDetailAttachment> findByCriteria(TestRunDetailAttachmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestRunDetailAttachment> specification = createSpecification(criteria);
        return testRunDetailAttachmentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestRunDetailAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunDetailAttachment> findByCriteria(TestRunDetailAttachmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestRunDetailAttachment> specification = createSpecification(criteria);
        return testRunDetailAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestRunDetailAttachmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestRunDetailAttachment> specification = createSpecification(criteria);
        return testRunDetailAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link TestRunDetailAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestRunDetailAttachment> createSpecification(TestRunDetailAttachmentCriteria criteria) {
        Specification<TestRunDetailAttachment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestRunDetailAttachment_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), TestRunDetailAttachment_.url));
            }
            if (criteria.getTestRunDetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestRunDetailId(),
                            root -> root.join(TestRunDetailAttachment_.testRunDetail, JoinType.LEFT).get(TestRunDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
