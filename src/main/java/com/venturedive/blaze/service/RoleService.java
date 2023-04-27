package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.Role;
import com.venturedive.blaze.repository.RoleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Role}.
 */
@Service
@Transactional
public class RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Save a role.
     *
     * @param role the entity to save.
     * @return the persisted entity.
     */
    public Role save(Role role) {
        log.debug("Request to save Role : {}", role);
        return roleRepository.save(role);
    }

    /**
     * Update a role.
     *
     * @param role the entity to save.
     * @return the persisted entity.
     */
    public Role update(Role role) {
        log.debug("Request to update Role : {}", role);
        return roleRepository.save(role);
    }

    /**
     * Partially update a role.
     *
     * @param role the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Role> partialUpdate(Role role) {
        log.debug("Request to partially update Role : {}", role);

        return roleRepository
            .findById(role.getId())
            .map(existingRole -> {
                if (role.getRoleName() != null) {
                    existingRole.setRoleName(role.getRoleName());
                }
                if (role.getIsdefault() != null) {
                    existingRole.setIsdefault(role.getIsdefault());
                }

                return existingRole;
            })
            .map(roleRepository::save);
    }

    /**
     * Get all the roles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Role> findAll(Pageable pageable) {
        log.debug("Request to get all Roles");
        return roleRepository.findAll(pageable);
    }

    /**
     * Get all the roles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Role> findAllWithEagerRelationships(Pageable pageable) {
        return roleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one role by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Role> findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        return roleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the role by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        roleRepository.deleteById(id);
    }
}
