package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.TestLevel;
import com.venturedive.blaze.repository.TestLevelRepository;
import com.venturedive.blaze.service.TestLevelQueryService;
import com.venturedive.blaze.service.TestLevelService;
import com.venturedive.blaze.service.criteria.TestLevelCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.TestLevel}.
 */
@RestController
@RequestMapping("/api")
public class TestLevelResource {

    private final Logger log = LoggerFactory.getLogger(TestLevelResource.class);

    private static final String ENTITY_NAME = "testLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestLevelService testLevelService;

    private final TestLevelRepository testLevelRepository;

    private final TestLevelQueryService testLevelQueryService;

    public TestLevelResource(
        TestLevelService testLevelService,
        TestLevelRepository testLevelRepository,
        TestLevelQueryService testLevelQueryService
    ) {
        this.testLevelService = testLevelService;
        this.testLevelRepository = testLevelRepository;
        this.testLevelQueryService = testLevelQueryService;
    }

    /**
     * {@code POST  /test-levels} : Create a new testLevel.
     *
     * @param testLevel the testLevel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testLevel, or with status {@code 400 (Bad Request)} if the testLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-levels")
    public ResponseEntity<TestLevel> createTestLevel(@Valid @RequestBody TestLevel testLevel) throws URISyntaxException {
        log.debug("REST request to save TestLevel : {}", testLevel);
        if (testLevel.getId() != null) {
            throw new BadRequestAlertException("A new testLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestLevel result = testLevelService.save(testLevel);
        return ResponseEntity
            .created(new URI("/api/test-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-levels/:id} : Updates an existing testLevel.
     *
     * @param id the id of the testLevel to save.
     * @param testLevel the testLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testLevel,
     * or with status {@code 400 (Bad Request)} if the testLevel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-levels/{id}")
    public ResponseEntity<TestLevel> updateTestLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestLevel testLevel
    ) throws URISyntaxException {
        log.debug("REST request to update TestLevel : {}, {}", id, testLevel);
        if (testLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestLevel result = testLevelService.update(testLevel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testLevel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-levels/:id} : Partial updates given fields of an existing testLevel, field will ignore if it is null
     *
     * @param id the id of the testLevel to save.
     * @param testLevel the testLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testLevel,
     * or with status {@code 400 (Bad Request)} if the testLevel is not valid,
     * or with status {@code 404 (Not Found)} if the testLevel is not found,
     * or with status {@code 500 (Internal Server Error)} if the testLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-levels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestLevel> partialUpdateTestLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestLevel testLevel
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestLevel partially : {}, {}", id, testLevel);
        if (testLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestLevel> result = testLevelService.partialUpdate(testLevel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testLevel.getId().toString())
        );
    }

    /**
     * {@code GET  /test-levels} : get all the testLevels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testLevels in body.
     */
    @GetMapping("/test-levels")
    public ResponseEntity<List<TestLevel>> getAllTestLevels(
        TestLevelCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestLevels by criteria: {}", criteria);
        Page<TestLevel> page = testLevelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-levels/count} : count all the testLevels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-levels/count")
    public ResponseEntity<Long> countTestLevels(TestLevelCriteria criteria) {
        log.debug("REST request to count TestLevels by criteria: {}", criteria);
        return ResponseEntity.ok().body(testLevelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-levels/:id} : get the "id" testLevel.
     *
     * @param id the id of the testLevel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testLevel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-levels/{id}")
    public ResponseEntity<TestLevel> getTestLevel(@PathVariable Long id) {
        log.debug("REST request to get TestLevel : {}", id);
        Optional<TestLevel> testLevel = testLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testLevel);
    }

    /**
     * {@code DELETE  /test-levels/:id} : delete the "id" testLevel.
     *
     * @param id the id of the testLevel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-levels/{id}")
    public ResponseEntity<Void> deleteTestLevel(@PathVariable Long id) {
        log.debug("REST request to delete TestLevel : {}", id);
        testLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
