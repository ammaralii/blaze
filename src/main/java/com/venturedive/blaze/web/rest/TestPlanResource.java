package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestPlan;
import com.venturedive.blaze.repository.TestPlanRepository;
import com.venturedive.blaze.service.TestPlanQueryService;
import com.venturedive.blaze.service.TestPlanService;
import com.venturedive.blaze.service.criteria.TestPlanCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestPlan}.
 */
@RestController
@RequestMapping("/api")
public class TestPlanResource {

    private final Logger log = LoggerFactory.getLogger(TestPlanResource.class);

    private static final String ENTITY_NAME = "testPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestPlanService testPlanService;

    private final TestPlanRepository testPlanRepository;

    private final TestPlanQueryService testPlanQueryService;

    public TestPlanResource(
        TestPlanService testPlanService,
        TestPlanRepository testPlanRepository,
        TestPlanQueryService testPlanQueryService
    ) {
        this.testPlanService = testPlanService;
        this.testPlanRepository = testPlanRepository;
        this.testPlanQueryService = testPlanQueryService;
    }

    /**
     * {@code POST  /test-plans} : Create a new testPlan.
     *
     * @param testPlan the testPlan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testPlan, or with status {@code 400 (Bad Request)} if the testPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-plans")
    public ResponseEntity<TestPlan> createTestPlan(@Valid @RequestBody TestPlan testPlan) throws URISyntaxException {
        log.debug("REST request to save TestPlan : {}", testPlan);
        if (testPlan.getId() != null) {
            throw new BadRequestAlertException("A new testPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestPlan result = testPlanService.save(testPlan);
        return ResponseEntity
            .created(new URI("/api/test-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-plans/:id} : Updates an existing testPlan.
     *
     * @param id the id of the testPlan to save.
     * @param testPlan the testPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testPlan,
     * or with status {@code 400 (Bad Request)} if the testPlan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-plans/{id}")
    public ResponseEntity<TestPlan> updateTestPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestPlan testPlan
    ) throws URISyntaxException {
        log.debug("REST request to update TestPlan : {}, {}", id, testPlan);
        if (testPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestPlan result = testPlanService.update(testPlan);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testPlan.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-plans/:id} : Partial updates given fields of an existing testPlan, field will ignore if it is null
     *
     * @param id the id of the testPlan to save.
     * @param testPlan the testPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testPlan,
     * or with status {@code 400 (Bad Request)} if the testPlan is not valid,
     * or with status {@code 404 (Not Found)} if the testPlan is not found,
     * or with status {@code 500 (Internal Server Error)} if the testPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-plans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestPlan> partialUpdateTestPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestPlan testPlan
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestPlan partially : {}, {}", id, testPlan);
        if (testPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestPlan> result = testPlanService.partialUpdate(testPlan);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testPlan.getId().toString())
        );
    }

    /**
     * {@code GET  /test-plans} : get all the testPlans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testPlans in body.
     */
    @GetMapping("/test-plans")
    public ResponseEntity<List<TestPlan>> getAllTestPlans(
        TestPlanCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestPlans by criteria: {}", criteria);
        Page<TestPlan> page = testPlanQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-plans/count} : count all the testPlans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-plans/count")
    public ResponseEntity<Long> countTestPlans(TestPlanCriteria criteria) {
        log.debug("REST request to count TestPlans by criteria: {}", criteria);
        return ResponseEntity.ok().body(testPlanQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-plans/:id} : get the "id" testPlan.
     *
     * @param id the id of the testPlan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testPlan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-plans/{id}")
    public ResponseEntity<TestPlan> getTestPlan(@PathVariable Long id) {
        log.debug("REST request to get TestPlan : {}", id);
        Optional<TestPlan> testPlan = testPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testPlan);
    }

    /**
     * {@code DELETE  /test-plans/:id} : delete the "id" testPlan.
     *
     * @param id the id of the testPlan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-plans/{id}")
    public ResponseEntity<Void> deleteTestPlan(@PathVariable Long id) {
        log.debug("REST request to delete TestPlan : {}", id);
        testPlanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
