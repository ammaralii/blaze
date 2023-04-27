package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Milestone.
 */
@Entity
@Table(name = "milestone")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Milestone implements Serializable {

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

    @Size(max = 65535)
    @Column(name = "reference", length = 65535)
    private String reference;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestoneParentmilestones", "testcaseMilestones", "testrunMilestones" },
        allowSetters = true
    )
    private Milestone parentMilestone;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestoneProjects", "testplanProjects", "testsuiteProjects", "users" },
        allowSetters = true
    )
    private Project project;

    @OneToMany(mappedBy = "parentMilestone")
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestoneParentmilestones", "testcaseMilestones", "testrunMilestones" },
        allowSetters = true
    )
    private Set<Milestone> milestoneParentmilestones = new HashSet<>();

    @OneToMany(mappedBy = "milestone")
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
    private Set<TestCase> testcaseMilestones = new HashSet<>();

    @OneToMany(mappedBy = "mileStone")
    @JsonIgnoreProperties(value = { "testLevel", "mileStone", "testrundetailsTestruns" }, allowSetters = true)
    private Set<TestRun> testrunMilestones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Milestone id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Milestone name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Milestone description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return this.reference;
    }

    public Milestone reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Milestone startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Milestone endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsCompleted() {
        return this.isCompleted;
    }

    public Milestone isCompleted(Boolean isCompleted) {
        this.setIsCompleted(isCompleted);
        return this;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Milestone getParentMilestone() {
        return this.parentMilestone;
    }

    public void setParentMilestone(Milestone milestone) {
        this.parentMilestone = milestone;
    }

    public Milestone parentMilestone(Milestone milestone) {
        this.setParentMilestone(milestone);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Milestone project(Project project) {
        this.setProject(project);
        return this;
    }

    public Set<Milestone> getMilestoneParentmilestones() {
        return this.milestoneParentmilestones;
    }

    public void setMilestoneParentmilestones(Set<Milestone> milestones) {
        if (this.milestoneParentmilestones != null) {
            this.milestoneParentmilestones.forEach(i -> i.setParentMilestone(null));
        }
        if (milestones != null) {
            milestones.forEach(i -> i.setParentMilestone(this));
        }
        this.milestoneParentmilestones = milestones;
    }

    public Milestone milestoneParentmilestones(Set<Milestone> milestones) {
        this.setMilestoneParentmilestones(milestones);
        return this;
    }

    public Milestone addMilestoneParentmilestone(Milestone milestone) {
        this.milestoneParentmilestones.add(milestone);
        milestone.setParentMilestone(this);
        return this;
    }

    public Milestone removeMilestoneParentmilestone(Milestone milestone) {
        this.milestoneParentmilestones.remove(milestone);
        milestone.setParentMilestone(null);
        return this;
    }

    public Set<TestCase> getTestcaseMilestones() {
        return this.testcaseMilestones;
    }

    public void setTestcaseMilestones(Set<TestCase> testCases) {
        if (this.testcaseMilestones != null) {
            this.testcaseMilestones.forEach(i -> i.setMilestone(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setMilestone(this));
        }
        this.testcaseMilestones = testCases;
    }

    public Milestone testcaseMilestones(Set<TestCase> testCases) {
        this.setTestcaseMilestones(testCases);
        return this;
    }

    public Milestone addTestcaseMilestone(TestCase testCase) {
        this.testcaseMilestones.add(testCase);
        testCase.setMilestone(this);
        return this;
    }

    public Milestone removeTestcaseMilestone(TestCase testCase) {
        this.testcaseMilestones.remove(testCase);
        testCase.setMilestone(null);
        return this;
    }

    public Set<TestRun> getTestrunMilestones() {
        return this.testrunMilestones;
    }

    public void setTestrunMilestones(Set<TestRun> testRuns) {
        if (this.testrunMilestones != null) {
            this.testrunMilestones.forEach(i -> i.setMileStone(null));
        }
        if (testRuns != null) {
            testRuns.forEach(i -> i.setMileStone(this));
        }
        this.testrunMilestones = testRuns;
    }

    public Milestone testrunMilestones(Set<TestRun> testRuns) {
        this.setTestrunMilestones(testRuns);
        return this;
    }

    public Milestone addTestrunMilestone(TestRun testRun) {
        this.testrunMilestones.add(testRun);
        testRun.setMileStone(this);
        return this;
    }

    public Milestone removeTestrunMilestone(TestRun testRun) {
        this.testrunMilestones.remove(testRun);
        testRun.setMileStone(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Milestone)) {
            return false;
        }
        return id != null && id.equals(((Milestone) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Milestone{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", reference='" + getReference() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isCompleted='" + getIsCompleted() + "'" +
            "}";
    }
}
