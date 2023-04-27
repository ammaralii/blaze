package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "project_name", length = 255, nullable = false, unique = true)
    private String projectName;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "isactive")
    private Boolean isactive;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "company", "templateFields", "projectDefaulttemplates", "testcaseTemplates" }, allowSetters = true)
    private Template defaultTemplate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "applicationuserCompanies", "projectCompanies", "templateCompanies", "templatefieldCompanies" },
        allowSetters = true
    )
    private Company company;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestoneParentmilestones", "testcaseMilestones", "testrunMilestones" },
        allowSetters = true
    )
    private Set<Milestone> milestoneProjects = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = { "project" }, allowSetters = true)
    private Set<TestPlan> testplanProjects = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = { "project", "sectionTestsuites", "testcaseTestsuites" }, allowSetters = true)
    private Set<TestSuite> testsuiteProjects = new HashSet<>();

    @ManyToMany(mappedBy = "projects")
    @JsonIgnoreProperties(value = { "company", "projects", "roles" }, allowSetters = true)
    private Set<ApplicationUser> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Project id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public Project projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return this.description;
    }

    public Project description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsactive() {
        return this.isactive;
    }

    public Project isactive(Boolean isactive) {
        this.setIsactive(isactive);
        return this;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public Project createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Project createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public Project updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Project updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Template getDefaultTemplate() {
        return this.defaultTemplate;
    }

    public void setDefaultTemplate(Template template) {
        this.defaultTemplate = template;
    }

    public Project defaultTemplate(Template template) {
        this.setDefaultTemplate(template);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Project company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<Milestone> getMilestoneProjects() {
        return this.milestoneProjects;
    }

    public void setMilestoneProjects(Set<Milestone> milestones) {
        if (this.milestoneProjects != null) {
            this.milestoneProjects.forEach(i -> i.setProject(null));
        }
        if (milestones != null) {
            milestones.forEach(i -> i.setProject(this));
        }
        this.milestoneProjects = milestones;
    }

    public Project milestoneProjects(Set<Milestone> milestones) {
        this.setMilestoneProjects(milestones);
        return this;
    }

    public Project addMilestoneProject(Milestone milestone) {
        this.milestoneProjects.add(milestone);
        milestone.setProject(this);
        return this;
    }

    public Project removeMilestoneProject(Milestone milestone) {
        this.milestoneProjects.remove(milestone);
        milestone.setProject(null);
        return this;
    }

    public Set<TestPlan> getTestplanProjects() {
        return this.testplanProjects;
    }

    public void setTestplanProjects(Set<TestPlan> testPlans) {
        if (this.testplanProjects != null) {
            this.testplanProjects.forEach(i -> i.setProject(null));
        }
        if (testPlans != null) {
            testPlans.forEach(i -> i.setProject(this));
        }
        this.testplanProjects = testPlans;
    }

    public Project testplanProjects(Set<TestPlan> testPlans) {
        this.setTestplanProjects(testPlans);
        return this;
    }

    public Project addTestplanProject(TestPlan testPlan) {
        this.testplanProjects.add(testPlan);
        testPlan.setProject(this);
        return this;
    }

    public Project removeTestplanProject(TestPlan testPlan) {
        this.testplanProjects.remove(testPlan);
        testPlan.setProject(null);
        return this;
    }

    public Set<TestSuite> getTestsuiteProjects() {
        return this.testsuiteProjects;
    }

    public void setTestsuiteProjects(Set<TestSuite> testSuites) {
        if (this.testsuiteProjects != null) {
            this.testsuiteProjects.forEach(i -> i.setProject(null));
        }
        if (testSuites != null) {
            testSuites.forEach(i -> i.setProject(this));
        }
        this.testsuiteProjects = testSuites;
    }

    public Project testsuiteProjects(Set<TestSuite> testSuites) {
        this.setTestsuiteProjects(testSuites);
        return this;
    }

    public Project addTestsuiteProject(TestSuite testSuite) {
        this.testsuiteProjects.add(testSuite);
        testSuite.setProject(this);
        return this;
    }

    public Project removeTestsuiteProject(TestSuite testSuite) {
        this.testsuiteProjects.remove(testSuite);
        testSuite.setProject(null);
        return this;
    }

    public Set<ApplicationUser> getUsers() {
        return this.users;
    }

    public void setUsers(Set<ApplicationUser> applicationUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeProject(this));
        }
        if (applicationUsers != null) {
            applicationUsers.forEach(i -> i.addProject(this));
        }
        this.users = applicationUsers;
    }

    public Project users(Set<ApplicationUser> applicationUsers) {
        this.setUsers(applicationUsers);
        return this;
    }

    public Project addUser(ApplicationUser applicationUser) {
        this.users.add(applicationUser);
        applicationUser.getProjects().add(this);
        return this;
    }

    public Project removeUser(ApplicationUser applicationUser) {
        this.users.remove(applicationUser);
        applicationUser.getProjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", projectName='" + getProjectName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isactive='" + getIsactive() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
