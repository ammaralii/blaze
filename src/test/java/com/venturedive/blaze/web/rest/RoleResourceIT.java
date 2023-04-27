package com.venturedive.blaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blaze.IntegrationTest;
import com.venturedive.blaze.domain.ApplicationUser;
import com.venturedive.blaze.domain.Permission;
import com.venturedive.blaze.domain.Role;
import com.venturedive.blaze.repository.RoleRepository;
import com.venturedive.blaze.service.RoleService;
import com.venturedive.blaze.service.criteria.RoleCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RoleResourceIT {

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ISDEFAULT = false;
    private static final Boolean UPDATED_ISDEFAULT = true;

    private static final String ENTITY_API_URL = "/api/roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private RoleRepository roleRepositoryMock;

    @Mock
    private RoleService roleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleMockMvc;

    private Role role;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = new Role().roleName(DEFAULT_ROLE_NAME).isdefault(DEFAULT_ISDEFAULT);
        return role;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createUpdatedEntity(EntityManager em) {
        Role role = new Role().roleName(UPDATED_ROLE_NAME).isdefault(UPDATED_ISDEFAULT);
        return role;
    }

    @BeforeEach
    public void initTest() {
        role = createEntity(em);
    }

    @Test
    @Transactional
    void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();
        // Create the Role
        restRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);
        assertThat(testRole.getIsdefault()).isEqualTo(DEFAULT_ISDEFAULT);
    }

    @Test
    @Transactional
    void createRoleWithExistingId() throws Exception {
        // Create the Role with an existing ID
        role.setId(1L);

        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleRepository.findAll().size();
        // set the field null
        role.setRoleName(null);

        // Create the Role, which fails.

        restRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isBadRequest());

        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoles() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)))
            .andExpect(jsonPath("$.[*].isdefault").value(hasItem(DEFAULT_ISDEFAULT.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(roleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(roleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(roleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(roleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(role.getId().intValue()))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME))
            .andExpect(jsonPath("$.isdefault").value(DEFAULT_ISDEFAULT.booleanValue()));
    }

    @Test
    @Transactional
    void getRolesByIdFiltering() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        Long id = role.getId();

        defaultRoleShouldBeFound("id.equals=" + id);
        defaultRoleShouldNotBeFound("id.notEquals=" + id);

        defaultRoleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRoleShouldNotBeFound("id.greaterThan=" + id);

        defaultRoleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRoleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where roleName equals to DEFAULT_ROLE_NAME
        defaultRoleShouldBeFound("roleName.equals=" + DEFAULT_ROLE_NAME);

        // Get all the roleList where roleName equals to UPDATED_ROLE_NAME
        defaultRoleShouldNotBeFound("roleName.equals=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameIsInShouldWork() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where roleName in DEFAULT_ROLE_NAME or UPDATED_ROLE_NAME
        defaultRoleShouldBeFound("roleName.in=" + DEFAULT_ROLE_NAME + "," + UPDATED_ROLE_NAME);

        // Get all the roleList where roleName equals to UPDATED_ROLE_NAME
        defaultRoleShouldNotBeFound("roleName.in=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where roleName is not null
        defaultRoleShouldBeFound("roleName.specified=true");

        // Get all the roleList where roleName is null
        defaultRoleShouldNotBeFound("roleName.specified=false");
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameContainsSomething() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where roleName contains DEFAULT_ROLE_NAME
        defaultRoleShouldBeFound("roleName.contains=" + DEFAULT_ROLE_NAME);

        // Get all the roleList where roleName contains UPDATED_ROLE_NAME
        defaultRoleShouldNotBeFound("roleName.contains=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByRoleNameNotContainsSomething() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where roleName does not contain DEFAULT_ROLE_NAME
        defaultRoleShouldNotBeFound("roleName.doesNotContain=" + DEFAULT_ROLE_NAME);

        // Get all the roleList where roleName does not contain UPDATED_ROLE_NAME
        defaultRoleShouldBeFound("roleName.doesNotContain=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByIsdefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where isdefault equals to DEFAULT_ISDEFAULT
        defaultRoleShouldBeFound("isdefault.equals=" + DEFAULT_ISDEFAULT);

        // Get all the roleList where isdefault equals to UPDATED_ISDEFAULT
        defaultRoleShouldNotBeFound("isdefault.equals=" + UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void getAllRolesByIsdefaultIsInShouldWork() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where isdefault in DEFAULT_ISDEFAULT or UPDATED_ISDEFAULT
        defaultRoleShouldBeFound("isdefault.in=" + DEFAULT_ISDEFAULT + "," + UPDATED_ISDEFAULT);

        // Get all the roleList where isdefault equals to UPDATED_ISDEFAULT
        defaultRoleShouldNotBeFound("isdefault.in=" + UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void getAllRolesByIsdefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where isdefault is not null
        defaultRoleShouldBeFound("isdefault.specified=true");

        // Get all the roleList where isdefault is null
        defaultRoleShouldNotBeFound("isdefault.specified=false");
    }

    @Test
    @Transactional
    void getAllRolesByPermissionIsEqualToSomething() throws Exception {
        Permission permission;
        if (TestUtil.findAll(em, Permission.class).isEmpty()) {
            roleRepository.saveAndFlush(role);
            permission = PermissionResourceIT.createEntity(em);
        } else {
            permission = TestUtil.findAll(em, Permission.class).get(0);
        }
        em.persist(permission);
        em.flush();
        role.addPermission(permission);
        roleRepository.saveAndFlush(role);
        Long permissionId = permission.getId();

        // Get all the roleList where permission equals to permissionId
        defaultRoleShouldBeFound("permissionId.equals=" + permissionId);

        // Get all the roleList where permission equals to (permissionId + 1)
        defaultRoleShouldNotBeFound("permissionId.equals=" + (permissionId + 1));
    }

    @Test
    @Transactional
    void getAllRolesByUserIsEqualToSomething() throws Exception {
        ApplicationUser user;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            roleRepository.saveAndFlush(role);
            user = ApplicationUserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(user);
        em.flush();
        role.addUser(user);
        roleRepository.saveAndFlush(role);
        Long userId = user.getId();

        // Get all the roleList where user equals to userId
        defaultRoleShouldBeFound("userId.equals=" + userId);

        // Get all the roleList where user equals to (userId + 1)
        defaultRoleShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoleShouldBeFound(String filter) throws Exception {
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)))
            .andExpect(jsonPath("$.[*].isdefault").value(hasItem(DEFAULT_ISDEFAULT.booleanValue())));

        // Check, that the count call also returns 1
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoleShouldNotBeFound(String filter) throws Exception {
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role
        Role updatedRole = roleRepository.findById(role.getId()).get();
        // Disconnect from session so that the updates on updatedRole are not directly saved in db
        em.detach(updatedRole);
        updatedRole.roleName(UPDATED_ROLE_NAME).isdefault(UPDATED_ISDEFAULT);

        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRole))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
        assertThat(testRole.getIsdefault()).isEqualTo(UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void putNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, role.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(role))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(role))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.isdefault(UPDATED_ISDEFAULT);

        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRole))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);
        assertThat(testRole.getIsdefault()).isEqualTo(UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void fullUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.roleName(UPDATED_ROLE_NAME).isdefault(UPDATED_ISDEFAULT);

        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRole))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
        assertThat(testRole.getIsdefault()).isEqualTo(UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void patchNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, role.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(role))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(role))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeDelete = roleRepository.findAll().size();

        // Delete the role
        restRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, role.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
