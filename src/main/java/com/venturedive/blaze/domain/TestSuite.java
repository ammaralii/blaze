package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestSuite.
 */
@Entity
@Table(name = "test_suite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestSuite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "test_suite_name", length = 255)
    private String testSuiteName;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestoneProjects", "testplanProjects", "testsuiteProjects", "users" },
        allowSetters = true
    )
    private Project project;

    @OneToMany(mappedBy = "testSuite")
    @JsonIgnoreProperties(value = { "testSuite", "parentSection", "sectionParentsections", "testcaseSections" }, allowSetters = true)
    private Set<Section> sectionTestsuites = new HashSet<>();

    @OneToMany(mappedBy = "testSuite")
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
    private Set<TestCase> testcaseTestsuites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestSuite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestSuiteName() {
        return this.testSuiteName;
    }

    public TestSuite testSuiteName(String testSuiteName) {
        this.setTestSuiteName(testSuiteName);
        return this;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public String getDescription() {
        return this.description;
    }

    public TestSuite description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public TestSuite createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TestSuite createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public TestSuite updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TestSuite updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TestSuite project(Project project) {
        this.setProject(project);
        return this;
    }

    public Set<Section> getSectionTestsuites() {
        return this.sectionTestsuites;
    }

    public void setSectionTestsuites(Set<Section> sections) {
        if (this.sectionTestsuites != null) {
            this.sectionTestsuites.forEach(i -> i.setTestSuite(null));
        }
        if (sections != null) {
            sections.forEach(i -> i.setTestSuite(this));
        }
        this.sectionTestsuites = sections;
    }

    public TestSuite sectionTestsuites(Set<Section> sections) {
        this.setSectionTestsuites(sections);
        return this;
    }

    public TestSuite addSectionTestsuite(Section section) {
        this.sectionTestsuites.add(section);
        section.setTestSuite(this);
        return this;
    }

    public TestSuite removeSectionTestsuite(Section section) {
        this.sectionTestsuites.remove(section);
        section.setTestSuite(null);
        return this;
    }

    public Set<TestCase> getTestcaseTestsuites() {
        return this.testcaseTestsuites;
    }

    public void setTestcaseTestsuites(Set<TestCase> testCases) {
        if (this.testcaseTestsuites != null) {
            this.testcaseTestsuites.forEach(i -> i.setTestSuite(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setTestSuite(this));
        }
        this.testcaseTestsuites = testCases;
    }

    public TestSuite testcaseTestsuites(Set<TestCase> testCases) {
        this.setTestcaseTestsuites(testCases);
        return this;
    }

    public TestSuite addTestcaseTestsuite(TestCase testCase) {
        this.testcaseTestsuites.add(testCase);
        testCase.setTestSuite(this);
        return this;
    }

    public TestSuite removeTestcaseTestsuite(TestCase testCase) {
        this.testcaseTestsuites.remove(testCase);
        testCase.setTestSuite(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestSuite)) {
            return false;
        }
        return id != null && id.equals(((TestSuite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestSuite{" +
            "id=" + getId() +
            ", testSuiteName='" + getTestSuiteName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
