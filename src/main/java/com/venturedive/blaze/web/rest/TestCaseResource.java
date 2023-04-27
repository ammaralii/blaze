package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestCase;
import com.venturedive.blaze.repository.TestCaseRepository;
import com.venturedive.blaze.service.TestCaseQueryService;
import com.venturedive.blaze.service.TestCaseService;
import com.venturedive.blaze.service.criteria.TestCaseCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestCase}.
 */
@RestController
@RequestMapping("/api")
public class TestCaseResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseResource.class);

    private static final String ENTITY_NAME = "testCase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseService testCaseService;

    private final TestCaseRepository testCaseRepository;

    private final TestCaseQueryService testCaseQueryService;

    public TestCaseResource(
        TestCaseService testCaseService,
        TestCaseRepository testCaseRepository,
        TestCaseQueryService testCaseQueryService
    ) {
        this.testCaseService = testCaseService;
        this.testCaseRepository = testCaseRepository;
        this.testCaseQueryService = testCaseQueryService;
    }

    /**
     * {@code POST  /test-cases} : Create a new testCase.
     *
     * @param testCase the testCase to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCase, or with status {@code 400 (Bad Request)} if the testCase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-cases")
    public ResponseEntity<TestCase> createTestCase(@Valid @RequestBody TestCase testCase) throws URISyntaxException {
        log.debug("REST request to save TestCase : {}", testCase);
        if (testCase.getId() != null) {
            throw new BadRequestAlertException("A new testCase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCase result = testCaseService.save(testCase);
        return ResponseEntity
            .created(new URI("/api/test-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-cases/:id} : Updates an existing testCase.
     *
     * @param id the id of the testCase to save.
     * @param testCase the testCase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCase,
     * or with status {@code 400 (Bad Request)} if the testCase is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-cases/{id}")
    public ResponseEntity<TestCase> updateTestCase(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCase testCase
    ) throws URISyntaxException {
        log.debug("REST request to update TestCase : {}, {}", id, testCase);
        if (testCase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCase.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCase result = testCaseService.update(testCase);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCase.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-cases/:id} : Partial updates given fields of an existing testCase, field will ignore if it is null
     *
     * @param id the id of the testCase to save.
     * @param testCase the testCase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCase,
     * or with status {@code 400 (Bad Request)} if the testCase is not valid,
     * or with status {@code 404 (Not Found)} if the testCase is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-cases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCase> partialUpdateTestCase(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCase testCase
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCase partially : {}, {}", id, testCase);
        if (testCase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCase.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCase> result = testCaseService.partialUpdate(testCase);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCase.getId().toString())
        );
    }

    /**
     * {@code GET  /test-cases} : get all the testCases.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCases in body.
     */
    @GetMapping("/test-cases")
    public ResponseEntity<List<TestCase>> getAllTestCases(
        TestCaseCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCases by criteria: {}", criteria);
        Page<TestCase> page = testCaseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-cases/count} : count all the testCases.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-cases/count")
    public ResponseEntity<Long> countTestCases(TestCaseCriteria criteria) {
        log.debug("REST request to count TestCases by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCaseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-cases/:id} : get the "id" testCase.
     *
     * @param id the id of the testCase to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCase, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-cases/{id}")
    public ResponseEntity<TestCase> getTestCase(@PathVariable Long id) {
        log.debug("REST request to get TestCase : {}", id);
        Optional<TestCase> testCase = testCaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCase);
    }

    /**
     * {@code DELETE  /test-cases/:id} : delete the "id" testCase.
     *
     * @param id the id of the testCase to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-cases/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        log.debug("REST request to delete TestCase : {}", id);
        testCaseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
