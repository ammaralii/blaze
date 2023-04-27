package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TemplateFieldType;
import com.venturedive.blaze.repository.TemplateFieldTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TemplateFieldType}.
 */
@Service
@Transactional
public class TemplateFieldTypeService {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldTypeService.class);

    private final TemplateFieldTypeRepository templateFieldTypeRepository;

    public TemplateFieldTypeService(TemplateFieldTypeRepository templateFieldTypeRepository) {
        this.templateFieldTypeRepository = templateFieldTypeRepository;
    }

    /**
     * Save a templateFieldType.
     *
     * @param templateFieldType the entity to save.
     * @return the persisted entity.
     */
    public TemplateFieldType save(TemplateFieldType templateFieldType) {
        log.debug("Request to save TemplateFieldType : {}", templateFieldType);
        return templateFieldTypeRepository.save(templateFieldType);
    }

    /**
     * Update a templateFieldType.
     *
     * @param templateFieldType the entity to save.
     * @return the persisted entity.
     */
    public TemplateFieldType update(TemplateFieldType templateFieldType) {
        log.debug("Request to update TemplateFieldType : {}", templateFieldType);
        return templateFieldTypeRepository.save(templateFieldType);
    }

    /**
     * Partially update a templateFieldType.
     *
     * @param templateFieldType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TemplateFieldType> partialUpdate(TemplateFieldType templateFieldType) {
        log.debug("Request to partially update TemplateFieldType : {}", templateFieldType);

        return templateFieldTypeRepository
            .findById(templateFieldType.getId())
            .map(existingTemplateFieldType -> {
                if (templateFieldType.getType() != null) {
                    existingTemplateFieldType.setType(templateFieldType.getType());
                }
                if (templateFieldType.getIsList() != null) {
                    existingTemplateFieldType.setIsList(templateFieldType.getIsList());
                }
                if (templateFieldType.getAttachments() != null) {
                    existingTemplateFieldType.setAttachments(templateFieldType.getAttachments());
                }

                return existingTemplateFieldType;
            })
            .map(templateFieldTypeRepository::save);
    }

    /**
     * Get all the templateFieldTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TemplateFieldType> findAll(Pageable pageable) {
        log.debug("Request to get all TemplateFieldTypes");
        return templateFieldTypeRepository.findAll(pageable);
    }

    /**
     * Get one templateFieldType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TemplateFieldType> findOne(Long id) {
        log.debug("Request to get TemplateFieldType : {}", id);
        return templateFieldTypeRepository.findById(id);
    }

    /**
     * Delete the templateFieldType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TemplateFieldType : {}", id);
        templateFieldTypeRepository.deleteById(id);
    }
}
