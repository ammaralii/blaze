package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.TestStatus} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TestStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestStatusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter statusName;

    private LongFilter testrundetailsStatusId;

    private LongFilter testrunstepdetailsStatusId;

    private Boolean distinct;

    public TestStatusCriteria() {}

    public TestStatusCriteria(TestStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.statusName = other.statusName == null ? null : other.statusName.copy();
        this.testrundetailsStatusId = other.testrundetailsStatusId == null ? null : other.testrundetailsStatusId.copy();
        this.testrunstepdetailsStatusId = other.testrunstepdetailsStatusId == null ? null : other.testrunstepdetailsStatusId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestStatusCriteria copy() {
        return new TestStatusCriteria(this);
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

    public StringFilter getStatusName() {
        return statusName;
    }

    public StringFilter statusName() {
        if (statusName == null) {
            statusName = new StringFilter();
        }
        return statusName;
    }

    public void setStatusName(StringFilter statusName) {
        this.statusName = statusName;
    }

    public LongFilter getTestrundetailsStatusId() {
        return testrundetailsStatusId;
    }

    public LongFilter testrundetailsStatusId() {
        if (testrundetailsStatusId == null) {
            testrundetailsStatusId = new LongFilter();
        }
        return testrundetailsStatusId;
    }

    public void setTestrundetailsStatusId(LongFilter testrundetailsStatusId) {
        this.testrundetailsStatusId = testrundetailsStatusId;
    }

    public LongFilter getTestrunstepdetailsStatusId() {
        return testrunstepdetailsStatusId;
    }

    public LongFilter testrunstepdetailsStatusId() {
        if (testrunstepdetailsStatusId == null) {
            testrunstepdetailsStatusId = new LongFilter();
        }
        return testrunstepdetailsStatusId;
    }

    public void setTestrunstepdetailsStatusId(LongFilter testrunstepdetailsStatusId) {
        this.testrunstepdetailsStatusId = testrunstepdetailsStatusId;
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
        final TestStatusCriteria that = (TestStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(statusName, that.statusName) &&
            Objects.equals(testrundetailsStatusId, that.testrundetailsStatusId) &&
            Objects.equals(testrunstepdetailsStatusId, that.testrunstepdetailsStatusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statusName, testrundetailsStatusId, testrunstepdetailsStatusId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestStatusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (statusName != null ? "statusName=" + statusName + ", " : "") +
            (testrundetailsStatusId != null ? "testrundetailsStatusId=" + testrundetailsStatusId + ", " : "") +
            (testrunstepdetailsStatusId != null ? "testrunstepdetailsStatusId=" + testrunstepdetailsStatusId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
