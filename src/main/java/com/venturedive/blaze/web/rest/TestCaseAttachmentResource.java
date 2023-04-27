package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestCaseAttachment;
import com.venturedive.blaze.repository.TestCaseAttachmentRepository;
import com.venturedive.blaze.service.TestCaseAttachmentQueryService;
import com.venturedive.blaze.service.TestCaseAttachmentService;
import com.venturedive.blaze.service.criteria.TestCaseAttachmentCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestCaseAttachment}.
 */
@RestController
@RequestMapping("/api")
public class TestCaseAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseAttachmentResource.class);

    private static final String ENTITY_NAME = "testCaseAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseAttachmentService testCaseAttachmentService;

    private final TestCaseAttachmentRepository testCaseAttachmentRepository;

    private final TestCaseAttachmentQueryService testCaseAttachmentQueryService;

    public TestCaseAttachmentResource(
        TestCaseAttachmentService testCaseAttachmentService,
        TestCaseAttachmentRepository testCaseAttachmentRepository,
        TestCaseAttachmentQueryService testCaseAttachmentQueryService
    ) {
        this.testCaseAttachmentService = testCaseAttachmentService;
        this.testCaseAttachmentRepository = testCaseAttachmentRepository;
        this.testCaseAttachmentQueryService = testCaseAttachmentQueryService;
    }

    /**
     * {@code POST  /test-case-attachments} : Create a new testCaseAttachment.
     *
     * @param testCaseAttachment the testCaseAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCaseAttachment, or with status {@code 400 (Bad Request)} if the testCaseAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-case-attachments")
    public ResponseEntity<TestCaseAttachment> createTestCaseAttachment(@Valid @RequestBody TestCaseAttachment testCaseAttachment)
        throws URISyntaxException {
        log.debug("REST request to save TestCaseAttachment : {}", testCaseAttachment);
        if (testCaseAttachment.getId() != null) {
            throw new BadRequestAlertException("A new testCaseAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCaseAttachment result = testCaseAttachmentService.save(testCaseAttachment);
        return ResponseEntity
            .created(new URI("/api/test-case-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-case-attachments/:id} : Updates an existing testCaseAttachment.
     *
     * @param id the id of the testCaseAttachment to save.
     * @param testCaseAttachment the testCaseAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseAttachment,
     * or with status {@code 400 (Bad Request)} if the testCaseAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCaseAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-case-attachments/{id}")
    public ResponseEntity<TestCaseAttachment> updateTestCaseAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCaseAttachment testCaseAttachment
    ) throws URISyntaxException {
        log.debug("REST request to update TestCaseAttachment : {}, {}", id, testCaseAttachment);
        if (testCaseAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCaseAttachment result = testCaseAttachmentService.update(testCaseAttachment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseAttachment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-case-attachments/:id} : Partial updates given fields of an existing testCaseAttachment, field will ignore if it is null
     *
     * @param id the id of the testCaseAttachment to save.
     * @param testCaseAttachment the testCaseAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseAttachment,
     * or with status {@code 400 (Bad Request)} if the testCaseAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the testCaseAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCaseAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-case-attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCaseAttachment> partialUpdateTestCaseAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCaseAttachment testCaseAttachment
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCaseAttachment partially : {}, {}", id, testCaseAttachment);
        if (testCaseAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCaseAttachment> result = testCaseAttachmentService.partialUpdate(testCaseAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /test-case-attachments} : get all the testCaseAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCaseAttachments in body.
     */
    @GetMapping("/test-case-attachments")
    public ResponseEntity<List<TestCaseAttachment>> getAllTestCaseAttachments(
        TestCaseAttachmentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCaseAttachments by criteria: {}", criteria);
        Page<TestCaseAttachment> page = testCaseAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-case-attachments/count} : count all the testCaseAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-case-attachments/count")
    public ResponseEntity<Long> countTestCaseAttachments(TestCaseAttachmentCriteria criteria) {
        log.debug("REST request to count TestCaseAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCaseAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-case-attachments/:id} : get the "id" testCaseAttachment.
     *
     * @param id the id of the testCaseAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCaseAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-case-attachments/{id}")
    public ResponseEntity<TestCaseAttachment> getTestCaseAttachment(@PathVariable Long id) {
        log.debug("REST request to get TestCaseAttachment : {}", id);
        Optional<TestCaseAttachment> testCaseAttachment = testCaseAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCaseAttachment);
    }

    /**
     * {@code DELETE  /test-case-attachments/:id} : delete the "id" testCaseAttachment.
     *
     * @param id the id of the testCaseAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-case-attachments/{id}")
    public ResponseEntity<Void> deleteTestCaseAttachment(@PathVariable Long id) {
        log.debug("REST request to delete TestCaseAttachment : {}", id);
        testCaseAttachmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
