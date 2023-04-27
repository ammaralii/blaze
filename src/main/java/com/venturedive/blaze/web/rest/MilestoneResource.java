package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.Milestone;
import com.venturedive.blaze.repository.MilestoneRepository;
import com.venturedive.blaze.service.MilestoneQueryService;
import com.venturedive.blaze.service.MilestoneService;
import com.venturedive.blaze.service.criteria.MilestoneCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.Milestone}.
 */
@RestController
@RequestMapping("/api")
public class MilestoneResource {

    private final Logger log = LoggerFactory.getLogger(MilestoneResource.class);

    private static final String ENTITY_NAME = "milestone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MilestoneService milestoneService;

    private final MilestoneRepository milestoneRepository;

    private final MilestoneQueryService milestoneQueryService;

    public MilestoneResource(
        MilestoneService milestoneService,
        MilestoneRepository milestoneRepository,
        MilestoneQueryService milestoneQueryService
    ) {
        this.milestoneService = milestoneService;
        this.milestoneRepository = milestoneRepository;
        this.milestoneQueryService = milestoneQueryService;
    }

    /**
     * {@code POST  /milestones} : Create a new milestone.
     *
     * @param milestone the milestone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new milestone, or with status {@code 400 (Bad Request)} if the milestone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/milestones")
    public ResponseEntity<Milestone> createMilestone(@Valid @RequestBody Milestone milestone) throws URISyntaxException {
        log.debug("REST request to save Milestone : {}", milestone);
        if (milestone.getId() != null) {
            throw new BadRequestAlertException("A new milestone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Milestone result = milestoneService.save(milestone);
        return ResponseEntity
            .created(new URI("/api/milestones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /milestones/:id} : Updates an existing milestone.
     *
     * @param id the id of the milestone to save.
     * @param milestone the milestone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated milestone,
     * or with status {@code 400 (Bad Request)} if the milestone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the milestone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/milestones/{id}")
    public ResponseEntity<Milestone> updateMilestone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Milestone milestone
    ) throws URISyntaxException {
        log.debug("REST request to update Milestone : {}, {}", id, milestone);
        if (milestone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, milestone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!milestoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Milestone result = milestoneService.update(milestone);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, milestone.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /milestones/:id} : Partial updates given fields of an existing milestone, field will ignore if it is null
     *
     * @param id the id of the milestone to save.
     * @param milestone the milestone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated milestone,
     * or with status {@code 400 (Bad Request)} if the milestone is not valid,
     * or with status {@code 404 (Not Found)} if the milestone is not found,
     * or with status {@code 500 (Internal Server Error)} if the milestone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/milestones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Milestone> partialUpdateMilestone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Milestone milestone
    ) throws URISyntaxException {
        log.debug("REST request to partial update Milestone partially : {}, {}", id, milestone);
        if (milestone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, milestone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!milestoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Milestone> result = milestoneService.partialUpdate(milestone);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, milestone.getId().toString())
        );
    }

    /**
     * {@code GET  /milestones} : get all the milestones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of milestones in body.
     */
    @GetMapping("/milestones")
    public ResponseEntity<List<Milestone>> getAllMilestones(
        MilestoneCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Milestones by criteria: {}", criteria);
        Page<Milestone> page = milestoneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /milestones/count} : count all the milestones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/milestones/count")
    public ResponseEntity<Long> countMilestones(MilestoneCriteria criteria) {
        log.debug("REST request to count Milestones by criteria: {}", criteria);
        return ResponseEntity.ok().body(milestoneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /milestones/:id} : get the "id" milestone.
     *
     * @param id the id of the milestone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the milestone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/milestones/{id}")
    public ResponseEntity<Milestone> getMilestone(@PathVariable Long id) {
        log.debug("REST request to get Milestone : {}", id);
        Optional<Milestone> milestone = milestoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(milestone);
    }

    /**
     * {@code DELETE  /milestones/:id} : delete the "id" milestone.
     *
     * @param id the id of the milestone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/milestones/{id}")
    public ResponseEntity<Void> deleteMilestone(@PathVariable Long id) {
        log.debug("REST request to delete Milestone : {}", id);
        milestoneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
