package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.ApplicationUser;
import com.venturedive.blaze.repository.ApplicationUserRepository;
import com.venturedive.blaze.service.criteria.ApplicationUserCriteria;
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
 * Service for executing complex queries for {@link ApplicationUser} entities in the database.
 * The main input is a {@link ApplicationUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ApplicationUser} or a {@link Page} of {@link ApplicationUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationUserQueryService extends QueryService<ApplicationUser> {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserQueryService.class);

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserQueryService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     * Return a {@link List} of {@link ApplicationUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationUser> findByCriteria(ApplicationUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ApplicationUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationUser> findByCriteria(ApplicationUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicationUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ApplicationUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ApplicationUser> createSpecification(ApplicationUserCriteria criteria) {
        Specification<ApplicationUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ApplicationUser_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), ApplicationUser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), ApplicationUser_.lastName));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), ApplicationUser_.password));
            }
            if (criteria.getLastActive() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastActive(), ApplicationUser_.lastActive));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), ApplicationUser_.status));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), ApplicationUser_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), ApplicationUser_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), ApplicationUser_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), ApplicationUser_.updatedAt));
            }
            if (criteria.getUserEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserEmail(), ApplicationUser_.userEmail));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), ApplicationUser_.isDeleted));
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyId(),
                            root -> root.join(ApplicationUser_.company, JoinType.LEFT).get(Company_.id)
                        )
                    );
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProjectId(),
                            root -> root.join(ApplicationUser_.projects, JoinType.LEFT).get(Project_.id)
                        )
                    );
            }
            if (criteria.getRoleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRoleId(), root -> root.join(ApplicationUser_.roles, JoinType.LEFT).get(Role_.id))
                    );
            }
        }
        return specification;
    }
}
