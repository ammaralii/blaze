package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestRun.
 */
@Entity
@Table(name = "test_run")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRun implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testrunTestlevels", "testCases" }, allowSetters = true)
    private TestLevel testLevel;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestoneParentmilestones", "testcaseMilestones", "testrunMilestones" },
        allowSetters = true
    )
    private Milestone mileStone;

    @OneToMany(mappedBy = "testRun")
    @JsonIgnoreProperties(
        value = { "testRun", "testCase", "status", "testrundetailattachmentTestrundetails", "testrunstepdetailsTestrundetails" },
        allowSetters = true
    )
    private Set<TestRunDetails> testrundetailsTestruns = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestRun id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TestRun name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public TestRun description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TestRun createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public TestRun createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public TestLevel getTestLevel() {
        return this.testLevel;
    }

    public void setTestLevel(TestLevel testLevel) {
        this.testLevel = testLevel;
    }

    public TestRun testLevel(TestLevel testLevel) {
        this.setTestLevel(testLevel);
        return this;
    }

    public Milestone getMileStone() {
        return this.mileStone;
    }

    public void setMileStone(Milestone milestone) {
        this.mileStone = milestone;
    }

    public TestRun mileStone(Milestone milestone) {
        this.setMileStone(milestone);
        return this;
    }

    public Set<TestRunDetails> getTestrundetailsTestruns() {
        return this.testrundetailsTestruns;
    }

    public void setTestrundetailsTestruns(Set<TestRunDetails> testRunDetails) {
        if (this.testrundetailsTestruns != null) {
            this.testrundetailsTestruns.forEach(i -> i.setTestRun(null));
        }
        if (testRunDetails != null) {
            testRunDetails.forEach(i -> i.setTestRun(this));
        }
        this.testrundetailsTestruns = testRunDetails;
    }

    public TestRun testrundetailsTestruns(Set<TestRunDetails> testRunDetails) {
        this.setTestrundetailsTestruns(testRunDetails);
        return this;
    }

    public TestRun addTestrundetailsTestrun(TestRunDetails testRunDetails) {
        this.testrundetailsTestruns.add(testRunDetails);
        testRunDetails.setTestRun(this);
        return this;
    }

    public TestRun removeTestrundetailsTestrun(TestRunDetails testRunDetails) {
        this.testrundetailsTestruns.remove(testRunDetails);
        testRunDetails.setTestRun(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestRun)) {
            return false;
        }
        return id != null && id.equals(((TestRun) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRun{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
