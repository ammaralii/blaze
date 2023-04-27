package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.Template;
import com.venturedive.blaze.repository.TemplateRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Template}.
 */
@Service
@Transactional
public class TemplateService {

    private final Logger log = LoggerFactory.getLogger(TemplateService.class);

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    /**
     * Save a template.
     *
     * @param template the entity to save.
     * @return the persisted entity.
     */
    public Template save(Template template) {
        log.debug("Request to save Template : {}", template);
        return templateRepository.save(template);
    }

    /**
     * Update a template.
     *
     * @param template the entity to save.
     * @return the persisted entity.
     */
    public Template update(Template template) {
        log.debug("Request to update Template : {}", template);
        return templateRepository.save(template);
    }

    /**
     * Partially update a template.
     *
     * @param template the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Template> partialUpdate(Template template) {
        log.debug("Request to partially update Template : {}", template);

        return templateRepository
            .findById(template.getId())
            .map(existingTemplate -> {
                if (template.getTemplateName() != null) {
                    existingTemplate.setTemplateName(template.getTemplateName());
                }
                if (template.getCreatedAt() != null) {
                    existingTemplate.setCreatedAt(template.getCreatedAt());
                }
                if (template.getCreatedBy() != null) {
                    existingTemplate.setCreatedBy(template.getCreatedBy());
                }

                return existingTemplate;
            })
            .map(templateRepository::save);
    }

    /**
     * Get all the templates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Template> findAll(Pageable pageable) {
        log.debug("Request to get all Templates");
        return templateRepository.findAll(pageable);
    }

    /**
     * Get all the templates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Template> findAllWithEagerRelationships(Pageable pageable) {
        return templateRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one template by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Template> findOne(Long id) {
        log.debug("Request to get Template : {}", id);
        return templateRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the template by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Template : {}", id);
        templateRepository.deleteById(id);
    }
}
