package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestCase.
 */
@Entity
@Table(name = "test_case")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "title", length = 255)
    private String title;

    @Size(max = 255)
    @Column(name = "estimate", length = 255)
    private String estimate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 255)
    @Column(name = "precondition", length = 255)
    private String precondition;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "is_automated")
    private Boolean isAutomated;

    @ManyToOne
    @JsonIgnoreProperties(value = { "project", "sectionTestsuites", "testcaseTestsuites" }, allowSetters = true)
    private TestSuite testSuite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testSuite", "parentSection", "sectionParentsections", "testcaseSections" }, allowSetters = true)
    private Section section;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "testcasePriorities" }, allowSetters = true)
    private TestCasePriority priority;

    @ManyToOne
    @JsonIgnoreProperties(value = { "company", "templateFields", "projectDefaulttemplates", "testcaseTemplates" }, allowSetters = true)
    private Template template;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestoneParentmilestones", "testcaseMilestones", "testrunMilestones" },
        allowSetters = true
    )
    private Milestone milestone;

    @ManyToMany
    @JoinTable(
        name = "rel_test_case__test_level",
        joinColumns = @JoinColumn(name = "test_case_id"),
        inverseJoinColumns = @JoinColumn(name = "test_level_id")
    )
    @JsonIgnoreProperties(value = { "testrunTestlevels", "testCases" }, allowSetters = true)
    private Set<TestLevel> testLevels = new HashSet<>();

    @OneToMany(mappedBy = "testCase")
    @JsonIgnoreProperties(value = { "testCase" }, allowSetters = true)
    private Set<TestCaseAttachment> testcaseattachmentTestcases = new HashSet<>();

    @OneToMany(mappedBy = "testCase")
    @JsonIgnoreProperties(
        value = { "templateField", "testCase", "testcasefieldattachmentTestcasefields", "testrunstepdetailsStepdetails" },
        allowSetters = true
    )
    private Set<TestCaseField> testcasefieldTestcases = new HashSet<>();

    @OneToMany(mappedBy = "testCase")
    @JsonIgnoreProperties(
        value = { "testRun", "testCase", "status", "testrundetailattachmentTestrundetails", "testrunstepdetailsTestrundetails" },
        allowSetters = true
    )
    private Set<TestRunDetails> testrundetailsTestcases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public TestCase title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEstimate() {
        return this.estimate;
    }

    public TestCase estimate(String estimate) {
        this.setEstimate(estimate);
        return this;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public TestCase createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TestCase createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public TestCase updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TestCase updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPrecondition() {
        return this.precondition;
    }

    public TestCase precondition(String precondition) {
        this.setPrecondition(precondition);
        return this;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getDescription() {
        return this.description;
    }

    public TestCase description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsAutomated() {
        return this.isAutomated;
    }

    public TestCase isAutomated(Boolean isAutomated) {
        this.setIsAutomated(isAutomated);
        return this;
    }

    public void setIsAutomated(Boolean isAutomated) {
        this.isAutomated = isAutomated;
    }

    public TestSuite getTestSuite() {
        return this.testSuite;
    }

    public void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public TestCase testSuite(TestSuite testSuite) {
        this.setTestSuite(testSuite);
        return this;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public TestCase section(Section section) {
        this.setSection(section);
        return this;
    }

    public TestCasePriority getPriority() {
        return this.priority;
    }

    public void setPriority(TestCasePriority testCasePriority) {
        this.priority = testCasePriority;
    }

    public TestCase priority(TestCasePriority testCasePriority) {
        this.setPriority(testCasePriority);
        return this;
    }

    public Template getTemplate() {
        return this.template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public TestCase template(Template template) {
        this.setTemplate(template);
        return this;
    }

    public Milestone getMilestone() {
        return this.milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public TestCase milestone(Milestone milestone) {
        this.setMilestone(milestone);
        return this;
    }

    public Set<TestLevel> getTestLevels() {
        return this.testLevels;
    }

    public void setTestLevels(Set<TestLevel> testLevels) {
        this.testLevels = testLevels;
    }

    public TestCase testLevels(Set<TestLevel> testLevels) {
        this.setTestLevels(testLevels);
        return this;
    }

    public TestCase addTestLevel(TestLevel testLevel) {
        this.testLevels.add(testLevel);
        testLevel.getTestCases().add(this);
        return this;
    }

    public TestCase removeTestLevel(TestLevel testLevel) {
        this.testLevels.remove(testLevel);
        testLevel.getTestCases().remove(this);
        return this;
    }

    public Set<TestCaseAttachment> getTestcaseattachmentTestcases() {
        return this.testcaseattachmentTestcases;
    }

    public void setTestcaseattachmentTestcases(Set<TestCaseAttachment> testCaseAttachments) {
        if (this.testcaseattachmentTestcases != null) {
            this.testcaseattachmentTestcases.forEach(i -> i.setTestCase(null));
        }
        if (testCaseAttachments != null) {
            testCaseAttachments.forEach(i -> i.setTestCase(this));
        }
        this.testcaseattachmentTestcases = testCaseAttachments;
    }

    public TestCase testcaseattachmentTestcases(Set<TestCaseAttachment> testCaseAttachments) {
        this.setTestcaseattachmentTestcases(testCaseAttachments);
        return this;
    }

    public TestCase addTestcaseattachmentTestcase(TestCaseAttachment testCaseAttachment) {
        this.testcaseattachmentTestcases.add(testCaseAttachment);
        testCaseAttachment.setTestCase(this);
        return this;
    }

    public TestCase removeTestcaseattachmentTestcase(TestCaseAttachment testCaseAttachment) {
        this.testcaseattachmentTestcases.remove(testCaseAttachment);
        testCaseAttachment.setTestCase(null);
        return this;
    }

    public Set<TestCaseField> getTestcasefieldTestcases() {
        return this.testcasefieldTestcases;
    }

    public void setTestcasefieldTestcases(Set<TestCaseField> testCaseFields) {
        if (this.testcasefieldTestcases != null) {
            this.testcasefieldTestcases.forEach(i -> i.setTestCase(null));
        }
        if (testCaseFields != null) {
            testCaseFields.forEach(i -> i.setTestCase(this));
        }
        this.testcasefieldTestcases = testCaseFields;
    }

    public TestCase testcasefieldTestcases(Set<TestCaseField> testCaseFields) {
        this.setTestcasefieldTestcases(testCaseFields);
        return this;
    }

    public TestCase addTestcasefieldTestcase(TestCaseField testCaseField) {
        this.testcasefieldTestcases.add(testCaseField);
        testCaseField.setTestCase(this);
        return this;
    }

    public TestCase removeTestcasefieldTestcase(TestCaseField testCaseField) {
        this.testcasefieldTestcases.remove(testCaseField);
        testCaseField.setTestCase(null);
        return this;
    }

    public Set<TestRunDetails> getTestrundetailsTestcases() {
        return this.testrundetailsTestcases;
    }

    public void setTestrundetailsTestcases(Set<TestRunDetails> testRunDetails) {
        if (this.testrundetailsTestcases != null) {
            this.testrundetailsTestcases.forEach(i -> i.setTestCase(null));
        }
        if (testRunDetails != null) {
            testRunDetails.forEach(i -> i.setTestCase(this));
        }
        this.testrundetailsTestcases = testRunDetails;
    }

    public TestCase testrundetailsTestcases(Set<TestRunDetails> testRunDetails) {
        this.setTestrundetailsTestcases(testRunDetails);
        return this;
    }

    public TestCase addTestrundetailsTestcase(TestRunDetails testRunDetails) {
        this.testrundetailsTestcases.add(testRunDetails);
        testRunDetails.setTestCase(this);
        return this;
    }

    public TestCase removeTestrundetailsTestcase(TestRunDetails testRunDetails) {
        this.testrundetailsTestcases.remove(testRunDetails);
        testRunDetails.setTestCase(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCase)) {
            return false;
        }
        return id != null && id.equals(((TestCase) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCase{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", estimate='" + getEstimate() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", precondition='" + getPrecondition() + "'" +
            ", description='" + getDescription() + "'" +
            ", isAutomated='" + getIsAutomated() + "'" +
            "}";
    }
}
