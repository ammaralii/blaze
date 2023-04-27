package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Template.
 */
@Entity
@Table(name = "template")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "template_name", length = 255)
    private String templateName;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "applicationuserCompanies", "projectCompanies", "templateCompanies", "templatefieldCompanies" },
        allowSetters = true
    )
    private Company company;

    @ManyToMany
    @JoinTable(
        name = "rel_template__template_field",
        joinColumns = @JoinColumn(name = "template_id"),
        inverseJoinColumns = @JoinColumn(name = "template_field_id")
    )
    @JsonIgnoreProperties(value = { "company", "templateFieldType", "testcasefieldTemplatefields", "templates" }, allowSetters = true)
    private Set<TemplateField> templateFields = new HashSet<>();

    @OneToMany(mappedBy = "defaultTemplate")
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestoneProjects", "testplanProjects", "testsuiteProjects", "users" },
        allowSetters = true
    )
    private Set<Project> projectDefaulttemplates = new HashSet<>();

    @OneToMany(mappedBy = "template")
    @JsonIgnoreProperties(
        value = {
            "testSuite",
            "section",
            "priority",
            "template",
            "milestone",
            "testLevels",
            "testcaseattachmentTestcases",
            "testcasefieldTestcases",
            "testrundetailsTestcases",
        },
        allowSetters = true
    )
    private Set<TestCase> testcaseTemplates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Template id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public Template templateName(String templateName) {
        this.setTemplateName(templateName);
        return this;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Template createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public Template createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Template company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<TemplateField> getTemplateFields() {
        return this.templateFields;
    }

    public void setTemplateFields(Set<TemplateField> templateFields) {
        this.templateFields = templateFields;
    }

    public Template templateFields(Set<TemplateField> templateFields) {
        this.setTemplateFields(templateFields);
        return this;
    }

    public Template addTemplateField(TemplateField templateField) {
        this.templateFields.add(templateField);
        templateField.getTemplates().add(this);
        return this;
    }

    public Template removeTemplateField(TemplateField templateField) {
        this.templateFields.remove(templateField);
        templateField.getTemplates().remove(this);
        return this;
    }

    public Set<Project> getProjectDefaulttemplates() {
        return this.projectDefaulttemplates;
    }

    public void setProjectDefaulttemplates(Set<Project> projects) {
        if (this.projectDefaulttemplates != null) {
            this.projectDefaulttemplates.forEach(i -> i.setDefaultTemplate(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setDefaultTemplate(this));
        }
        this.projectDefaulttemplates = projects;
    }

    public Template projectDefaulttemplates(Set<Project> projects) {
        this.setProjectDefaulttemplates(projects);
        return this;
    }

    public Template addProjectDefaulttemplate(Project project) {
        this.projectDefaulttemplates.add(project);
        project.setDefaultTemplate(this);
        return this;
    }

    public Template removeProjectDefaulttemplate(Project project) {
        this.projectDefaulttemplates.remove(project);
        project.setDefaultTemplate(null);
        return this;
    }

    public Set<TestCase> getTestcaseTemplates() {
        return this.testcaseTemplates;
    }

    public void setTestcaseTemplates(Set<TestCase> testCases) {
        if (this.testcaseTemplates != null) {
            this.testcaseTemplates.forEach(i -> i.setTemplate(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setTemplate(this));
        }
        this.testcaseTemplates = testCases;
    }

    public Template testcaseTemplates(Set<TestCase> testCases) {
        this.setTestcaseTemplates(testCases);
        return this;
    }

    public Template addTestcaseTemplate(TestCase testCase) {
        this.testcaseTemplates.add(testCase);
        testCase.setTemplate(this);
        return this;
    }

    public Template removeTestcaseTemplate(TestCase testCase) {
        this.testcaseTemplates.remove(testCase);
        testCase.setTemplate(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Template)) {
            return false;
        }
        return id != null && id.equals(((Template) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Template{" +
            "id=" + getId() +
            ", templateName='" + getTemplateName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
