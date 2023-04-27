package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestStatus.
 */
@Entity
@Table(name = "test_status")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "status_name", length = 255)
    private String statusName;

    @OneToMany(mappedBy = "status")
    @JsonIgnoreProperties(
        value = { "testRun", "testCase", "status", "testrundetailattachmentTestrundetails", "testrunstepdetailsTestrundetails" },
        allowSetters = true
    )
    private Set<TestRunDetails> testrundetailsStatuses = new HashSet<>();

    @OneToMany(mappedBy = "status")
    @JsonIgnoreProperties(
        value = { "testRunDetail", "stepDetail", "status", "testrunstepdetailattachmentTestrunstepdetails" },
        allowSetters = true
    )
    private Set<TestRunStepDetails> testrunstepdetailsStatuses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public TestStatus statusName(String statusName) {
        this.setStatusName(statusName);
        return this;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Set<TestRunDetails> getTestrundetailsStatuses() {
        return this.testrundetailsStatuses;
    }

    public void setTestrundetailsStatuses(Set<TestRunDetails> testRunDetails) {
        if (this.testrundetailsStatuses != null) {
            this.testrundetailsStatuses.forEach(i -> i.setStatus(null));
        }
        if (testRunDetails != null) {
            testRunDetails.forEach(i -> i.setStatus(this));
        }
        this.testrundetailsStatuses = testRunDetails;
    }

    public TestStatus testrundetailsStatuses(Set<TestRunDetails> testRunDetails) {
        this.setTestrundetailsStatuses(testRunDetails);
        return this;
    }

    public TestStatus addTestrundetailsStatus(TestRunDetails testRunDetails) {
        this.testrundetailsStatuses.add(testRunDetails);
        testRunDetails.setStatus(this);
        return this;
    }

    public TestStatus removeTestrundetailsStatus(TestRunDetails testRunDetails) {
        this.testrundetailsStatuses.remove(testRunDetails);
        testRunDetails.setStatus(null);
        return this;
    }

    public Set<TestRunStepDetails> getTestrunstepdetailsStatuses() {
        return this.testrunstepdetailsStatuses;
    }

    public void setTestrunstepdetailsStatuses(Set<TestRunStepDetails> testRunStepDetails) {
        if (this.testrunstepdetailsStatuses != null) {
            this.testrunstepdetailsStatuses.forEach(i -> i.setStatus(null));
        }
        if (testRunStepDetails != null) {
            testRunStepDetails.forEach(i -> i.setStatus(this));
        }
        this.testrunstepdetailsStatuses = testRunStepDetails;
    }

    public TestStatus testrunstepdetailsStatuses(Set<TestRunStepDetails> testRunStepDetails) {
        this.setTestrunstepdetailsStatuses(testRunStepDetails);
        return this;
    }

    public TestStatus addTestrunstepdetailsStatus(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsStatuses.add(testRunStepDetails);
        testRunStepDetails.setStatus(this);
        return this;
    }

    public TestStatus removeTestrunstepdetailsStatus(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsStatuses.remove(testRunStepDetails);
        testRunStepDetails.setStatus(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestStatus)) {
            return false;
        }
        return id != null && id.equals(((TestStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestStatus{" +
            "id=" + getId() +
            ", statusName='" + getStatusName() + "'" +
            "}";
    }
}
