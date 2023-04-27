package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.Template;
import com.venturedive.blaze.repository.TemplateRepository;
import com.venturedive.blaze.service.TemplateQueryService;
import com.venturedive.blaze.service.TemplateService;
import com.venturedive.blaze.service.criteria.TemplateCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.Template}.
 */
@RestController
@RequestMapping("/api")
public class TemplateResource {

    private final Logger log = LoggerFactory.getLogger(TemplateResource.class);

    private static final String ENTITY_NAME = "template";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemplateService templateService;

    private final TemplateRepository templateRepository;

    private final TemplateQueryService templateQueryService;

    public TemplateResource(
        TemplateService templateService,
        TemplateRepository templateRepository,
        TemplateQueryService templateQueryService
    ) {
        this.templateService = templateService;
        this.templateRepository = templateRepository;
        this.templateQueryService = templateQueryService;
    }

    /**
     * {@code POST  /templates} : Create a new template.
     *
     * @param template the template to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new template, or with status {@code 400 (Bad Request)} if the template has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/templates")
    public ResponseEntity<Template> createTemplate(@Valid @RequestBody Template template) throws URISyntaxException {
        log.debug("REST request to save Template : {}", template);
        if (template.getId() != null) {
            throw new BadRequestAlertException("A new template cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Template result = templateService.save(template);
        return ResponseEntity
            .created(new URI("/api/templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /templates/:id} : Updates an existing template.
     *
     * @param id the id of the template to save.
     * @param template the template to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated template,
     * or with status {@code 400 (Bad Request)} if the template is not valid,
     * or with status {@code 500 (Internal Server Error)} if the template couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/templates/{id}")
    public ResponseEntity<Template> updateTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Template template
    ) throws URISyntaxException {
        log.debug("REST request to update Template : {}, {}", id, template);
        if (template.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, template.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Template result = templateService.update(template);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, template.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /templates/:id} : Partial updates given fields of an existing template, field will ignore if it is null
     *
     * @param id the id of the template to save.
     * @param template the template to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated template,
     * or with status {@code 400 (Bad Request)} if the template is not valid,
     * or with status {@code 404 (Not Found)} if the template is not found,
     * or with status {@code 500 (Internal Server Error)} if the template couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/templates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Template> partialUpdateTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Template template
    ) throws URISyntaxException {
        log.debug("REST request to partial update Template partially : {}, {}", id, template);
        if (template.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, template.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Template> result = templateService.partialUpdate(template);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, template.getId().toString())
        );
    }

    /**
     * {@code GET  /templates} : get all the templates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templates in body.
     */
    @GetMapping("/templates")
    public ResponseEntity<List<Template>> getAllTemplates(
        TemplateCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Templates by criteria: {}", criteria);
        Page<Template> page = templateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /templates/count} : count all the templates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/templates/count")
    public ResponseEntity<Long> countTemplates(TemplateCriteria criteria) {
        log.debug("REST request to count Templates by criteria: {}", criteria);
        return ResponseEntity.ok().body(templateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /templates/:id} : get the "id" template.
     *
     * @param id the id of the template to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the template, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/templates/{id}")
    public ResponseEntity<Template> getTemplate(@PathVariable Long id) {
        log.debug("REST request to get Template : {}", id);
        Optional<Template> template = templateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(template);
    }

    /**
     * {@code DELETE  /templates/:id} : delete the "id" template.
     *
     * @param id the id of the template to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/templates/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        log.debug("REST request to delete Template : {}", id);
        templateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
