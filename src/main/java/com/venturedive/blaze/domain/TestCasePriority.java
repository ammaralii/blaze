package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestCasePriority.
 */
@Entity
@Table(name = "test_case_priority")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCasePriority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "priority")
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
    private Set<TestCase> testcasePriorities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCasePriority id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TestCasePriority name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TestCase> getTestcasePriorities() {
        return this.testcasePriorities;
    }

    public void setTestcasePriorities(Set<TestCase> testCases) {
        if (this.testcasePriorities != null) {
            this.testcasePriorities.forEach(i -> i.setPriority(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setPriority(this));
        }
        this.testcasePriorities = testCases;
    }

    public TestCasePriority testcasePriorities(Set<TestCase> testCases) {
        this.setTestcasePriorities(testCases);
        return this;
    }

    public TestCasePriority addTestcasePriority(TestCase testCase) {
        this.testcasePriorities.add(testCase);
        testCase.setPriority(this);
        return this;
    }

    public TestCasePriority removeTestcasePriority(TestCase testCase) {
        this.testcasePriorities.remove(testCase);
        testCase.setPriority(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCasePriority)) {
            return false;
        }
        return id != null && id.equals(((TestCasePriority) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCasePriority{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
