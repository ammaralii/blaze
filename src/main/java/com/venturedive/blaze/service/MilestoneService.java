package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.Milestone;
import com.venturedive.blaze.repository.MilestoneRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Milestone}.
 */
@Service
@Transactional
public class MilestoneService {

    private final Logger log = LoggerFactory.getLogger(MilestoneService.class);

    private final MilestoneRepository milestoneRepository;

    public MilestoneService(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    /**
     * Save a milestone.
     *
     * @param milestone the entity to save.
     * @return the persisted entity.
     */
    public Milestone save(Milestone milestone) {
        log.debug("Request to save Milestone : {}", milestone);
        return milestoneRepository.save(milestone);
    }

    /**
     * Update a milestone.
     *
     * @param milestone the entity to save.
     * @return the persisted entity.
     */
    public Milestone update(Milestone milestone) {
        log.debug("Request to update Milestone : {}", milestone);
        return milestoneRepository.save(milestone);
    }

    /**
     * Partially update a milestone.
     *
     * @param milestone the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Milestone> partialUpdate(Milestone milestone) {
        log.debug("Request to partially update Milestone : {}", milestone);

        return milestoneRepository
            .findById(milestone.getId())
            .map(existingMilestone -> {
                if (milestone.getName() != null) {
                    existingMilestone.setName(milestone.getName());
                }
                if (milestone.getDescription() != null) {
                    existingMilestone.setDescription(milestone.getDescription());
                }
                if (milestone.getReference() != null) {
                    existingMilestone.setReference(milestone.getReference());
                }
                if (milestone.getStartDate() != null) {
                    existingMilestone.setStartDate(milestone.getStartDate());
                }
                if (milestone.getEndDate() != null) {
                    existingMilestone.setEndDate(milestone.getEndDate());
                }
                if (milestone.getIsCompleted() != null) {
                    existingMilestone.setIsCompleted(milestone.getIsCompleted());
                }

                return existingMilestone;
            })
            .map(milestoneRepository::save);
    }

    /**
     * Get all the milestones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Milestone> findAll(Pageable pageable) {
        log.debug("Request to get all Milestones");
        return milestoneRepository.findAll(pageable);
    }

    /**
     * Get one milestone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Milestone> findOne(Long id) {
        log.debug("Request to get Milestone : {}", id);
        return milestoneRepository.findById(id);
    }

    /**
     * Delete the milestone by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Milestone : {}", id);
        milestoneRepository.deleteById(id);
    }
}
