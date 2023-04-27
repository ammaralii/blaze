package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestRunStepDetails.
 */
@Entity
@Table(name = "test_run_step_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunStepDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 65535)
    @Column(name = "actual_result", length = 65535)
    private String actualResult;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "testRun", "testCase", "status", "testrundetailattachmentTestrundetails", "testrunstepdetailsTestrundetails" },
        allowSetters = true
    )
    private TestRunDetails testRunDetail;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "templateField", "testCase", "testcasefieldattachmentTestcasefields", "testrunstepdetailsStepdetails" },
        allowSetters = true
    )
    private TestCaseField stepDetail;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testrundetailsStatuses", "testrunstepdetailsStatuses" }, allowSetters = true)
    private TestStatus status;

    @OneToMany(mappedBy = "testRunStepDetail")
    @JsonIgnoreProperties(value = { "testRunStepDetail" }, allowSetters = true)
    private Set<TestRunStepDetailAttachment> testrunstepdetailattachmentTestrunstepdetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestRunStepDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActualResult() {
        return this.actualResult;
    }

    public TestRunStepDetails actualResult(String actualResult) {
        this.setActualResult(actualResult);
        return this;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }

    public TestRunDetails getTestRunDetail() {
        return this.testRunDetail;
    }

    public void setTestRunDetail(TestRunDetails testRunDetails) {
        this.testRunDetail = testRunDetails;
    }

    public TestRunStepDetails testRunDetail(TestRunDetails testRunDetails) {
        this.setTestRunDetail(testRunDetails);
        return this;
    }

    public TestCaseField getStepDetail() {
        return this.stepDetail;
    }

    public void setStepDetail(TestCaseField testCaseField) {
        this.stepDetail = testCaseField;
    }

    public TestRunStepDetails stepDetail(TestCaseField testCaseField) {
        this.setStepDetail(testCaseField);
        return this;
    }

    public TestStatus getStatus() {
        return this.status;
    }

    public void setStatus(TestStatus testStatus) {
        this.status = testStatus;
    }

    public TestRunStepDetails status(TestStatus testStatus) {
        this.setStatus(testStatus);
        return this;
    }

    public Set<TestRunStepDetailAttachment> getTestrunstepdetailattachmentTestrunstepdetails() {
        return this.testrunstepdetailattachmentTestrunstepdetails;
    }

    public void setTestrunstepdetailattachmentTestrunstepdetails(Set<TestRunStepDetailAttachment> testRunStepDetailAttachments) {
        if (this.testrunstepdetailattachmentTestrunstepdetails != null) {
            this.testrunstepdetailattachmentTestrunstepdetails.forEach(i -> i.setTestRunStepDetail(null));
        }
        if (testRunStepDetailAttachments != null) {
            testRunStepDetailAttachments.forEach(i -> i.setTestRunStepDetail(this));
        }
        this.testrunstepdetailattachmentTestrunstepdetails = testRunStepDetailAttachments;
    }

    public TestRunStepDetails testrunstepdetailattachmentTestrunstepdetails(Set<TestRunStepDetailAttachment> testRunStepDetailAttachments) {
        this.setTestrunstepdetailattachmentTestrunstepdetails(testRunStepDetailAttachments);
        return this;
    }

    public TestRunStepDetails addTestrunstepdetailattachmentTestrunstepdetail(TestRunStepDetailAttachment testRunStepDetailAttachment) {
        this.testrunstepdetailattachmentTestrunstepdetails.add(testRunStepDetailAttachment);
        testRunStepDetailAttachment.setTestRunStepDetail(this);
        return this;
    }

    public TestRunStepDetails removeTestrunstepdetailattachmentTestrunstepdetail(TestRunStepDetailAttachment testRunStepDetailAttachment) {
        this.testrunstepdetailattachmentTestrunstepdetails.remove(testRunStepDetailAttachment);
        testRunStepDetailAttachment.setTestRunStepDetail(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestRunStepDetails)) {
            return false;
        }
        return id != null && id.equals(((TestRunStepDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunStepDetails{" +
            "id=" + getId() +
            ", actualResult='" + getActualResult() + "'" +
            "}";
    }
}
