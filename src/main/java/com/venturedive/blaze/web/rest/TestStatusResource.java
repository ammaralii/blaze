package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestStatus;
import com.venturedive.blaze.repository.TestStatusRepository;
import com.venturedive.blaze.service.TestStatusQueryService;
import com.venturedive.blaze.service.TestStatusService;
import com.venturedive.blaze.service.criteria.TestStatusCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestStatus}.
 */
@RestController
@RequestMapping("/api")
public class TestStatusResource {

    private final Logger log = LoggerFactory.getLogger(TestStatusResource.class);

    private static final String ENTITY_NAME = "testStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestStatusService testStatusService;

    private final TestStatusRepository testStatusRepository;

    private final TestStatusQueryService testStatusQueryService;

    public TestStatusResource(
        TestStatusService testStatusService,
        TestStatusRepository testStatusRepository,
        TestStatusQueryService testStatusQueryService
    ) {
        this.testStatusService = testStatusService;
        this.testStatusRepository = testStatusRepository;
        this.testStatusQueryService = testStatusQueryService;
    }

    /**
     * {@code POST  /test-statuses} : Create a new testStatus.
     *
     * @param testStatus the testStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testStatus, or with status {@code 400 (Bad Request)} if the testStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-statuses")
    public ResponseEntity<TestStatus> createTestStatus(@Valid @RequestBody TestStatus testStatus) throws URISyntaxException {
        log.debug("REST request to save TestStatus : {}", testStatus);
        if (testStatus.getId() != null) {
            throw new BadRequestAlertException("A new testStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestStatus result = testStatusService.save(testStatus);
        return ResponseEntity
            .created(new URI("/api/test-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-statuses/:id} : Updates an existing testStatus.
     *
     * @param id the id of the testStatus to save.
     * @param testStatus the testStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testStatus,
     * or with status {@code 400 (Bad Request)} if the testStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-statuses/{id}")
    public ResponseEntity<TestStatus> updateTestStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestStatus testStatus
    ) throws URISyntaxException {
        log.debug("REST request to update TestStatus : {}, {}", id, testStatus);
        if (testStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestStatus result = testStatusService.update(testStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-statuses/:id} : Partial updates given fields of an existing testStatus, field will ignore if it is null
     *
     * @param id the id of the testStatus to save.
     * @param testStatus the testStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testStatus,
     * or with status {@code 400 (Bad Request)} if the testStatus is not valid,
     * or with status {@code 404 (Not Found)} if the testStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the testStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestStatus> partialUpdateTestStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestStatus testStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestStatus partially : {}, {}", id, testStatus);
        if (testStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestStatus> result = testStatusService.partialUpdate(testStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /test-statuses} : get all the testStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testStatuses in body.
     */
    @GetMapping("/test-statuses")
    public ResponseEntity<List<TestStatus>> getAllTestStatuses(
        TestStatusCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestStatuses by criteria: {}", criteria);
        Page<TestStatus> page = testStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-statuses/count} : count all the testStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-statuses/count")
    public ResponseEntity<Long> countTestStatuses(TestStatusCriteria criteria) {
        log.debug("REST request to count TestStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(testStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-statuses/:id} : get the "id" testStatus.
     *
     * @param id the id of the testStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-statuses/{id}")
    public ResponseEntity<TestStatus> getTestStatus(@PathVariable Long id) {
        log.debug("REST request to get TestStatus : {}", id);
        Optional<TestStatus> testStatus = testStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testStatus);
    }

    /**
     * {@code DELETE  /test-statuses/:id} : delete the "id" testStatus.
     *
     * @param id the id of the testStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-statuses/{id}")
    public ResponseEntity<Void> deleteTestStatus(@PathVariable Long id) {
        log.debug("REST request to delete TestStatus : {}", id);
        testStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
