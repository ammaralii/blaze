package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestRunDetails;
import com.venturedive.blaze.repository.TestRunDetailsRepository;
import com.venturedive.blaze.service.TestRunDetailsQueryService;
import com.venturedive.blaze.service.TestRunDetailsService;
import com.venturedive.blaze.service.criteria.TestRunDetailsCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestRunDetails}.
 */
@RestController
@RequestMapping("/api")
public class TestRunDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TestRunDetailsResource.class);

    private static final String ENTITY_NAME = "testRunDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestRunDetailsService testRunDetailsService;

    private final TestRunDetailsRepository testRunDetailsRepository;

    private final TestRunDetailsQueryService testRunDetailsQueryService;

    public TestRunDetailsResource(
        TestRunDetailsService testRunDetailsService,
        TestRunDetailsRepository testRunDetailsRepository,
        TestRunDetailsQueryService testRunDetailsQueryService
    ) {
        this.testRunDetailsService = testRunDetailsService;
        this.testRunDetailsRepository = testRunDetailsRepository;
        this.testRunDetailsQueryService = testRunDetailsQueryService;
    }

    /**
     * {@code POST  /test-run-details} : Create a new testRunDetails.
     *
     * @param testRunDetails the testRunDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testRunDetails, or with status {@code 400 (Bad Request)} if the testRunDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-run-details")
    public ResponseEntity<TestRunDetails> createTestRunDetails(@Valid @RequestBody TestRunDetails testRunDetails)
        throws URISyntaxException {
        log.debug("REST request to save TestRunDetails : {}", testRunDetails);
        if (testRunDetails.getId() != null) {
            throw new BadRequestAlertException("A new testRunDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestRunDetails result = testRunDetailsService.save(testRunDetails);
        return ResponseEntity
            .created(new URI("/api/test-run-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-run-details/:id} : Updates an existing testRunDetails.
     *
     * @param id the id of the testRunDetails to save.
     * @param testRunDetails the testRunDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunDetails,
     * or with status {@code 400 (Bad Request)} if the testRunDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testRunDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-run-details/{id}")
    public ResponseEntity<TestRunDetails> updateTestRunDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestRunDetails testRunDetails
    ) throws URISyntaxException {
        log.debug("REST request to update TestRunDetails : {}, {}", id, testRunDetails);
        if (testRunDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestRunDetails result = testRunDetailsService.update(testRunDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-run-details/:id} : Partial updates given fields of an existing testRunDetails, field will ignore if it is null
     *
     * @param id the id of the testRunDetails to save.
     * @param testRunDetails the testRunDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunDetails,
     * or with status {@code 400 (Bad Request)} if the testRunDetails is not valid,
     * or with status {@code 404 (Not Found)} if the testRunDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the testRunDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-run-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestRunDetails> partialUpdateTestRunDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestRunDetails testRunDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestRunDetails partially : {}, {}", id, testRunDetails);
        if (testRunDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestRunDetails> result = testRunDetailsService.partialUpdate(testRunDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /test-run-details} : get all the testRunDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testRunDetails in body.
     */
    @GetMapping("/test-run-details")
    public ResponseEntity<List<TestRunDetails>> getAllTestRunDetails(
        TestRunDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestRunDetails by criteria: {}", criteria);
        Page<TestRunDetails> page = testRunDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-run-details/count} : count all the testRunDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-run-details/count")
    public ResponseEntity<Long> countTestRunDetails(TestRunDetailsCriteria criteria) {
        log.debug("REST request to count TestRunDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(testRunDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-run-details/:id} : get the "id" testRunDetails.
     *
     * @param id the id of the testRunDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testRunDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-run-details/{id}")
    public ResponseEntity<TestRunDetails> getTestRunDetails(@PathVariable Long id) {
        log.debug("REST request to get TestRunDetails : {}", id);
        Optional<TestRunDetails> testRunDetails = testRunDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testRunDetails);
    }

    /**
     * {@code DELETE  /test-run-details/:id} : delete the "id" testRunDetails.
     *
     * @param id the id of the testRunDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-run-details/{id}")
    public ResponseEntity<Void> deleteTestRunDetails(@PathVariable Long id) {
        log.debug("REST request to delete TestRunDetails : {}", id);
        testRunDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
