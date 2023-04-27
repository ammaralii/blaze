package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.Section;
import com.venturedive.blaze.repository.SectionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Section}.
 */
@Service
@Transactional
public class SectionService {

    private final Logger log = LoggerFactory.getLogger(SectionService.class);

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    /**
     * Save a section.
     *
     * @param section the entity to save.
     * @return the persisted entity.
     */
    public Section save(Section section) {
        log.debug("Request to save Section : {}", section);
        return sectionRepository.save(section);
    }

    /**
     * Update a section.
     *
     * @param section the entity to save.
     * @return the persisted entity.
     */
    public Section update(Section section) {
        log.debug("Request to update Section : {}", section);
        return sectionRepository.save(section);
    }

    /**
     * Partially update a section.
     *
     * @param section the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Section> partialUpdate(Section section) {
        log.debug("Request to partially update Section : {}", section);

        return sectionRepository
            .findById(section.getId())
            .map(existingSection -> {
                if (section.getName() != null) {
                    existingSection.setName(section.getName());
                }
                if (section.getDescription() != null) {
                    existingSection.setDescription(section.getDescription());
                }
                if (section.getCreatedAt() != null) {
                    existingSection.setCreatedAt(section.getCreatedAt());
                }
                if (section.getCreatedBy() != null) {
                    existingSection.setCreatedBy(section.getCreatedBy());
                }
                if (section.getUpdatedAt() != null) {
                    existingSection.setUpdatedAt(section.getUpdatedAt());
                }
                if (section.getUpdatedBy() != null) {
                    existingSection.setUpdatedBy(section.getUpdatedBy());
                }

                return existingSection;
            })
            .map(sectionRepository::save);
    }

    /**
     * Get all the sections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Section> findAll(Pageable pageable) {
        log.debug("Request to get all Sections");
        return sectionRepository.findAll(pageable);
    }

    /**
     * Get one section by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Section> findOne(Long id) {
        log.debug("Request to get Section : {}", id);
        return sectionRepository.findById(id);
    }

    /**
     * Delete the section by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Section : {}", id);
        sectionRepository.deleteById(id);
    }
}
