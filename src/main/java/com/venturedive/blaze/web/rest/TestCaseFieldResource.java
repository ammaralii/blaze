package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestCaseField;
import com.venturedive.blaze.repository.TestCaseFieldRepository;
import com.venturedive.blaze.service.TestCaseFieldQueryService;
import com.venturedive.blaze.service.TestCaseFieldService;
import com.venturedive.blaze.service.criteria.TestCaseFieldCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestCaseField}.
 */
@RestController
@RequestMapping("/api")
public class TestCaseFieldResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldResource.class);

    private static final String ENTITY_NAME = "testCaseField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseFieldService testCaseFieldService;

    private final TestCaseFieldRepository testCaseFieldRepository;

    private final TestCaseFieldQueryService testCaseFieldQueryService;

    public TestCaseFieldResource(
        TestCaseFieldService testCaseFieldService,
        TestCaseFieldRepository testCaseFieldRepository,
        TestCaseFieldQueryService testCaseFieldQueryService
    ) {
        this.testCaseFieldService = testCaseFieldService;
        this.testCaseFieldRepository = testCaseFieldRepository;
        this.testCaseFieldQueryService = testCaseFieldQueryService;
    }

    /**
     * {@code POST  /test-case-fields} : Create a new testCaseField.
     *
     * @param testCaseField the testCaseField to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCaseField, or with status {@code 400 (Bad Request)} if the testCaseField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-case-fields")
    public ResponseEntity<TestCaseField> createTestCaseField(@Valid @RequestBody TestCaseField testCaseField) throws URISyntaxException {
        log.debug("REST request to save TestCaseField : {}", testCaseField);
        if (testCaseField.getId() != null) {
            throw new BadRequestAlertException("A new testCaseField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCaseField result = testCaseFieldService.save(testCaseField);
        return ResponseEntity
            .created(new URI("/api/test-case-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-case-fields/:id} : Updates an existing testCaseField.
     *
     * @param id the id of the testCaseField to save.
     * @param testCaseField the testCaseField to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseField,
     * or with status {@code 400 (Bad Request)} if the testCaseField is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCaseField couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-case-fields/{id}")
    public ResponseEntity<TestCaseField> updateTestCaseField(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCaseField testCaseField
    ) throws URISyntaxException {
        log.debug("REST request to update TestCaseField : {}, {}", id, testCaseField);
        if (testCaseField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseField.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCaseField result = testCaseFieldService.update(testCaseField);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseField.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-case-fields/:id} : Partial updates given fields of an existing testCaseField, field will ignore if it is null
     *
     * @param id the id of the testCaseField to save.
     * @param testCaseField the testCaseField to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseField,
     * or with status {@code 400 (Bad Request)} if the testCaseField is not valid,
     * or with status {@code 404 (Not Found)} if the testCaseField is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCaseField couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-case-fields/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCaseField> partialUpdateTestCaseField(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCaseField testCaseField
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCaseField partially : {}, {}", id, testCaseField);
        if (testCaseField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseField.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCaseField> result = testCaseFieldService.partialUpdate(testCaseField);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseField.getId().toString())
        );
    }

    /**
     * {@code GET  /test-case-fields} : get all the testCaseFields.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCaseFields in body.
     */
    @GetMapping("/test-case-fields")
    public ResponseEntity<List<TestCaseField>> getAllTestCaseFields(
        TestCaseFieldCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCaseFields by criteria: {}", criteria);
        Page<TestCaseField> page = testCaseFieldQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-case-fields/count} : count all the testCaseFields.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-case-fields/count")
    public ResponseEntity<Long> countTestCaseFields(TestCaseFieldCriteria criteria) {
        log.debug("REST request to count TestCaseFields by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCaseFieldQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-case-fields/:id} : get the "id" testCaseField.
     *
     * @param id the id of the testCaseField to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCaseField, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-case-fields/{id}")
    public ResponseEntity<TestCaseField> getTestCaseField(@PathVariable Long id) {
        log.debug("REST request to get TestCaseField : {}", id);
        Optional<TestCaseField> testCaseField = testCaseFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCaseField);
    }

    /**
     * {@code DELETE  /test-case-fields/:id} : delete the "id" testCaseField.
     *
     * @param id the id of the testCaseField to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-case-fields/{id}")
    public ResponseEntity<Void> deleteTestCaseField(@PathVariable Long id) {
        log.debug("REST request to delete TestCaseField : {}", id);
        testCaseFieldService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
