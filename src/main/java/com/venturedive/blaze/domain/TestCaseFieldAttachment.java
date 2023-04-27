package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestCaseFieldAttachment.
 */
@Entity
@Table(name = "test_case_field_attachment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseFieldAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 65535)
    @Column(name = "url", length = 65535, nullable = false)
    private String url;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "templateField", "testCase", "testcasefieldattachmentTestcasefields", "testrunstepdetailsStepdetails" },
        allowSetters = true
    )
    private TestCaseField testCaseField;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCaseFieldAttachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public TestCaseFieldAttachment url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TestCaseField getTestCaseField() {
        return this.testCaseField;
    }

    public void setTestCaseField(TestCaseField testCaseField) {
        this.testCaseField = testCaseField;
    }

    public TestCaseFieldAttachment testCaseField(TestCaseField testCaseField) {
        this.setTestCaseField(testCaseField);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseFieldAttachment)) {
            return false;
        }
        return id != null && id.equals(((TestCaseFieldAttachment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseFieldAttachment{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
