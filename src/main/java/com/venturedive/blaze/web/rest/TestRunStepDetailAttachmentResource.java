package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestRunStepDetailAttachment;
import com.venturedive.blaze.repository.TestRunStepDetailAttachmentRepository;
import com.venturedive.blaze.service.TestRunStepDetailAttachmentQueryService;
import com.venturedive.blaze.service.TestRunStepDetailAttachmentService;
import com.venturedive.blaze.service.criteria.TestRunStepDetailAttachmentCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestRunStepDetailAttachment}.
 */
@RestController
@RequestMapping("/api")
public class TestRunStepDetailAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(TestRunStepDetailAttachmentResource.class);

    private static final String ENTITY_NAME = "testRunStepDetailAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestRunStepDetailAttachmentService testRunStepDetailAttachmentService;

    private final TestRunStepDetailAttachmentRepository testRunStepDetailAttachmentRepository;

    private final TestRunStepDetailAttachmentQueryService testRunStepDetailAttachmentQueryService;

    public TestRunStepDetailAttachmentResource(
        TestRunStepDetailAttachmentService testRunStepDetailAttachmentService,
        TestRunStepDetailAttachmentRepository testRunStepDetailAttachmentRepository,
        TestRunStepDetailAttachmentQueryService testRunStepDetailAttachmentQueryService
    ) {
        this.testRunStepDetailAttachmentService = testRunStepDetailAttachmentService;
        this.testRunStepDetailAttachmentRepository = testRunStepDetailAttachmentRepository;
        this.testRunStepDetailAttachmentQueryService = testRunStepDetailAttachmentQueryService;
    }

    /**
     * {@code POST  /test-run-step-detail-attachments} : Create a new testRunStepDetailAttachment.
     *
     * @param testRunStepDetailAttachment the testRunStepDetailAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testRunStepDetailAttachment, or with status {@code 400 (Bad Request)} if the testRunStepDetailAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-run-step-detail-attachments")
    public ResponseEntity<TestRunStepDetailAttachment> createTestRunStepDetailAttachment(
        @Valid @RequestBody TestRunStepDetailAttachment testRunStepDetailAttachment
    ) throws URISyntaxException {
        log.debug("REST request to save TestRunStepDetailAttachment : {}", testRunStepDetailAttachment);
        if (testRunStepDetailAttachment.getId() != null) {
            throw new BadRequestAlertException("A new testRunStepDetailAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestRunStepDetailAttachment result = testRunStepDetailAttachmentService.save(testRunStepDetailAttachment);
        return ResponseEntity
            .created(new URI("/api/test-run-step-detail-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-run-step-detail-attachments/:id} : Updates an existing testRunStepDetailAttachment.
     *
     * @param id the id of the testRunStepDetailAttachment to save.
     * @param testRunStepDetailAttachment the testRunStepDetailAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunStepDetailAttachment,
     * or with status {@code 400 (Bad Request)} if the testRunStepDetailAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testRunStepDetailAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-run-step-detail-attachments/{id}")
    public ResponseEntity<TestRunStepDetailAttachment> updateTestRunStepDetailAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestRunStepDetailAttachment testRunStepDetailAttachment
    ) throws URISyntaxException {
        log.debug("REST request to update TestRunStepDetailAttachment : {}, {}", id, testRunStepDetailAttachment);
        if (testRunStepDetailAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunStepDetailAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunStepDetailAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestRunStepDetailAttachment result = testRunStepDetailAttachmentService.update(testRunStepDetailAttachment);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunStepDetailAttachment.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /test-run-step-detail-attachments/:id} : Partial updates given fields of an existing testRunStepDetailAttachment, field will ignore if it is null
     *
     * @param id the id of the testRunStepDetailAttachment to save.
     * @param testRunStepDetailAttachment the testRunStepDetailAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunStepDetailAttachment,
     * or with status {@code 400 (Bad Request)} if the testRunStepDetailAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the testRunStepDetailAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the testRunStepDetailAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-run-step-detail-attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestRunStepDetailAttachment> partialUpdateTestRunStepDetailAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestRunStepDetailAttachment testRunStepDetailAttachment
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestRunStepDetailAttachment partially : {}, {}", id, testRunStepDetailAttachment);
        if (testRunStepDetailAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunStepDetailAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunStepDetailAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestRunStepDetailAttachment> result = testRunStepDetailAttachmentService.partialUpdate(testRunStepDetailAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunStepDetailAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /test-run-step-detail-attachments} : get all the testRunStepDetailAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testRunStepDetailAttachments in body.
     */
    @GetMapping("/test-run-step-detail-attachments")
    public ResponseEntity<List<TestRunStepDetailAttachment>> getAllTestRunStepDetailAttachments(
        TestRunStepDetailAttachmentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestRunStepDetailAttachments by criteria: {}", criteria);
        Page<TestRunStepDetailAttachment> page = testRunStepDetailAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-run-step-detail-attachments/count} : count all the testRunStepDetailAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-run-step-detail-attachments/count")
    public ResponseEntity<Long> countTestRunStepDetailAttachments(TestRunStepDetailAttachmentCriteria criteria) {
        log.debug("REST request to count TestRunStepDetailAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(testRunStepDetailAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-run-step-detail-attachments/:id} : get the "id" testRunStepDetailAttachment.
     *
     * @param id the id of the testRunStepDetailAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testRunStepDetailAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-run-step-detail-attachments/{id}")
    public ResponseEntity<TestRunStepDetailAttachment> getTestRunStepDetailAttachment(@PathVariable Long id) {
        log.debug("REST request to get TestRunStepDetailAttachment : {}", id);
        Optional<TestRunStepDetailAttachment> testRunStepDetailAttachment = testRunStepDetailAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testRunStepDetailAttachment);
    }

    /**
     * {@code DELETE  /test-run-step-detail-attachments/:id} : delete the "id" testRunStepDetailAttachment.
     *
     * @param id the id of the testRunStepDetailAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-run-step-detail-attachments/{id}")
    public ResponseEntity<Void> deleteTestRunStepDetailAttachment(@PathVariable Long id) {
        log.debug("REST request to delete TestRunStepDetailAttachment : {}", id);
        testRunStepDetailAttachmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
