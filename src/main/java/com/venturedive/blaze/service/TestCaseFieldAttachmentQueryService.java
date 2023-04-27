package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestCaseFieldAttachment;
import com.venturedive.blaze.repository.TestCaseFieldAttachmentRepository;
import com.venturedive.blaze.service.criteria.TestCaseFieldAttachmentCriteria;
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
 * Service for executing complex queries for {@link TestCaseFieldAttachment} entities in the database.
 * The main input is a {@link TestCaseFieldAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCaseFieldAttachment} or a {@link Page} of {@link TestCaseFieldAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCaseFieldAttachmentQueryService extends QueryService<TestCaseFieldAttachment> {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldAttachmentQueryService.class);

    private final TestCaseFieldAttachmentRepository testCaseFieldAttachmentRepository;

    public TestCaseFieldAttachmentQueryService(TestCaseFieldAttachmentRepository testCaseFieldAttachmentRepository) {
        this.testCaseFieldAttachmentRepository = testCaseFieldAttachmentRepository;
    }

    /**
     * Return a {@link List} of {@link TestCaseFieldAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCaseFieldAttachment> findByCriteria(TestCaseFieldAttachmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCaseFieldAttachment> specification = createSpecification(criteria);
        return testCaseFieldAttachmentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCaseFieldAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseFieldAttachment> findByCriteria(TestCaseFieldAttachmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCaseFieldAttachment> specification = createSpecification(criteria);
        return testCaseFieldAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCaseFieldAttachmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCaseFieldAttachment> specification = createSpecification(criteria);
        return testCaseFieldAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCaseFieldAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCaseFieldAttachment> createSpecification(TestCaseFieldAttachmentCriteria criteria) {
        Specification<TestCaseFieldAttachment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCaseFieldAttachment_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), TestCaseFieldAttachment_.url));
            }
            if (criteria.getTestCaseFieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestCaseFieldId(),
                            root -> root.join(TestCaseFieldAttachment_.testCaseField, JoinType.LEFT).get(TestCaseField_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
