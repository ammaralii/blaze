package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TemplateField;
import com.venturedive.blaze.repository.TemplateFieldRepository;
import com.venturedive.blaze.service.TemplateFieldQueryService;
import com.venturedive.blaze.service.TemplateFieldService;
import com.venturedive.blaze.service.criteria.TemplateFieldCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TemplateField}.
 */
@RestController
@RequestMapping("/api")
public class TemplateFieldResource {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldResource.class);

    private static final String ENTITY_NAME = "templateField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemplateFieldService templateFieldService;

    private final TemplateFieldRepository templateFieldRepository;

    private final TemplateFieldQueryService templateFieldQueryService;

    public TemplateFieldResource(
        TemplateFieldService templateFieldService,
        TemplateFieldRepository templateFieldRepository,
        TemplateFieldQueryService templateFieldQueryService
    ) {
        this.templateFieldService = templateFieldService;
        this.templateFieldRepository = templateFieldRepository;
        this.templateFieldQueryService = templateFieldQueryService;
    }

    /**
     * {@code POST  /template-fields} : Create a new templateField.
     *
     * @param templateField the templateField to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new templateField, or with status {@code 400 (Bad Request)} if the templateField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/template-fields")
    public ResponseEntity<TemplateField> createTemplateField(@Valid @RequestBody TemplateField templateField) throws URISyntaxException {
        log.debug("REST request to save TemplateField : {}", templateField);
        if (templateField.getId() != null) {
            throw new BadRequestAlertException("A new templateField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TemplateField result = templateFieldService.save(templateField);
        return ResponseEntity
            .created(new URI("/api/template-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /template-fields/:id} : Updates an existing templateField.
     *
     * @param id the id of the templateField to save.
     * @param templateField the templateField to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateField,
     * or with status {@code 400 (Bad Request)} if the templateField is not valid,
     * or with status {@code 500 (Internal Server Error)} if the templateField couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/template-fields/{id}")
    public ResponseEntity<TemplateField> updateTemplateField(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TemplateField templateField
    ) throws URISyntaxException {
        log.debug("REST request to update TemplateField : {}, {}", id, templateField);
        if (templateField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateField.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TemplateField result = templateFieldService.update(templateField);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateField.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /template-fields/:id} : Partial updates given fields of an existing templateField, field will ignore if it is null
     *
     * @param id the id of the templateField to save.
     * @param templateField the templateField to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateField,
     * or with status {@code 400 (Bad Request)} if the templateField is not valid,
     * or with status {@code 404 (Not Found)} if the templateField is not found,
     * or with status {@code 500 (Internal Server Error)} if the templateField couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/template-fields/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TemplateField> partialUpdateTemplateField(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TemplateField templateField
    ) throws URISyntaxException {
        log.debug("REST request to partial update TemplateField partially : {}, {}", id, templateField);
        if (templateField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateField.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TemplateField> result = templateFieldService.partialUpdate(templateField);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateField.getId().toString())
        );
    }

    /**
     * {@code GET  /template-fields} : get all the templateFields.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templateFields in body.
     */
    @GetMapping("/template-fields")
    public ResponseEntity<List<TemplateField>> getAllTemplateFields(
        TemplateFieldCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TemplateFields by criteria: {}", criteria);
        Page<TemplateField> page = templateFieldQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /template-fields/count} : count all the templateFields.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/template-fields/count")
    public ResponseEntity<Long> countTemplateFields(TemplateFieldCriteria criteria) {
        log.debug("REST request to count TemplateFields by criteria: {}", criteria);
        return ResponseEntity.ok().body(templateFieldQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /template-fields/:id} : get the "id" templateField.
     *
     * @param id the id of the templateField to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the templateField, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/template-fields/{id}")
    public ResponseEntity<TemplateField> getTemplateField(@PathVariable Long id) {
        log.debug("REST request to get TemplateField : {}", id);
        Optional<TemplateField> templateField = templateFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(templateField);
    }

    /**
     * {@code DELETE  /template-fields/:id} : delete the "id" templateField.
     *
     * @param id the id of the templateField to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/template-fields/{id}")
    public ResponseEntity<Void> deleteTemplateField(@PathVariable Long id) {
        log.debug("REST request to delete TemplateField : {}", id);
        templateFieldService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
