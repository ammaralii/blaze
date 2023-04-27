package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestRunStepDetails;
import com.venturedive.blaze.repository.TestRunStepDetailsRepository;
import com.venturedive.blaze.service.TestRunStepDetailsQueryService;
import com.venturedive.blaze.service.TestRunStepDetailsService;
import com.venturedive.blaze.service.criteria.TestRunStepDetailsCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestRunStepDetails}.
 */
@RestController
@RequestMapping("/api")
public class TestRunStepDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TestRunStepDetailsResource.class);

    private static final String ENTITY_NAME = "testRunStepDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestRunStepDetailsService testRunStepDetailsService;

    private final TestRunStepDetailsRepository testRunStepDetailsRepository;

    private final TestRunStepDetailsQueryService testRunStepDetailsQueryService;

    public TestRunStepDetailsResource(
        TestRunStepDetailsService testRunStepDetailsService,
        TestRunStepDetailsRepository testRunStepDetailsRepository,
        TestRunStepDetailsQueryService testRunStepDetailsQueryService
    ) {
        this.testRunStepDetailsService = testRunStepDetailsService;
        this.testRunStepDetailsRepository = testRunStepDetailsRepository;
        this.testRunStepDetailsQueryService = testRunStepDetailsQueryService;
    }

    /**
     * {@code POST  /test-run-step-details} : Create a new testRunStepDetails.
     *
     * @param testRunStepDetails the testRunStepDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testRunStepDetails, or with status {@code 400 (Bad Request)} if the testRunStepDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-run-step-details")
    public ResponseEntity<TestRunStepDetails> createTestRunStepDetails(@Valid @RequestBody TestRunStepDetails testRunStepDetails)
        throws URISyntaxException {
        log.debug("REST request to save TestRunStepDetails : {}", testRunStepDetails);
        if (testRunStepDetails.getId() != null) {
            throw new BadRequestAlertException("A new testRunStepDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestRunStepDetails result = testRunStepDetailsService.save(testRunStepDetails);
        return ResponseEntity
            .created(new URI("/api/test-run-step-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-run-step-details/:id} : Updates an existing testRunStepDetails.
     *
     * @param id the id of the testRunStepDetails to save.
     * @param testRunStepDetails the testRunStepDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunStepDetails,
     * or with status {@code 400 (Bad Request)} if the testRunStepDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testRunStepDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-run-step-details/{id}")
    public ResponseEntity<TestRunStepDetails> updateTestRunStepDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestRunStepDetails testRunStepDetails
    ) throws URISyntaxException {
        log.debug("REST request to update TestRunStepDetails : {}, {}", id, testRunStepDetails);
        if (testRunStepDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunStepDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunStepDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestRunStepDetails result = testRunStepDetailsService.update(testRunStepDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunStepDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-run-step-details/:id} : Partial updates given fields of an existing testRunStepDetails, field will ignore if it is null
     *
     * @param id the id of the testRunStepDetails to save.
     * @param testRunStepDetails the testRunStepDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunStepDetails,
     * or with status {@code 400 (Bad Request)} if the testRunStepDetails is not valid,
     * or with status {@code 404 (Not Found)} if the testRunStepDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the testRunStepDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-run-step-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestRunStepDetails> partialUpdateTestRunStepDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestRunStepDetails testRunStepDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestRunStepDetails partially : {}, {}", id, testRunStepDetails);
        if (testRunStepDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunStepDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunStepDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestRunStepDetails> result = testRunStepDetailsService.partialUpdate(testRunStepDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunStepDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /test-run-step-details} : get all the testRunStepDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testRunStepDetails in body.
     */
    @GetMapping("/test-run-step-details")
    public ResponseEntity<List<TestRunStepDetails>> getAllTestRunStepDetails(
        TestRunStepDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestRunStepDetails by criteria: {}", criteria);
        Page<TestRunStepDetails> page = testRunStepDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-run-step-details/count} : count all the testRunStepDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-run-step-details/count")
    public ResponseEntity<Long> countTestRunStepDetails(TestRunStepDetailsCriteria criteria) {
        log.debug("REST request to count TestRunStepDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(testRunStepDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-run-step-details/:id} : get the "id" testRunStepDetails.
     *
     * @param id the id of the testRunStepDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testRunStepDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-run-step-details/{id}")
    public ResponseEntity<TestRunStepDetails> getTestRunStepDetails(@PathVariable Long id) {
        log.debug("REST request to get TestRunStepDetails : {}", id);
        Optional<TestRunStepDetails> testRunStepDetails = testRunStepDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testRunStepDetails);
    }

    /**
     * {@code DELETE  /test-run-step-details/:id} : delete the "id" testRunStepDetails.
     *
     * @param id the id of the testRunStepDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-run-step-details/{id}")
    public ResponseEntity<Void> deleteTestRunStepDetails(@PathVariable Long id) {
        log.debug("REST request to delete TestRunStepDetails : {}", id);
        testRunStepDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
