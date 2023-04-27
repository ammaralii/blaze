package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestLevel.
 */
@Entity
@Table(name = "test_level")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @OneToMany(mappedBy = "testLevel")
    @JsonIgnoreProperties(value = { "testLevel", "mileStone", "testrundetailsTestruns" }, allowSetters = true)
    private Set<TestRun> testrunTestlevels = new HashSet<>();

    @ManyToMany(mappedBy = "testLevels")
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
    private Set<TestCase> testCases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestLevel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TestLevel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TestRun> getTestrunTestlevels() {
        return this.testrunTestlevels;
    }

    public void setTestrunTestlevels(Set<TestRun> testRuns) {
        if (this.testrunTestlevels != null) {
            this.testrunTestlevels.forEach(i -> i.setTestLevel(null));
        }
        if (testRuns != null) {
            testRuns.forEach(i -> i.setTestLevel(this));
        }
        this.testrunTestlevels = testRuns;
    }

    public TestLevel testrunTestlevels(Set<TestRun> testRuns) {
        this.setTestrunTestlevels(testRuns);
        return this;
    }

    public TestLevel addTestrunTestlevel(TestRun testRun) {
        this.testrunTestlevels.add(testRun);
        testRun.setTestLevel(this);
        return this;
    }

    public TestLevel removeTestrunTestlevel(TestRun testRun) {
        this.testrunTestlevels.remove(testRun);
        testRun.setTestLevel(null);
        return this;
    }

    public Set<TestCase> getTestCases() {
        return this.testCases;
    }

    public void setTestCases(Set<TestCase> testCases) {
        if (this.testCases != null) {
            this.testCases.forEach(i -> i.removeTestLevel(this));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.addTestLevel(this));
        }
        this.testCases = testCases;
    }

    public TestLevel testCases(Set<TestCase> testCases) {
        this.setTestCases(testCases);
        return this;
    }

    public TestLevel addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
        testCase.getTestLevels().add(this);
        return this;
    }

    public TestLevel removeTestCase(TestCase testCase) {
        this.testCases.remove(testCase);
        testCase.getTestLevels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestLevel)) {
            return false;
        }
        return id != null && id.equals(((TestLevel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestLevel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
