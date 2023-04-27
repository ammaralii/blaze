package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.TestLevel} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TestLevelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-levels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestLevelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter testrunTestlevelId;

    private LongFilter testCaseId;

    private Boolean distinct;

    public TestLevelCriteria() {}

    public TestLevelCriteria(TestLevelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.testrunTestlevelId = other.testrunTestlevelId == null ? null : other.testrunTestlevelId.copy();
        this.testCaseId = other.testCaseId == null ? null : other.testCaseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestLevelCriteria copy() {
        return new TestLevelCriteria(this);
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

    public LongFilter getTestrunTestlevelId() {
        return testrunTestlevelId;
    }

    public LongFilter testrunTestlevelId() {
        if (testrunTestlevelId == null) {
            testrunTestlevelId = new LongFilter();
        }
        return testrunTestlevelId;
    }

    public void setTestrunTestlevelId(LongFilter testrunTestlevelId) {
        this.testrunTestlevelId = testrunTestlevelId;
    }

    public LongFilter getTestCaseId() {
        return testCaseId;
    }

    public LongFilter testCaseId() {
        if (testCaseId == null) {
            testCaseId = new LongFilter();
        }
        return testCaseId;
    }

    public void setTestCaseId(LongFilter testCaseId) {
        this.testCaseId = testCaseId;
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
        final TestLevelCriteria that = (TestLevelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(testrunTestlevelId, that.testrunTestlevelId) &&
            Objects.equals(testCaseId, that.testCaseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, testrunTestlevelId, testCaseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestLevelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (testrunTestlevelId != null ? "testrunTestlevelId=" + testrunTestlevelId + ", " : "") +
            (testCaseId != null ? "testCaseId=" + testCaseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
