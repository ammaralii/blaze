package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestRunDetailAttachment;
import com.venturedive.blaze.repository.TestRunDetailAttachmentRepository;
import com.venturedive.blaze.service.TestRunDetailAttachmentQueryService;
import com.venturedive.blaze.service.TestRunDetailAttachmentService;
import com.venturedive.blaze.service.criteria.TestRunDetailAttachmentCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestRunDetailAttachment}.
 */
@RestController
@RequestMapping("/api")
public class TestRunDetailAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(TestRunDetailAttachmentResource.class);

    private static final String ENTITY_NAME = "testRunDetailAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestRunDetailAttachmentService testRunDetailAttachmentService;

    private final TestRunDetailAttachmentRepository testRunDetailAttachmentRepository;

    private final TestRunDetailAttachmentQueryService testRunDetailAttachmentQueryService;

    public TestRunDetailAttachmentResource(
        TestRunDetailAttachmentService testRunDetailAttachmentService,
        TestRunDetailAttachmentRepository testRunDetailAttachmentRepository,
        TestRunDetailAttachmentQueryService testRunDetailAttachmentQueryService
    ) {
        this.testRunDetailAttachmentService = testRunDetailAttachmentService;
        this.testRunDetailAttachmentRepository = testRunDetailAttachmentRepository;
        this.testRunDetailAttachmentQueryService = testRunDetailAttachmentQueryService;
    }

    /**
     * {@code POST  /test-run-detail-attachments} : Create a new testRunDetailAttachment.
     *
     * @param testRunDetailAttachment the testRunDetailAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testRunDetailAttachment, or with status {@code 400 (Bad Request)} if the testRunDetailAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-run-detail-attachments")
    public ResponseEntity<TestRunDetailAttachment> createTestRunDetailAttachment(
        @Valid @RequestBody TestRunDetailAttachment testRunDetailAttachment
    ) throws URISyntaxException {
        log.debug("REST request to save TestRunDetailAttachment : {}", testRunDetailAttachment);
        if (testRunDetailAttachment.getId() != null) {
            throw new BadRequestAlertException("A new testRunDetailAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestRunDetailAttachment result = testRunDetailAttachmentService.save(testRunDetailAttachment);
        return ResponseEntity
            .created(new URI("/api/test-run-detail-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-run-detail-attachments/:id} : Updates an existing testRunDetailAttachment.
     *
     * @param id the id of the testRunDetailAttachment to save.
     * @param testRunDetailAttachment the testRunDetailAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunDetailAttachment,
     * or with status {@code 400 (Bad Request)} if the testRunDetailAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testRunDetailAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-run-detail-attachments/{id}")
    public ResponseEntity<TestRunDetailAttachment> updateTestRunDetailAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestRunDetailAttachment testRunDetailAttachment
    ) throws URISyntaxException {
        log.debug("REST request to update TestRunDetailAttachment : {}, {}", id, testRunDetailAttachment);
        if (testRunDetailAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunDetailAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunDetailAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestRunDetailAttachment result = testRunDetailAttachmentService.update(testRunDetailAttachment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunDetailAttachment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-run-detail-attachments/:id} : Partial updates given fields of an existing testRunDetailAttachment, field will ignore if it is null
     *
     * @param id the id of the testRunDetailAttachment to save.
     * @param testRunDetailAttachment the testRunDetailAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunDetailAttachment,
     * or with status {@code 400 (Bad Request)} if the testRunDetailAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the testRunDetailAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the testRunDetailAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-run-detail-attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestRunDetailAttachment> partialUpdateTestRunDetailAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestRunDetailAttachment testRunDetailAttachment
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestRunDetailAttachment partially : {}, {}", id, testRunDetailAttachment);
        if (testRunDetailAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunDetailAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunDetailAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestRunDetailAttachment> result = testRunDetailAttachmentService.partialUpdate(testRunDetailAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunDetailAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /test-run-detail-attachments} : get all the testRunDetailAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testRunDetailAttachments in body.
     */
    @GetMapping("/test-run-detail-attachments")
    public ResponseEntity<List<TestRunDetailAttachment>> getAllTestRunDetailAttachments(
        TestRunDetailAttachmentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestRunDetailAttachments by criteria: {}", criteria);
        Page<TestRunDetailAttachment> page = testRunDetailAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-run-detail-attachments/count} : count all the testRunDetailAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-run-detail-attachments/count")
    public ResponseEntity<Long> countTestRunDetailAttachments(TestRunDetailAttachmentCriteria criteria) {
        log.debug("REST request to count TestRunDetailAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(testRunDetailAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-run-detail-attachments/:id} : get the "id" testRunDetailAttachment.
     *
     * @param id the id of the testRunDetailAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testRunDetailAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-run-detail-attachments/{id}")
    public ResponseEntity<TestRunDetailAttachment> getTestRunDetailAttachment(@PathVariable Long id) {
        log.debug("REST request to get TestRunDetailAttachment : {}", id);
        Optional<TestRunDetailAttachment> testRunDetailAttachment = testRunDetailAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testRunDetailAttachment);
    }

    /**
     * {@code DELETE  /test-run-detail-attachments/:id} : delete the "id" testRunDetailAttachment.
     *
     * @param id the id of the testRunDetailAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-run-detail-attachments/{id}")
    public ResponseEntity<Void> deleteTestRunDetailAttachment(@PathVariable Long id) {
        log.debug("REST request to delete TestRunDetailAttachment : {}", id);
        testRunDetailAttachmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
