package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "country", length = 255)
    private String country;

    @Size(max = 65535)
    @Column(name = "company_address", length = 65535)
    private String companyAddress;

    @Size(max = 255)
    @Column(name = "organization", length = 255)
    private String organization;

    @Column(name = "expected_no_of_users")
    private Integer expectedNoOfUsers;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "projects", "roles" }, allowSetters = true)
    private Set<ApplicationUser> applicationuserCompanies = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestoneProjects", "testplanProjects", "testsuiteProjects", "users" },
        allowSetters = true
    )
    private Set<Project> projectCompanies = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "templateFields", "projectDefaulttemplates", "testcaseTemplates" }, allowSetters = true)
    private Set<Template> templateCompanies = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "templateFieldType", "testcasefieldTemplatefields", "templates" }, allowSetters = true)
    private Set<TemplateField> templatefieldCompanies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return this.country;
    }

    public Company country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompanyAddress() {
        return this.companyAddress;
    }

    public Company companyAddress(String companyAddress) {
        this.setCompanyAddress(companyAddress);
        return this;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getOrganization() {
        return this.organization;
    }

    public Company organization(String organization) {
        this.setOrganization(organization);
        return this;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Integer getExpectedNoOfUsers() {
        return this.expectedNoOfUsers;
    }

    public Company expectedNoOfUsers(Integer expectedNoOfUsers) {
        this.setExpectedNoOfUsers(expectedNoOfUsers);
        return this;
    }

    public void setExpectedNoOfUsers(Integer expectedNoOfUsers) {
        this.expectedNoOfUsers = expectedNoOfUsers;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Company createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<ApplicationUser> getApplicationuserCompanies() {
        return this.applicationuserCompanies;
    }

    public void setApplicationuserCompanies(Set<ApplicationUser> applicationUsers) {
        if (this.applicationuserCompanies != null) {
            this.applicationuserCompanies.forEach(i -> i.setCompany(null));
        }
        if (applicationUsers != null) {
            applicationUsers.forEach(i -> i.setCompany(this));
        }
        this.applicationuserCompanies = applicationUsers;
    }

    public Company applicationuserCompanies(Set<ApplicationUser> applicationUsers) {
        this.setApplicationuserCompanies(applicationUsers);
        return this;
    }

    public Company addApplicationuserCompany(ApplicationUser applicationUser) {
        this.applicationuserCompanies.add(applicationUser);
        applicationUser.setCompany(this);
        return this;
    }

    public Company removeApplicationuserCompany(ApplicationUser applicationUser) {
        this.applicationuserCompanies.remove(applicationUser);
        applicationUser.setCompany(null);
        return this;
    }

    public Set<Project> getProjectCompanies() {
        return this.projectCompanies;
    }

    public void setProjectCompanies(Set<Project> projects) {
        if (this.projectCompanies != null) {
            this.projectCompanies.forEach(i -> i.setCompany(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setCompany(this));
        }
        this.projectCompanies = projects;
    }

    public Company projectCompanies(Set<Project> projects) {
        this.setProjectCompanies(projects);
        return this;
    }

    public Company addProjectCompany(Project project) {
        this.projectCompanies.add(project);
        project.setCompany(this);
        return this;
    }

    public Company removeProjectCompany(Project project) {
        this.projectCompanies.remove(project);
        project.setCompany(null);
        return this;
    }

    public Set<Template> getTemplateCompanies() {
        return this.templateCompanies;
    }

    public void setTemplateCompanies(Set<Template> templates) {
        if (this.templateCompanies != null) {
            this.templateCompanies.forEach(i -> i.setCompany(null));
        }
        if (templates != null) {
            templates.forEach(i -> i.setCompany(this));
        }
        this.templateCompanies = templates;
    }

    public Company templateCompanies(Set<Template> templates) {
        this.setTemplateCompanies(templates);
        return this;
    }

    public Company addTemplateCompany(Template template) {
        this.templateCompanies.add(template);
        template.setCompany(this);
        return this;
    }

    public Company removeTemplateCompany(Template template) {
        this.templateCompanies.remove(template);
        template.setCompany(null);
        return this;
    }

    public Set<TemplateField> getTemplatefieldCompanies() {
        return this.templatefieldCompanies;
    }

    public void setTemplatefieldCompanies(Set<TemplateField> templateFields) {
        if (this.templatefieldCompanies != null) {
            this.templatefieldCompanies.forEach(i -> i.setCompany(null));
        }
        if (templateFields != null) {
            templateFields.forEach(i -> i.setCompany(this));
        }
        this.templatefieldCompanies = templateFields;
    }

    public Company templatefieldCompanies(Set<TemplateField> templateFields) {
        this.setTemplatefieldCompanies(templateFields);
        return this;
    }

    public Company addTemplatefieldCompany(TemplateField templateField) {
        this.templatefieldCompanies.add(templateField);
        templateField.setCompany(this);
        return this;
    }

    public Company removeTemplatefieldCompany(TemplateField templateField) {
        this.templatefieldCompanies.remove(templateField);
        templateField.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", companyAddress='" + getCompanyAddress() + "'" +
            ", organization='" + getOrganization() + "'" +
            ", expectedNoOfUsers=" + getExpectedNoOfUsers() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
