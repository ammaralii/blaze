package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestSuite;
import com.venturedive.blaze.repository.TestSuiteRepository;
import com.venturedive.blaze.service.TestSuiteQueryService;
import com.venturedive.blaze.service.TestSuiteService;
import com.venturedive.blaze.service.criteria.TestSuiteCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestSuite}.
 */
@RestController
@RequestMapping("/api")
public class TestSuiteResource {

    private final Logger log = LoggerFactory.getLogger(TestSuiteResource.class);

    private static final String ENTITY_NAME = "testSuite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestSuiteService testSuiteService;

    private final TestSuiteRepository testSuiteRepository;

    private final TestSuiteQueryService testSuiteQueryService;

    public TestSuiteResource(
        TestSuiteService testSuiteService,
        TestSuiteRepository testSuiteRepository,
        TestSuiteQueryService testSuiteQueryService
    ) {
        this.testSuiteService = testSuiteService;
        this.testSuiteRepository = testSuiteRepository;
        this.testSuiteQueryService = testSuiteQueryService;
    }

    /**
     * {@code POST  /test-suites} : Create a new testSuite.
     *
     * @param testSuite the testSuite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testSuite, or with status {@code 400 (Bad Request)} if the testSuite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-suites")
    public ResponseEntity<TestSuite> createTestSuite(@Valid @RequestBody TestSuite testSuite) throws URISyntaxException {
        log.debug("REST request to save TestSuite : {}", testSuite);
        if (testSuite.getId() != null) {
            throw new BadRequestAlertException("A new testSuite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestSuite result = testSuiteService.save(testSuite);
        return ResponseEntity
            .created(new URI("/api/test-suites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-suites/:id} : Updates an existing testSuite.
     *
     * @param id the id of the testSuite to save.
     * @param testSuite the testSuite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testSuite,
     * or with status {@code 400 (Bad Request)} if the testSuite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testSuite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-suites/{id}")
    public ResponseEntity<TestSuite> updateTestSuite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestSuite testSuite
    ) throws URISyntaxException {
        log.debug("REST request to update TestSuite : {}, {}", id, testSuite);
        if (testSuite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testSuite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testSuiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestSuite result = testSuiteService.update(testSuite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testSuite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-suites/:id} : Partial updates given fields of an existing testSuite, field will ignore if it is null
     *
     * @param id the id of the testSuite to save.
     * @param testSuite the testSuite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testSuite,
     * or with status {@code 400 (Bad Request)} if the testSuite is not valid,
     * or with status {@code 404 (Not Found)} if the testSuite is not found,
     * or with status {@code 500 (Internal Server Error)} if the testSuite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-suites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestSuite> partialUpdateTestSuite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestSuite testSuite
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestSuite partially : {}, {}", id, testSuite);
        if (testSuite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testSuite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testSuiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestSuite> result = testSuiteService.partialUpdate(testSuite);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testSuite.getId().toString())
        );
    }

    /**
     * {@code GET  /test-suites} : get all the testSuites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testSuites in body.
     */
    @GetMapping("/test-suites")
    public ResponseEntity<List<TestSuite>> getAllTestSuites(
        TestSuiteCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestSuites by criteria: {}", criteria);
        Page<TestSuite> page = testSuiteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-suites/count} : count all the testSuites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-suites/count")
    public ResponseEntity<Long> countTestSuites(TestSuiteCriteria criteria) {
        log.debug("REST request to count TestSuites by criteria: {}", criteria);
        return ResponseEntity.ok().body(testSuiteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-suites/:id} : get the "id" testSuite.
     *
     * @param id the id of the testSuite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testSuite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-suites/{id}")
    public ResponseEntity<TestSuite> getTestSuite(@PathVariable Long id) {
        log.debug("REST request to get TestSuite : {}", id);
        Optional<TestSuite> testSuite = testSuiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testSuite);
    }

    /**
     * {@code DELETE  /test-suites/:id} : delete the "id" testSuite.
     *
     * @param id the id of the testSuite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-suites/{id}")
    public ResponseEntity<Void> deleteTestSuite(@PathVariable Long id) {
        log.debug("REST request to delete TestSuite : {}", id);
        testSuiteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
