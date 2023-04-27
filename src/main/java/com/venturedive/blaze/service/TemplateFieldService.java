package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.TemplateField;
import com.venturedive.blaze.repository.TemplateFieldRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TemplateField}.
 */
@Service
@Transactional
public class TemplateFieldService {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldService.class);

    private final TemplateFieldRepository templateFieldRepository;

    public TemplateFieldService(TemplateFieldRepository templateFieldRepository) {
        this.templateFieldRepository = templateFieldRepository;
    }

    /**
     * Save a templateField.
     *
     * @param templateField the entity to save.
     * @return the persisted entity.
     */
    public TemplateField save(TemplateField templateField) {
        log.debug("Request to save TemplateField : {}", templateField);
        return templateFieldRepository.save(templateField);
    }

    /**
     * Update a templateField.
     *
     * @param templateField the entity to save.
     * @return the persisted entity.
     */
    public TemplateField update(TemplateField templateField) {
        log.debug("Request to update TemplateField : {}", templateField);
        return templateFieldRepository.save(templateField);
    }

    /**
     * Partially update a templateField.
     *
     * @param templateField the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TemplateField> partialUpdate(TemplateField templateField) {
        log.debug("Request to partially update TemplateField : {}", templateField);

        return templateFieldRepository
            .findById(templateField.getId())
            .map(existingTemplateField -> {
                if (templateField.getFieldName() != null) {
                    existingTemplateField.setFieldName(templateField.getFieldName());
                }

                return existingTemplateField;
            })
            .map(templateFieldRepository::save);
    }

    /**
     * Get all the templateFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TemplateField> findAll(Pageable pageable) {
        log.debug("Request to get all TemplateFields");
        return templateFieldRepository.findAll(pageable);
    }

    /**
     * Get all the templateFields with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TemplateField> findAllWithEagerRelationships(Pageable pageable) {
        return templateFieldRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one templateField by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TemplateField> findOne(Long id) {
        log.debug("Request to get TemplateField : {}", id);
        return templateFieldRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the templateField by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TemplateField : {}", id);
        templateFieldRepository.deleteById(id);
    }
}
