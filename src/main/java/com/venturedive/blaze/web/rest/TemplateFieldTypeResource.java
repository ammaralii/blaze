package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TemplateFieldType;
import com.venturedive.blaze.repository.TemplateFieldTypeRepository;
import com.venturedive.blaze.service.TemplateFieldTypeQueryService;
import com.venturedive.blaze.service.TemplateFieldTypeService;
import com.venturedive.blaze.service.criteria.TemplateFieldTypeCriteria;
import com.venturedive.blaze.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.venturedive.blaze.domain.TemplateFieldType}.
 */
@RestController
@RequestMapping("/api")
public class TemplateFieldTypeResource {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldTypeResource.class);

    private static final String ENTITY_NAME = "templateFieldType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemplateFieldTypeService templateFieldTypeService;

    private final TemplateFieldTypeRepository templateFieldTypeRepository;

    private final TemplateFieldTypeQueryService templateFieldTypeQueryService;

    public TemplateFieldTypeResource(
        TemplateFieldTypeService templateFieldTypeService,
        TemplateFieldTypeRepository templateFieldTypeRepository,
        TemplateFieldTypeQueryService templateFieldTypeQueryService
    ) {
        this.templateFieldTypeService = templateFieldTypeService;
        this.templateFieldTypeRepository = templateFieldTypeRepository;
        this.templateFieldTypeQueryService = templateFieldTypeQueryService;
    }

    /**
     * {@code POST  /template-field-types} : Create a new templateFieldType.
     *
     * @param templateFieldType the templateFieldType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new templateFieldType, or with status {@code 400 (Bad Request)} if the templateFieldType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/template-field-types")
    public ResponseEntity<TemplateFieldType> createTemplateFieldType(@Valid @RequestBody TemplateFieldType templateFieldType)
        throws URISyntaxException {
        log.debug("REST request to save TemplateFieldType : {}", templateFieldType);
        if (templateFieldType.getId() != null) {
            throw new BadRequestAlertException("A new templateFieldType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TemplateFieldType result = templateFieldTypeService.save(templateFieldType);
        return ResponseEntity
            .created(new URI("/api/template-field-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /template-field-types/:id} : Updates an existing templateFieldType.
     *
     * @param id the id of the templateFieldType to save.
     * @param templateFieldType the templateFieldType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateFieldType,
     * or with status {@code 400 (Bad Request)} if the templateFieldType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the templateFieldType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/template-field-types/{id}")
    public ResponseEntity<TemplateFieldType> updateTemplateFieldType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TemplateFieldType templateFieldType
    ) throws URISyntaxException {
        log.debug("REST request to update TemplateFieldType : {}, {}", id, templateFieldType);
        if (templateFieldType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateFieldType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateFieldTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TemplateFieldType result = templateFieldTypeService.update(templateFieldType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateFieldType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /template-field-types/:id} : Partial updates given fields of an existing templateFieldType, field will ignore if it is null
     *
     * @param id the id of the templateFieldType to save.
     * @param templateFieldType the templateFieldType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateFieldType,
     * or with status {@code 400 (Bad Request)} if the templateFieldType is not valid,
     * or with status {@code 404 (Not Found)} if the templateFieldType is not found,
     * or with status {@code 500 (Internal Server Error)} if the templateFieldType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/template-field-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TemplateFieldType> partialUpdateTemplateFieldType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TemplateFieldType templateFieldType
    ) throws URISyntaxException {
        log.debug("REST request to partial update TemplateFieldType partially : {}, {}", id, templateFieldType);
        if (templateFieldType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateFieldType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateFieldTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TemplateFieldType> result = templateFieldTypeService.partialUpdate(templateFieldType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateFieldType.getId().toString())
        );
    }

    /**
     * {@code GET  /template-field-types} : get all the templateFieldTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templateFieldTypes in body.
     */
    @GetMapping("/template-field-types")
    public ResponseEntity<List<TemplateFieldType>> getAllTemplateFieldTypes(
        TemplateFieldTypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TemplateFieldTypes by criteria: {}", criteria);
        Page<TemplateFieldType> page = templateFieldTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /template-field-types/count} : count all the templateFieldTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/template-field-types/count")
    public ResponseEntity<Long> countTemplateFieldTypes(TemplateFieldTypeCriteria criteria) {
        log.debug("REST request to count TemplateFieldTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(templateFieldTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /template-field-types/:id} : get the "id" templateFieldType.
     *
     * @param id the id of the templateFieldType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the templateFieldType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/template-field-types/{id}")
    public ResponseEntity<TemplateFieldType> getTemplateFieldType(@PathVariable Long id) {
        log.debug("REST request to get TemplateFieldType : {}", id);
        Optional<TemplateFieldType> templateFieldType = templateFieldTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(templateFieldType);
    }

    /**
     * {@code DELETE  /template-field-types/:id} : delete the "id" templateFieldType.
     *
     * @param id the id of the templateFieldType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/template-field-types/{id}")
    public ResponseEntity<Void> deleteTemplateFieldType(@PathVariable Long id) {
        log.debug("REST request to delete TemplateFieldType : {}", id);
        templateFieldTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
