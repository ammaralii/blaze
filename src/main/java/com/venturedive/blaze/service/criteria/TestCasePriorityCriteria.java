package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.TestCasePriority} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TestCasePriorityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-case-priorities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCasePriorityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter testcasePriorityId;

    private Boolean distinct;

    public TestCasePriorityCriteria() {}

    public TestCasePriorityCriteria(TestCasePriorityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.testcasePriorityId = other.testcasePriorityId == null ? null : other.testcasePriorityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestCasePriorityCriteria copy() {
        return new TestCasePriorityCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getTestcasePriorityId() {
        return testcasePriorityId;
    }

    public LongFilter testcasePriorityId() {
        if (testcasePriorityId == null) {
            testcasePriorityId = new LongFilter();
        }
        return testcasePriorityId;
    }

    public void setTestcasePriorityId(LongFilter testcasePriorityId) {
        this.testcasePriorityId = testcasePriorityId;
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
        final TestCasePriorityCriteria that = (TestCasePriorityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(testcasePriorityId, that.testcasePriorityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, testcasePriorityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCasePriorityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (testcasePriorityId != null ? "testcasePriorityId=" + testcasePriorityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
