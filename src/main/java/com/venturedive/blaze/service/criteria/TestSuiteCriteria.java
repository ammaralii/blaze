package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.TestSuite} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TestSuiteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-suites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestSuiteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter testSuiteName;

    private StringFilter description;

    private IntegerFilter createdBy;

    private InstantFilter createdAt;

    private IntegerFilter updatedBy;

    private InstantFilter updatedAt;

    private LongFilter projectId;

    private LongFilter sectionTestsuiteId;

    private LongFilter testcaseTestsuiteId;

    private Boolean distinct;

    public TestSuiteCriteria() {}

    public TestSuiteCriteria(TestSuiteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.testSuiteName = other.testSuiteName == null ? null : other.testSuiteName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.sectionTestsuiteId = other.sectionTestsuiteId == null ? null : other.sectionTestsuiteId.copy();
        this.testcaseTestsuiteId = other.testcaseTestsuiteId == null ? null : other.testcaseTestsuiteId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestSuiteCriteria copy() {
        return new TestSuiteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTestSuiteName() {
        return testSuiteName;
    }

    public StringFilter testSuiteName() {
        if (testSuiteName == null) {
            testSuiteName = new StringFilter();
        }
        return testSuiteName;
    }

    public void setTestSuiteName(StringFilter testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getCreatedBy() {
        return createdBy;
    }

    public IntegerFilter createdBy() {
        if (createdBy == null) {
            createdBy = new IntegerFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(IntegerFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public IntegerFilter getUpdatedBy() {
        return updatedBy;
    }

    public IntegerFilter updatedBy() {
        if (updatedBy == null) {
            updatedBy = new IntegerFilter();
        }
        return updatedBy;
    }

    public void setUpdatedBy(IntegerFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new InstantFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public LongFilter projectId() {
        if (projectId == null) {
            projectId = new LongFilter();
        }
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getSectionTestsuiteId() {
        return sectionTestsuiteId;
    }

    public LongFilter sectionTestsuiteId() {
        if (sectionTestsuiteId == null) {
            sectionTestsuiteId = new LongFilter();
        }
        return sectionTestsuiteId;
    }

    public void setSectionTestsuiteId(LongFilter sectionTestsuiteId) {
        this.sectionTestsuiteId = sectionTestsuiteId;
    }

    public LongFilter getTestcaseTestsuiteId() {
        return testcaseTestsuiteId;
    }

    public LongFilter testcaseTestsuiteId() {
        if (testcaseTestsuiteId == null) {
            testcaseTestsuiteId = new LongFilter();
        }
        return testcaseTestsuiteId;
    }

    public void setTestcaseTestsuiteId(LongFilter testcaseTestsuiteId) {
        this.testcaseTestsuiteId = testcaseTestsuiteId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TestSuiteCriteria that = (TestSuiteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(testSuiteName, that.testSuiteName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(sectionTestsuiteId, that.sectionTestsuiteId) &&
            Objects.equals(testcaseTestsuiteId, that.testcaseTestsuiteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            testSuiteName,
            description,
            createdBy,
            createdAt,
            updatedBy,
            updatedAt,
            projectId,
            sectionTestsuiteId,
            testcaseTestsuiteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestSuiteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (testSuiteName != null ? "testSuiteName=" + testSuiteName + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (sectionTestsuiteId != null ? "sectionTestsuiteId=" + sectionTestsuiteId + ", " : "") +
            (testcaseTestsuiteId != null ? "testcaseTestsuiteId=" + testcaseTestsuiteId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
