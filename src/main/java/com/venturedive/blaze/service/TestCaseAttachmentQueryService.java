package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestCaseAttachment;
import com.venturedive.blaze.repository.TestCaseAttachmentRepository;
import com.venturedive.blaze.service.criteria.TestCaseAttachmentCriteria;
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
 * Service for executing complex queries for {@link TestCaseAttachment} entities in the database.
 * The main input is a {@link TestCaseAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCaseAttachment} or a {@link Page} of {@link TestCaseAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCaseAttachmentQueryService extends QueryService<TestCaseAttachment> {

    private final Logger log = LoggerFactory.getLogger(TestCaseAttachmentQueryService.class);

    private final TestCaseAttachmentRepository testCaseAttachmentRepository;

    public TestCaseAttachmentQueryService(TestCaseAttachmentRepository testCaseAttachmentRepository) {
        this.testCaseAttachmentRepository = testCaseAttachmentRepository;
    }

    /**
     * Return a {@link List} of {@link TestCaseAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCaseAttachment> findByCriteria(TestCaseAttachmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCaseAttachment> specification = createSpecification(criteria);
        return testCaseAttachmentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCaseAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseAttachment> findByCriteria(TestCaseAttachmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCaseAttachment> specification = createSpecification(criteria);
        return testCaseAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCaseAttachmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCaseAttachment> specification = createSpecification(criteria);
        return testCaseAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCaseAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCaseAttachment> createSpecification(TestCaseAttachmentCriteria criteria) {
        Specification<TestCaseAttachment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCaseAttachment_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), TestCaseAttachment_.url));
            }
            if (criteria.getTestCaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestCaseId(),
                            root -> root.join(TestCaseAttachment_.testCase, JoinType.LEFT).get(TestCase_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
