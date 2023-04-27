package com.venturedive.blaze.service;

import com.venturedive.blaze.domain.Project;
import com.venturedive.blaze.repository.ProjectRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Project}.
 */
@Service
@Transactional
public class ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Save a project.
     *
     * @param project the entity to save.
     * @return the persisted entity.
     */
    public Project save(Project project) {
        log.debug("Request to save Project : {}", project);
        return projectRepository.save(project);
    }

    /**
     * Update a project.
     *
     * @param project the entity to save.
     * @return the persisted entity.
     */
    public Project update(Project project) {
        log.debug("Request to update Project : {}", project);
        return projectRepository.save(project);
    }

    /**
     * Partially update a project.
     *
     * @param project the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Project> partialUpdate(Project project) {
        log.debug("Request to partially update Project : {}", project);

        return projectRepository
            .findById(project.getId())
            .map(existingProject -> {
                if (project.getProjectName() != null) {
                    existingProject.setProjectName(project.getProjectName());
                }
                if (project.getDescription() != null) {
                    existingProject.setDescription(project.getDescription());
                }
                if (project.getIsactive() != null) {
                    existingProject.setIsactive(project.getIsactive());
                }
                if (project.getCreatedBy() != null) {
                    existingProject.setCreatedBy(project.getCreatedBy());
                }
                if (project.getCreatedAt() != null) {
                    existingProject.setCreatedAt(project.getCreatedAt());
                }
                if (project.getUpdatedBy() != null) {
                    existingProject.setUpdatedBy(project.getUpdatedBy());
                }
                if (project.getUpdatedAt() != null) {
                    existingProject.setUpdatedAt(project.getUpdatedAt());
                }

                return existingProject;
            })
            .map(projectRepository::save);
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Project> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(pageable);
    }

    /**
     * Get one project by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Project> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }
}
