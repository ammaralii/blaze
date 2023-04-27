package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestCaseFieldAttachment;
import com.venturedive.blaze.repository.TestCaseFieldAttachmentRepository;
import com.venturedive.blaze.service.TestCaseFieldAttachmentQueryService;
import com.venturedive.blaze.service.TestCaseFieldAttachmentService;
import com.venturedive.blaze.service.criteria.TestCaseFieldAttachmentCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestCaseFieldAttachment}.
 */
@RestController
@RequestMapping("/api")
public class TestCaseFieldAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldAttachmentResource.class);

    private static final String ENTITY_NAME = "testCaseFieldAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseFieldAttachmentService testCaseFieldAttachmentService;

    private final TestCaseFieldAttachmentRepository testCaseFieldAttachmentRepository;

    private final TestCaseFieldAttachmentQueryService testCaseFieldAttachmentQueryService;

    public TestCaseFieldAttachmentResource(
        TestCaseFieldAttachmentService testCaseFieldAttachmentService,
        TestCaseFieldAttachmentRepository testCaseFieldAttachmentRepository,
        TestCaseFieldAttachmentQueryService testCaseFieldAttachmentQueryService
    ) {
        this.testCaseFieldAttachmentService = testCaseFieldAttachmentService;
        this.testCaseFieldAttachmentRepository = testCaseFieldAttachmentRepository;
        this.testCaseFieldAttachmentQueryService = testCaseFieldAttachmentQueryService;
    }

    /**
     * {@code POST  /test-case-field-attachments} : Create a new testCaseFieldAttachment.
     *
     * @param testCaseFieldAttachment the testCaseFieldAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCaseFieldAttachment, or with status {@code 400 (Bad Request)} if the testCaseFieldAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-case-field-attachments")
    public ResponseEntity<TestCaseFieldAttachment> createTestCaseFieldAttachment(
        @Valid @RequestBody TestCaseFieldAttachment testCaseFieldAttachment
    ) throws URISyntaxException {
        log.debug("REST request to save TestCaseFieldAttachment : {}", testCaseFieldAttachment);
        if (testCaseFieldAttachment.getId() != null) {
            throw new BadRequestAlertException("A new testCaseFieldAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCaseFieldAttachment result = testCaseFieldAttachmentService.save(testCaseFieldAttachment);
        return ResponseEntity
            .created(new URI("/api/test-case-field-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-case-field-attachments/:id} : Updates an existing testCaseFieldAttachment.
     *
     * @param id the id of the testCaseFieldAttachment to save.
     * @param testCaseFieldAttachment the testCaseFieldAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseFieldAttachment,
     * or with status {@code 400 (Bad Request)} if the testCaseFieldAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCaseFieldAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-case-field-attachments/{id}")
    public ResponseEntity<TestCaseFieldAttachment> updateTestCaseFieldAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCaseFieldAttachment testCaseFieldAttachment
    ) throws URISyntaxException {
        log.debug("REST request to update TestCaseFieldAttachment : {}, {}", id, testCaseFieldAttachment);
        if (testCaseFieldAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseFieldAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseFieldAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCaseFieldAttachment result = testCaseFieldAttachmentService.update(testCaseFieldAttachment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseFieldAttachment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-case-field-attachments/:id} : Partial updates given fields of an existing testCaseFieldAttachment, field will ignore if it is null
     *
     * @param id the id of the testCaseFieldAttachment to save.
     * @param testCaseFieldAttachment the testCaseFieldAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseFieldAttachment,
     * or with status {@code 400 (Bad Request)} if the testCaseFieldAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the testCaseFieldAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCaseFieldAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-case-field-attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCaseFieldAttachment> partialUpdateTestCaseFieldAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCaseFieldAttachment testCaseFieldAttachment
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCaseFieldAttachment partially : {}, {}", id, testCaseFieldAttachment);
        if (testCaseFieldAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseFieldAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseFieldAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCaseFieldAttachment> result = testCaseFieldAttachmentService.partialUpdate(testCaseFieldAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseFieldAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /test-case-field-attachments} : get all the testCaseFieldAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCaseFieldAttachments in body.
     */
    @GetMapping("/test-case-field-attachments")
    public ResponseEntity<List<TestCaseFieldAttachment>> getAllTestCaseFieldAttachments(
        TestCaseFieldAttachmentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCaseFieldAttachments by criteria: {}", criteria);
        Page<TestCaseFieldAttachment> page = testCaseFieldAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-case-field-attachments/count} : count all the testCaseFieldAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-case-field-attachments/count")
    public ResponseEntity<Long> countTestCaseFieldAttachments(TestCaseFieldAttachmentCriteria criteria) {
        log.debug("REST request to count TestCaseFieldAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCaseFieldAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-case-field-attachments/:id} : get the "id" testCaseFieldAttachment.
     *
     * @param id the id of the testCaseFieldAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCaseFieldAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-case-field-attachments/{id}")
    public ResponseEntity<TestCaseFieldAttachment> getTestCaseFieldAttachment(@PathVariable Long id) {
        log.debug("REST request to get TestCaseFieldAttachment : {}", id);
        Optional<TestCaseFieldAttachment> testCaseFieldAttachment = testCaseFieldAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCaseFieldAttachment);
    }

    /**
     * {@code DELETE  /test-case-field-attachments/:id} : delete the "id" testCaseFieldAttachment.
     *
     * @param id the id of the testCaseFieldAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-case-field-attachments/{id}")
    public ResponseEntity<Void> deleteTestCaseFieldAttachment(@PathVariable Long id) {
        log.debug("REST request to delete TestCaseFieldAttachment : {}", id);
        testCaseFieldAttachmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
