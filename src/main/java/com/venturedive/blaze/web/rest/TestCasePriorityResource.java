package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestCasePriority;
import com.venturedive.blaze.repository.TestCasePriorityRepository;
import com.venturedive.blaze.service.TestCasePriorityQueryService;
import com.venturedive.blaze.service.TestCasePriorityService;
import com.venturedive.blaze.service.criteria.TestCasePriorityCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestCasePriority}.
 */
@RestController
@RequestMapping("/api")
public class TestCasePriorityResource {

    private final Logger log = LoggerFactory.getLogger(TestCasePriorityResource.class);

    private static final String ENTITY_NAME = "testCasePriority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCasePriorityService testCasePriorityService;

    private final TestCasePriorityRepository testCasePriorityRepository;

    private final TestCasePriorityQueryService testCasePriorityQueryService;

    public TestCasePriorityResource(
        TestCasePriorityService testCasePriorityService,
        TestCasePriorityRepository testCasePriorityRepository,
        TestCasePriorityQueryService testCasePriorityQueryService
    ) {
        this.testCasePriorityService = testCasePriorityService;
        this.testCasePriorityRepository = testCasePriorityRepository;
        this.testCasePriorityQueryService = testCasePriorityQueryService;
    }

    /**
     * {@code POST  /test-case-priorities} : Create a new testCasePriority.
     *
     * @param testCasePriority the testCasePriority to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCasePriority, or with status {@code 400 (Bad Request)} if the testCasePriority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-case-priorities")
    public ResponseEntity<TestCasePriority> createTestCasePriority(@Valid @RequestBody TestCasePriority testCasePriority)
        throws URISyntaxException {
        log.debug("REST request to save TestCasePriority : {}", testCasePriority);
        if (testCasePriority.getId() != null) {
            throw new BadRequestAlertException("A new testCasePriority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCasePriority result = testCasePriorityService.save(testCasePriority);
        return ResponseEntity
            .created(new URI("/api/test-case-priorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-case-priorities/:id} : Updates an existing testCasePriority.
     *
     * @param id the id of the testCasePriority to save.
     * @param testCasePriority the testCasePriority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCasePriority,
     * or with status {@code 400 (Bad Request)} if the testCasePriority is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCasePriority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-case-priorities/{id}")
    public ResponseEntity<TestCasePriority> updateTestCasePriority(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCasePriority testCasePriority
    ) throws URISyntaxException {
        log.debug("REST request to update TestCasePriority : {}, {}", id, testCasePriority);
        if (testCasePriority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCasePriority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCasePriorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCasePriority result = testCasePriorityService.update(testCasePriority);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCasePriority.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-case-priorities/:id} : Partial updates given fields of an existing testCasePriority, field will ignore if it is null
     *
     * @param id the id of the testCasePriority to save.
     * @param testCasePriority the testCasePriority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCasePriority,
     * or with status {@code 400 (Bad Request)} if the testCasePriority is not valid,
     * or with status {@code 404 (Not Found)} if the testCasePriority is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCasePriority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-case-priorities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCasePriority> partialUpdateTestCasePriority(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCasePriority testCasePriority
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCasePriority partially : {}, {}", id, testCasePriority);
        if (testCasePriority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCasePriority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCasePriorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCasePriority> result = testCasePriorityService.partialUpdate(testCasePriority);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCasePriority.getId().toString())
        );
    }

    /**
     * {@code GET  /test-case-priorities} : get all the testCasePriorities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCasePriorities in body.
     */
    @GetMapping("/test-case-priorities")
    public ResponseEntity<List<TestCasePriority>> getAllTestCasePriorities(
        TestCasePriorityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCasePriorities by criteria: {}", criteria);
        Page<TestCasePriority> page = testCasePriorityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-case-priorities/count} : count all the testCasePriorities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-case-priorities/count")
    public ResponseEntity<Long> countTestCasePriorities(TestCasePriorityCriteria criteria) {
        log.debug("REST request to count TestCasePriorities by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCasePriorityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-case-priorities/:id} : get the "id" testCasePriority.
     *
     * @param id the id of the testCasePriority to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCasePriority, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-case-priorities/{id}")
    public ResponseEntity<TestCasePriority> getTestCasePriority(@PathVariable Long id) {
        log.debug("REST request to get TestCasePriority : {}", id);
        Optional<TestCasePriority> testCasePriority = testCasePriorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCasePriority);
    }

    /**
     * {@code DELETE  /test-case-priorities/:id} : delete the "id" testCasePriority.
     *
     * @param id the id of the testCasePriority to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-case-priorities/{id}")
    public ResponseEntity<Void> deleteTestCasePriority(@PathVariable Long id) {
        log.debug("REST request to delete TestCasePriority : {}", id);
        testCasePriorityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
