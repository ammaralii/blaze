package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TemplateField.
 */
@Entity
@Table(name = "template_field")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "field_name", length = 255)
    private String fieldName;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "applicationuserCompanies", "projectCompanies", "templateCompanies", "templatefieldCompanies" },
        allowSetters = true
    )
    private Company company;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "templatefieldTemplatefieldtypes" }, allowSetters = true)
    private TemplateFieldType templateFieldType;

    @OneToMany(mappedBy = "templateField")
    @JsonIgnoreProperties(
        value = { "templateField", "testCase", "testcasefieldattachmentTestcasefields", "testrunstepdetailsStepdetails" },
        allowSetters = true
    )
    private Set<TestCaseField> testcasefieldTemplatefields = new HashSet<>();

    @ManyToMany(mappedBy = "templateFields")
    @JsonIgnoreProperties(value = { "company", "templateFields", "projectDefaulttemplates", "testcaseTemplates" }, allowSetters = true)
    private Set<Template> templates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TemplateField id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public TemplateField fieldName(String fieldName) {
        this.setFieldName(fieldName);
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public TemplateField company(Company company) {
        this.setCompany(company);
        return this;
    }

    public TemplateFieldType getTemplateFieldType() {
        return this.templateFieldType;
    }

    public void setTemplateFieldType(TemplateFieldType templateFieldType) {
        this.templateFieldType = templateFieldType;
    }

    public TemplateField templateFieldType(TemplateFieldType templateFieldType) {
        this.setTemplateFieldType(templateFieldType);
        return this;
    }

    public Set<TestCaseField> getTestcasefieldTemplatefields() {
        return this.testcasefieldTemplatefields;
    }

    public void setTestcasefieldTemplatefields(Set<TestCaseField> testCaseFields) {
        if (this.testcasefieldTemplatefields != null) {
            this.testcasefieldTemplatefields.forEach(i -> i.setTemplateField(null));
        }
        if (testCaseFields != null) {
            testCaseFields.forEach(i -> i.setTemplateField(this));
        }
        this.testcasefieldTemplatefields = testCaseFields;
    }

    public TemplateField testcasefieldTemplatefields(Set<TestCaseField> testCaseFields) {
        this.setTestcasefieldTemplatefields(testCaseFields);
        return this;
    }

    public TemplateField addTestcasefieldTemplatefield(TestCaseField testCaseField) {
        this.testcasefieldTemplatefields.add(testCaseField);
        testCaseField.setTemplateField(this);
        return this;
    }

    public TemplateField removeTestcasefieldTemplatefield(TestCaseField testCaseField) {
        this.testcasefieldTemplatefields.remove(testCaseField);
        testCaseField.setTemplateField(null);
        return this;
    }

    public Set<Template> getTemplates() {
        return this.templates;
    }

    public void setTemplates(Set<Template> templates) {
        if (this.templates != null) {
            this.templates.forEach(i -> i.removeTemplateField(this));
        }
        if (templates != null) {
            templates.forEach(i -> i.addTemplateField(this));
        }
        this.templates = templates;
    }

    public TemplateField templates(Set<Template> templates) {
        this.setTemplates(templates);
        return this;
    }

    public TemplateField addTemplate(Template template) {
        this.templates.add(template);
        template.getTemplateFields().add(this);
        return this;
    }

    public TemplateField removeTemplate(Template template) {
        this.templates.remove(template);
        template.getTemplateFields().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateField)) {
            return false;
        }
        return id != null && id.equals(((TemplateField) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateField{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
