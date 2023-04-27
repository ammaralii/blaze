package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.Permission;
import com.venturedive.blaze.domain.Role;
import com.venturedive.blaze.repository.PermissionRepository;
import com.venturedive.blaze.service.criteria.PermissionCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PermissionResourceIT {

    private static final String DEFAULT_PERMISSION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSION_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionMockMvc;

    private Permission permission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createEntity(EntityManager em) {
        Permission permission = new Permission().permissionName(DEFAULT_PERMISSION_NAME);
        return permission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createUpdatedEntity(EntityManager em) {
        Permission permission = new Permission().permissionName(UPDATED_PERMISSION_NAME);
        return permission;
    }

    @BeforeEach
    public void initTest() {
        permission = createEntity(em);
    }

    @Test
    @Transactional
    void createPermission() throws Exception {
        int databaseSizeBeforeCreate = permissionRepository.findAll().size();
        // Create the Permission
        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isCreated());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate + 1);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getPermissionName()).isEqualTo(DEFAULT_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void createPermissionWithExistingId() throws Exception {
        // Create the Permission with an existing ID
        permission.setId(1L);

        int databaseSizeBeforeCreate = permissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPermissionNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setPermissionName(null);

        // Create the Permission, which fails.

        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPermissions() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())))
            .andExpect(jsonPath("$.[*].permissionName").value(hasItem(DEFAULT_PERMISSION_NAME)));
    }

    @Test
    @Transactional
    void getPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get the permission
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, permission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permission.getId().intValue()))
            .andExpect(jsonPath("$.permissionName").value(DEFAULT_PERMISSION_NAME));
    }

    @Test
    @Transactional
    void getPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        Long id = permission.getId();

        defaultPermissionShouldBeFound("id.equals=" + id);
        defaultPermissionShouldNotBeFound("id.notEquals=" + id);

        defaultPermissionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPermissionShouldNotBeFound("id.greaterThan=" + id);

        defaultPermissionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPermissionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where permissionName equals to DEFAULT_PERMISSION_NAME
        defaultPermissionShouldBeFound("permissionName.equals=" + DEFAULT_PERMISSION_NAME);

        // Get all the permissionList where permissionName equals to UPDATED_PERMISSION_NAME
        defaultPermissionShouldNotBeFound("permissionName.equals=" + UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where permissionName in DEFAULT_PERMISSION_NAME or UPDATED_PERMISSION_NAME
        defaultPermissionShouldBeFound("permissionName.in=" + DEFAULT_PERMISSION_NAME + "," + UPDATED_PERMISSION_NAME);

        // Get all the permissionList where permissionName equals to UPDATED_PERMISSION_NAME
        defaultPermissionShouldNotBeFound("permissionName.in=" + UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where permissionName is not null
        defaultPermissionShouldBeFound("permissionName.specified=true");

        // Get all the permissionList where permissionName is null
        defaultPermissionShouldNotBeFound("permissionName.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameContainsSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where permissionName contains DEFAULT_PERMISSION_NAME
        defaultPermissionShouldBeFound("permissionName.contains=" + DEFAULT_PERMISSION_NAME);

        // Get all the permissionList where permissionName contains UPDATED_PERMISSION_NAME
        defaultPermissionShouldNotBeFound("permissionName.contains=" + UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameNotContainsSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where permissionName does not contain DEFAULT_PERMISSION_NAME
        defaultPermissionShouldNotBeFound("permissionName.doesNotContain=" + DEFAULT_PERMISSION_NAME);

        // Get all the permissionList where permissionName does not contain UPDATED_PERMISSION_NAME
        defaultPermissionShouldBeFound("permissionName.doesNotContain=" + UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionsByRoleIsEqualToSomething() throws Exception {
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            permissionRepository.saveAndFlush(permission);
            role = RoleResourceIT.createEntity(em);
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        em.persist(role);
        em.flush();
        permission.addRole(role);
        permissionRepository.saveAndFlush(permission);
        Long roleId = role.getId();

        // Get all the permissionList where role equals to roleId
        defaultPermissionShouldBeFound("roleId.equals=" + roleId);

        // Get all the permissionList where role equals to (roleId + 1)
        defaultPermissionShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPermissionShouldBeFound(String filter) throws Exception {
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())))
            .andExpect(jsonPath("$.[*].permissionName").value(hasItem(DEFAULT_PERMISSION_NAME)));

        // Check, that the count call also returns 1
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPermissionShouldNotBeFound(String filter) throws Exception {
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPermission() throws Exception {
        // Get the permission
        restPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission
        Permission updatedPermission = permissionRepository.findById(permission.getId()).get();
        // Disconnect from session so that the updates on updatedPermission are not directly saved in db
        em.detach(updatedPermission);
        updatedPermission.permissionName(UPDATED_PERMISSION_NAME);

        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPermission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPermission))
            )
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getPermissionName()).isEqualTo(UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissionWithPatch() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission using partial update
        Permission partialUpdatedPermission = new Permission();
        partialUpdatedPermission.setId(permission.getId());

        partialUpdatedPermission.permissionName(UPDATED_PERMISSION_NAME);

        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermission))
            )
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getPermissionName()).isEqualTo(UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePermissionWithPatch() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission using partial update
        Permission partialUpdatedPermission = new Permission();
        partialUpdatedPermission.setId(permission.getId());

        partialUpdatedPermission.permissionName(UPDATED_PERMISSION_NAME);

        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermission))
            )
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getPermissionName()).isEqualTo(UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(permission))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeDelete = permissionRepository.findAll().size();

        // Delete the permission
        restPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, permission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
