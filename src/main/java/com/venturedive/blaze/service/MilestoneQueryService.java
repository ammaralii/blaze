package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.*; // for static metamodels
import com.venturedive.blaze.domain.Milestone;
import com.venturedive.blaze.repository.MilestoneRepository;
import com.venturedive.blaze.service.criteria.MilestoneCriteria;
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
 * Service for executing complex queries for {@link Milestone} entities in the database.
 * The main input is a {@link MilestoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Milestone} or a {@link Page} of {@link Milestone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MilestoneQueryService extends QueryService<Milestone> {

    private final Logger log = LoggerFactory.getLogger(MilestoneQueryService.class);

    private final MilestoneRepository milestoneRepository;

    public MilestoneQueryService(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    /**
     * Return a {@link List} of {@link Milestone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Milestone> findByCriteria(MilestoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Milestone> specification = createSpecification(criteria);
        return milestoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Milestone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Milestone> findByCriteria(MilestoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Milestone> specification = createSpecification(criteria);
        return milestoneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MilestoneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Milestone> specification = createSpecification(criteria);
        return milestoneRepository.count(specification);
    }

    /**
     * Function to convert {@link MilestoneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Milestone> createSpecification(MilestoneCriteria criteria) {
        Specification<Milestone> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Milestone_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Milestone_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Milestone_.description));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), Milestone_.reference));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Milestone_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Milestone_.endDate));
            }
            if (criteria.getIsCompleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsCompleted(), Milestone_.isCompleted));
            }
            if (criteria.getParentMilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentMilestoneId(),
                            root -> root.join(Milestone_.parentMilestone, JoinType.LEFT).get(Milestone_.id)
                        )
                    );
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(Milestone_.project, JoinType.LEFT).get(Project_.id))
                    );
            }
            if (criteria.getMilestoneParentmilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMilestoneParentmilestoneId(),
                            root -> root.join(Milestone_.milestoneParentmilestones, JoinType.LEFT).get(Milestone_.id)
                        )
                    );
            }
            if (criteria.getTestcaseMilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcaseMilestoneId(),
                            root -> root.join(Milestone_.testcaseMilestones, JoinType.LEFT).get(TestCase_.id)
                        )
                    );
            }
            if (criteria.getTestrunMilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunMilestoneId(),
                            root -> root.join(Milestone_.testrunMilestones, JoinType.LEFT).get(TestRun_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
