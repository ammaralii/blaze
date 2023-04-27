package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.ApplicationUser;
import com.venturedive.blaze.repository.ApplicationUserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApplicationUser}.
 */
@Service
@Transactional
public class ApplicationUserService {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserService.class);

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     * Save a applicationUser.
     *
     * @param applicationUser the entity to save.
     * @return the persisted entity.
     */
    public ApplicationUser save(ApplicationUser applicationUser) {
        log.debug("Request to save ApplicationUser : {}", applicationUser);
        return applicationUserRepository.save(applicationUser);
    }

    /**
     * Update a applicationUser.
     *
     * @param applicationUser the entity to save.
     * @return the persisted entity.
     */
    public ApplicationUser update(ApplicationUser applicationUser) {
        log.debug("Request to update ApplicationUser : {}", applicationUser);
        return applicationUserRepository.save(applicationUser);
    }

    /**
     * Partially update a applicationUser.
     *
     * @param applicationUser the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicationUser> partialUpdate(ApplicationUser applicationUser) {
        log.debug("Request to partially update ApplicationUser : {}", applicationUser);

        return applicationUserRepository
            .findById(applicationUser.getId())
            .map(existingApplicationUser -> {
                if (applicationUser.getFirstName() != null) {
                    existingApplicationUser.setFirstName(applicationUser.getFirstName());
                }
                if (applicationUser.getLastName() != null) {
                    existingApplicationUser.setLastName(applicationUser.getLastName());
                }
                if (applicationUser.getPassword() != null) {
                    existingApplicationUser.setPassword(applicationUser.getPassword());
                }
                if (applicationUser.getLastActive() != null) {
                    existingApplicationUser.setLastActive(applicationUser.getLastActive());
                }
                if (applicationUser.getStatus() != null) {
                    existingApplicationUser.setStatus(applicationUser.getStatus());
                }
                if (applicationUser.getCreatedBy() != null) {
                    existingApplicationUser.setCreatedBy(applicationUser.getCreatedBy());
                }
                if (applicationUser.getCreatedAt() != null) {
                    existingApplicationUser.setCreatedAt(applicationUser.getCreatedAt());
                }
                if (applicationUser.getUpdatedBy() != null) {
                    existingApplicationUser.setUpdatedBy(applicationUser.getUpdatedBy());
                }
                if (applicationUser.getUpdatedAt() != null) {
                    existingApplicationUser.setUpdatedAt(applicationUser.getUpdatedAt());
                }
                if (applicationUser.getUserEmail() != null) {
                    existingApplicationUser.setUserEmail(applicationUser.getUserEmail());
                }
                if (applicationUser.getIsDeleted() != null) {
                    existingApplicationUser.setIsDeleted(applicationUser.getIsDeleted());
                }

                return existingApplicationUser;
            })
            .map(applicationUserRepository::save);
    }

    /**
     * Get all the applicationUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationUser> findAll(Pageable pageable) {
        log.debug("Request to get all ApplicationUsers");
        return applicationUserRepository.findAll(pageable);
    }

    /**
     * Get all the applicationUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable) {
        return applicationUserRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one applicationUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationUser> findOne(Long id) {
        log.debug("Request to get ApplicationUser : {}", id);
        return applicationUserRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the applicationUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
    }
}
