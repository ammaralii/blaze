package com.venturedive.blaze.web.rest;

import com.venturedive.blaze.domain.Role;
import com.venturedive.blaze.repository.RoleRepository;
import com.venturedive.blaze.service.RoleQueryService;
import com.venturedive.blaze.service.RoleService;
import com.venturedive.blaze.service.criteria.RoleCriteria;
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
 * REST controller for managing {@link com.venturedive.blaze.domain.Role}.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "role";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    private final RoleQueryService roleQueryService;

    public RoleResource(RoleService roleService, RoleRepository roleRepository, RoleQueryService roleQueryService) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.roleQueryService = roleQueryService;
    }

    /**
     * {@code POST  /roles} : Create a new role.
     *
     * @param role the role to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new role, or with status {@code 400 (Bad Request)} if the role has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to save Role : {}", role);
        if (role.getId() != null) {
            throw new BadRequestAlertException("A new role cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Role result = roleService.save(role);
        return ResponseEntity
            .created(new URI("/api/roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /roles/:id} : Updates an existing role.
     *
     * @param id the id of the role to save.
     * @param role the role to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated role,
     * or with status {@code 400 (Bad Request)} if the role is not valid,
     * or with status {@code 500 (Internal Server Error)} if the role couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Role role)
        throws URISyntaxException {
        log.debug("REST request to update Role : {}, {}", id, role);
        if (role.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, role.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Role result = roleService.update(role);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, role.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /roles/:id} : Partial updates given fields of an existing role, field will ignore if it is null
     *
     * @param id the id of the role to save.
     * @param role the role to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated role,
     * or with status {@code 400 (Bad Request)} if the role is not valid,
     * or with status {@code 404 (Not Found)} if the role is not found,
     * or with status {@code 500 (Internal Server Error)} if the role couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Role> partialUpdateRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Role role
    ) throws URISyntaxException {
        log.debug("REST request to partial update Role partially : {}, {}", id, role);
        if (role.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, role.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Role> result = roleService.partialUpdate(role);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, role.getId().toString())
        );
    }

    /**
     * {@code GET  /roles} : get all the roles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles in body.
     */
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles(RoleCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Roles by criteria: {}", criteria);
        Page<Role> page = roleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /roles/count} : count all the roles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/roles/count")
    public ResponseEntity<Long> countRoles(RoleCriteria criteria) {
        log.debug("REST request to count Roles by criteria: {}", criteria);
        return ResponseEntity.ok().body(roleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /roles/:id} : get the "id" role.
     *
     * @param id the id of the role to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the role, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Optional<Role> role = roleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(role);
    }

    /**
     * {@code DELETE  /roles/:id} : delete the "id" role.
     *
     * @param id the id of the role to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        roleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
