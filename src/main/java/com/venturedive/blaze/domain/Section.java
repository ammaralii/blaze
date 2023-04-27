package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Section.
 */
@Entity
@Table(name = "section")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "project", "sectionTestsuites", "testcaseTestsuites" }, allowSetters = true)
    private TestSuite testSuite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testSuite", "parentSection", "sectionParentsections", "testcaseSections" }, allowSetters = true)
    private Section parentSection;

    @OneToMany(mappedBy = "parentSection")
    @JsonIgnoreProperties(value = { "testSuite", "parentSection", "sectionParentsections", "testcaseSections" }, allowSetters = true)
    private Set<Section> sectionParentsections = new HashSet<>();

    @OneToMany(mappedBy = "section")
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
    private Set<TestCase> testcaseSections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Section id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Section name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Section description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Section createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public Section createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Section updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public Section updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public TestSuite getTestSuite() {
        return this.testSuite;
    }

    public void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public Section testSuite(TestSuite testSuite) {
        this.setTestSuite(testSuite);
        return this;
    }

    public Section getParentSection() {
        return this.parentSection;
    }

    public void setParentSection(Section section) {
        this.parentSection = section;
    }

    public Section parentSection(Section section) {
        this.setParentSection(section);
        return this;
    }

    public Set<Section> getSectionParentsections() {
        return this.sectionParentsections;
    }

    public void setSectionParentsections(Set<Section> sections) {
        if (this.sectionParentsections != null) {
            this.sectionParentsections.forEach(i -> i.setParentSection(null));
        }
        if (sections != null) {
            sections.forEach(i -> i.setParentSection(this));
        }
        this.sectionParentsections = sections;
    }

    public Section sectionParentsections(Set<Section> sections) {
        this.setSectionParentsections(sections);
        return this;
    }

    public Section addSectionParentsection(Section section) {
        this.sectionParentsections.add(section);
        section.setParentSection(this);
        return this;
    }

    public Section removeSectionParentsection(Section section) {
        this.sectionParentsections.remove(section);
        section.setParentSection(null);
        return this;
    }

    public Set<TestCase> getTestcaseSections() {
        return this.testcaseSections;
    }

    public void setTestcaseSections(Set<TestCase> testCases) {
        if (this.testcaseSections != null) {
            this.testcaseSections.forEach(i -> i.setSection(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setSection(this));
        }
        this.testcaseSections = testCases;
    }

    public Section testcaseSections(Set<TestCase> testCases) {
        this.setTestcaseSections(testCases);
        return this;
    }

    public Section addTestcaseSection(TestCase testCase) {
        this.testcaseSections.add(testCase);
        testCase.setSection(this);
        return this;
    }

    public Section removeTestcaseSection(TestCase testCase) {
        this.testcaseSections.remove(testCase);
        testCase.setSection(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        return id != null && id.equals(((Section) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Section{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            "}";
    }
}
