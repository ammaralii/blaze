package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestPlan.
 */
@Entity
@Table(name = "test_plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestPlan implements Serializable {

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

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestoneProjects", "testplanProjects", "testsuiteProjects", "users" },
        allowSetters = true
    )
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TestPlan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public TestPlan description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public TestPlan createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TestPlan createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TestPlan project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestPlan)) {
            return false;
        }
        return id != null && id.equals(((TestPlan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestPlan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
