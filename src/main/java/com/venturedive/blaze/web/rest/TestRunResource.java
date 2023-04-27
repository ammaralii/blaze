package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestRun;
import com.venturedive.blaze.repository.TestRunRepository;
import com.venturedive.blaze.service.TestRunQueryService;
import com.venturedive.blaze.service.TestRunService;
import com.venturedive.blaze.service.criteria.TestRunCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestRun}.
 */
@RestController
@RequestMapping("/api")
public class TestRunResource {

    private final Logger log = LoggerFactory.getLogger(TestRunResource.class);

    private static final String ENTITY_NAME = "testRun";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestRunService testRunService;

    private final TestRunRepository testRunRepository;

    private final TestRunQueryService testRunQueryService;

    public TestRunResource(TestRunService testRunService, TestRunRepository testRunRepository, TestRunQueryService testRunQueryService) {
        this.testRunService = testRunService;
        this.testRunRepository = testRunRepository;
        this.testRunQueryService = testRunQueryService;
    }

    /**
     * {@code POST  /test-runs} : Create a new testRun.
     *
     * @param testRun the testRun to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testRun, or with status {@code 400 (Bad Request)} if the testRun has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-runs")
    public ResponseEntity<TestRun> createTestRun(@Valid @RequestBody TestRun testRun) throws URISyntaxException {
        log.debug("REST request to save TestRun : {}", testRun);
        if (testRun.getId() != null) {
            throw new BadRequestAlertException("A new testRun cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestRun result = testRunService.save(testRun);
        return ResponseEntity
            .created(new URI("/api/test-runs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-runs/:id} : Updates an existing testRun.
     *
     * @param id the id of the testRun to save.
     * @param testRun the testRun to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRun,
     * or with status {@code 400 (Bad Request)} if the testRun is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testRun couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-runs/{id}")
    public ResponseEntity<TestRun> updateTestRun(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestRun testRun
    ) throws URISyntaxException {
        log.debug("REST request to update TestRun : {}, {}", id, testRun);
        if (testRun.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRun.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestRun result = testRunService.update(testRun);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRun.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-runs/:id} : Partial updates given fields of an existing testRun, field will ignore if it is null
     *
     * @param id the id of the testRun to save.
     * @param testRun the testRun to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRun,
     * or with status {@code 400 (Bad Request)} if the testRun is not valid,
     * or with status {@code 404 (Not Found)} if the testRun is not found,
     * or with status {@code 500 (Internal Server Error)} if the testRun couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-runs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestRun> partialUpdateTestRun(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestRun testRun
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestRun partially : {}, {}", id, testRun);
        if (testRun.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRun.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestRun> result = testRunService.partialUpdate(testRun);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRun.getId().toString())
        );
    }

    /**
     * {@code GET  /test-runs} : get all the testRuns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testRuns in body.
     */
    @GetMapping("/test-runs")
    public ResponseEntity<List<TestRun>> getAllTestRuns(
        TestRunCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestRuns by criteria: {}", criteria);
        Page<TestRun> page = testRunQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-runs/count} : count all the testRuns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-runs/count")
    public ResponseEntity<Long> countTestRuns(TestRunCriteria criteria) {
        log.debug("REST request to count TestRuns by criteria: {}", criteria);
        return ResponseEntity.ok().body(testRunQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-runs/:id} : get the "id" testRun.
     *
     * @param id the id of the testRun to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testRun, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-runs/{id}")
    public ResponseEntity<TestRun> getTestRun(@PathVariable Long id) {
        log.debug("REST request to get TestRun : {}", id);
        Optional<TestRun> testRun = testRunService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testRun);
    }

    /**
     * {@code DELETE  /test-runs/:id} : delete the "id" testRun.
     *
     * @param id the id of the testRun to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-runs/{id}")
    public ResponseEntity<Void> deleteTestRun(@PathVariable Long id) {
        log.debug("REST request to delete TestRun : {}", id);
        testRunService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
