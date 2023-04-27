package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestCaseField.
 */
@Entity
@Table(name = "test_case_field")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 65535)
    @Column(name = "expected_result", length = 65535)
    private String expectedResult;

    @Size(max = 255)
    @Column(name = "value", length = 255)
    private String value;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "company", "templateFieldType", "testcasefieldTemplatefields", "templates" }, allowSetters = true)
    private TemplateField templateField;

    @ManyToOne(optional = false)
    @NotNull
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
    private TestCase testCase;

    @OneToMany(mappedBy = "testCaseField")
    @JsonIgnoreProperties(value = { "testCaseField" }, allowSetters = true)
    private Set<TestCaseFieldAttachment> testcasefieldattachmentTestcasefields = new HashSet<>();

    @OneToMany(mappedBy = "stepDetail")
    @JsonIgnoreProperties(
        value = { "testRunDetail", "stepDetail", "status", "testrunstepdetailattachmentTestrunstepdetails" },
        allowSetters = true
    )
    private Set<TestRunStepDetails> testrunstepdetailsStepdetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCaseField id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpectedResult() {
        return this.expectedResult;
    }

    public TestCaseField expectedResult(String expectedResult) {
        this.setExpectedResult(expectedResult);
        return this;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getValue() {
        return this.value;
    }

    public TestCaseField value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TemplateField getTemplateField() {
        return this.templateField;
    }

    public void setTemplateField(TemplateField templateField) {
        this.templateField = templateField;
    }

    public TestCaseField templateField(TemplateField templateField) {
        this.setTemplateField(templateField);
        return this;
    }

    public TestCase getTestCase() {
        return this.testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public TestCaseField testCase(TestCase testCase) {
        this.setTestCase(testCase);
        return this;
    }

    public Set<TestCaseFieldAttachment> getTestcasefieldattachmentTestcasefields() {
        return this.testcasefieldattachmentTestcasefields;
    }

    public void setTestcasefieldattachmentTestcasefields(Set<TestCaseFieldAttachment> testCaseFieldAttachments) {
        if (this.testcasefieldattachmentTestcasefields != null) {
            this.testcasefieldattachmentTestcasefields.forEach(i -> i.setTestCaseField(null));
        }
        if (testCaseFieldAttachments != null) {
            testCaseFieldAttachments.forEach(i -> i.setTestCaseField(this));
        }
        this.testcasefieldattachmentTestcasefields = testCaseFieldAttachments;
    }

    public TestCaseField testcasefieldattachmentTestcasefields(Set<TestCaseFieldAttachment> testCaseFieldAttachments) {
        this.setTestcasefieldattachmentTestcasefields(testCaseFieldAttachments);
        return this;
    }

    public TestCaseField addTestcasefieldattachmentTestcasefield(TestCaseFieldAttachment testCaseFieldAttachment) {
        this.testcasefieldattachmentTestcasefields.add(testCaseFieldAttachment);
        testCaseFieldAttachment.setTestCaseField(this);
        return this;
    }

    public TestCaseField removeTestcasefieldattachmentTestcasefield(TestCaseFieldAttachment testCaseFieldAttachment) {
        this.testcasefieldattachmentTestcasefields.remove(testCaseFieldAttachment);
        testCaseFieldAttachment.setTestCaseField(null);
        return this;
    }

    public Set<TestRunStepDetails> getTestrunstepdetailsStepdetails() {
        return this.testrunstepdetailsStepdetails;
    }

    public void setTestrunstepdetailsStepdetails(Set<TestRunStepDetails> testRunStepDetails) {
        if (this.testrunstepdetailsStepdetails != null) {
            this.testrunstepdetailsStepdetails.forEach(i -> i.setStepDetail(null));
        }
        if (testRunStepDetails != null) {
            testRunStepDetails.forEach(i -> i.setStepDetail(this));
        }
        this.testrunstepdetailsStepdetails = testRunStepDetails;
    }

    public TestCaseField testrunstepdetailsStepdetails(Set<TestRunStepDetails> testRunStepDetails) {
        this.setTestrunstepdetailsStepdetails(testRunStepDetails);
        return this;
    }

    public TestCaseField addTestrunstepdetailsStepdetail(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsStepdetails.add(testRunStepDetails);
        testRunStepDetails.setStepDetail(this);
        return this;
    }

    public TestCaseField removeTestrunstepdetailsStepdetail(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsStepdetails.remove(testRunStepDetails);
        testRunStepDetails.setStepDetail(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseField)) {
            return false;
        }
        return id != null && id.equals(((TestCaseField) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseField{" +
            "id=" + getId() +
            ", expectedResult='" + getExpectedResult() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
