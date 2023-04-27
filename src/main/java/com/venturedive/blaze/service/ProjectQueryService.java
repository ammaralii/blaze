package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.Project;
import com.venturedive.blaze.repository.ProjectRepository;
import com.venturedive.blaze.service.criteria.ProjectCriteria;
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
 * Service for executing complex queries for {@link Project} entities in the database.
 * The main input is a {@link ProjectCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Project} or a {@link Page} of {@link Project} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectQueryService extends QueryService<Project> {

    private final Logger log = LoggerFactory.getLogger(ProjectQueryService.class);

    private final ProjectRepository projectRepository;

    public ProjectQueryService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Return a {@link List} of {@link Project} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Project> findByCriteria(ProjectCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Project> specification = createSpecification(criteria);
        return projectRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Project} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Project> findByCriteria(ProjectCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Project> specification = createSpecification(criteria);
        return projectRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProjectCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Project> specification = createSpecification(criteria);
        return projectRepository.count(specification);
    }

    /**
     * Function to convert {@link ProjectCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Project> createSpecification(ProjectCriteria criteria) {
        Specification<Project> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Project_.id));
            }
            if (criteria.getProjectName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectName(), Project_.projectName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Project_.description));
            }
            if (criteria.getIsactive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsactive(), Project_.isactive));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Project_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Project_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), Project_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Project_.updatedAt));
            }
            if (criteria.getDefaultTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDefaultTemplateId(),
                            root -> root.join(Project_.defaultTemplate, JoinType.LEFT).get(Template_.id)
                        )
                    );
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompanyId(), root -> root.join(Project_.company, JoinType.LEFT).get(Company_.id))
                    );
            }
            if (criteria.getMilestoneProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMilestoneProjectId(),
                            root -> root.join(Project_.milestoneProjects, JoinType.LEFT).get(Milestone_.id)
                        )
                    );
            }
            if (criteria.getTestplanProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestplanProjectId(),
                            root -> root.join(Project_.testplanProjects, JoinType.LEFT).get(TestPlan_.id)
                        )
                    );
            }
            if (criteria.getTestsuiteProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestsuiteProjectId(),
                            root -> root.join(Project_.testsuiteProjects, JoinType.LEFT).get(TestSuite_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Project_.users, JoinType.LEFT).get(ApplicationUser_.id))
                    );
            }
        }
        return specification;
    }
}
