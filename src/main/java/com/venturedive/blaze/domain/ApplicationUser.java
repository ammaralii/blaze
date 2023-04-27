package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @Size(max = 255)
    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "last_active")
    private Instant lastActive;

    @Size(max = 255)
    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 255)
    @Column(name = "user_email", length = 255, unique = true)
    private String userEmail;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "applicationuserCompanies", "projectCompanies", "templateCompanies", "templatefieldCompanies" },
        allowSetters = true
    )
    private Company company;

    @ManyToMany
    @JoinTable(
        name = "rel_application_user__project",
        joinColumns = @JoinColumn(name = "application_user_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestoneProjects", "testplanProjects", "testsuiteProjects", "users" },
        allowSetters = true
    )
    private Set<Project> projects = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_application_user__role",
        joinColumns = @JoinColumn(name = "application_user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnoreProperties(value = { "permissions", "users" }, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public ApplicationUser firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public ApplicationUser lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public ApplicationUser password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getLastActive() {
        return this.lastActive;
    }

    public ApplicationUser lastActive(Instant lastActive) {
        this.setLastActive(lastActive);
        return this;
    }

    public void setLastActive(Instant lastActive) {
        this.lastActive = lastActive;
    }

    public String getStatus() {
        return this.status;
    }

    public ApplicationUser status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public ApplicationUser createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ApplicationUser createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public ApplicationUser updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ApplicationUser updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public ApplicationUser userEmail(String userEmail) {
        this.setUserEmail(userEmail);
        return this;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public ApplicationUser isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ApplicationUser company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public ApplicationUser projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public ApplicationUser addProject(Project project) {
        this.projects.add(project);
        project.getUsers().add(this);
        return this;
    }

    public ApplicationUser removeProject(Project project) {
        this.projects.remove(project);
        project.getUsers().remove(this);
        return this;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public ApplicationUser roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public ApplicationUser addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
        return this;
    }

    public ApplicationUser removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", password='" + getPassword() + "'" +
            ", lastActive='" + getLastActive() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", userEmail='" + getUserEmail() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
