package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.Company;
import com.venturedive.blaze.repository.CompanyRepository;
import com.venturedive.blaze.service.criteria.CompanyCriteria;
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
 * Service for executing complex queries for {@link Company} entities in the database.
 * The main input is a {@link CompanyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Company} or a {@link Page} of {@link Company} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompanyQueryService extends QueryService<Company> {

    private final Logger log = LoggerFactory.getLogger(CompanyQueryService.class);

    private final CompanyRepository companyRepository;

    public CompanyQueryService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Return a {@link List} of {@link Company} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Company> findByCriteria(CompanyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Company} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Company> findByCriteria(CompanyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompanyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.count(specification);
    }

    /**
     * Function to convert {@link CompanyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Company> createSpecification(CompanyCriteria criteria) {
        Specification<Company> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Company_.id));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Company_.country));
            }
            if (criteria.getCompanyAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyAddress(), Company_.companyAddress));
            }
            if (criteria.getOrganization() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrganization(), Company_.organization));
            }
            if (criteria.getExpectedNoOfUsers() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpectedNoOfUsers(), Company_.expectedNoOfUsers));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Company_.createdAt));
            }
            if (criteria.getApplicationuserCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApplicationuserCompanyId(),
                            root -> root.join(Company_.applicationuserCompanies, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getProjectCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProjectCompanyId(),
                            root -> root.join(Company_.projectCompanies, JoinType.LEFT).get(Project_.id)
                        )
                    );
            }
            if (criteria.getTemplateCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateCompanyId(),
                            root -> root.join(Company_.templateCompanies, JoinType.LEFT).get(Template_.id)
                        )
                    );
            }
            if (criteria.getTemplatefieldCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplatefieldCompanyId(),
                            root -> root.join(Company_.templatefieldCompanies, JoinType.LEFT).get(TemplateField_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
