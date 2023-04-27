package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.TestCaseField;
import com.venturedive.blaze.repository.TestCaseFieldRepository;
import com.venturedive.blaze.service.criteria.TestCaseFieldCriteria;
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
 * Service for executing complex queries for {@link TestCaseField} entities in the database.
 * The main input is a {@link TestCaseFieldCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCaseField} or a {@link Page} of {@link TestCaseField} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCaseFieldQueryService extends QueryService<TestCaseField> {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldQueryService.class);

    private final TestCaseFieldRepository testCaseFieldRepository;

    public TestCaseFieldQueryService(TestCaseFieldRepository testCaseFieldRepository) {
        this.testCaseFieldRepository = testCaseFieldRepository;
    }

    /**
     * Return a {@link List} of {@link TestCaseField} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCaseField> findByCriteria(TestCaseFieldCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCaseField> specification = createSpecification(criteria);
        return testCaseFieldRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCaseField} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseField> findByCriteria(TestCaseFieldCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCaseField> specification = createSpecification(criteria);
        return testCaseFieldRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCaseFieldCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCaseField> specification = createSpecification(criteria);
        return testCaseFieldRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCaseFieldCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCaseField> createSpecification(TestCaseFieldCriteria criteria) {
        Specification<TestCaseField> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCaseField_.id));
            }
            if (criteria.getExpectedResult() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExpectedResult(), TestCaseField_.expectedResult));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), TestCaseField_.value));
            }
            if (criteria.getTemplateFieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateFieldId(),
                            root -> root.join(TestCaseField_.templateField, JoinType.LEFT).get(TemplateField_.id)
                        )
                    );
            }
            if (criteria.getTestCaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestCaseId(),
                            root -> root.join(TestCaseField_.testCase, JoinType.LEFT).get(TestCase_.id)
                        )
                    );
            }
            if (criteria.getTestcasefieldattachmentTestcasefieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasefieldattachmentTestcasefieldId(),
                            root ->
                                root
                                    .join(TestCaseField_.testcasefieldattachmentTestcasefields, JoinType.LEFT)
                                    .get(TestCaseFieldAttachment_.id)
                        )
                    );
            }
            if (criteria.getTestrunstepdetailsStepdetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunstepdetailsStepdetailId(),
                            root -> root.join(TestCaseField_.testrunstepdetailsStepdetails, JoinType.LEFT).get(TestRunStepDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
