package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.TestRunDetails} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TestRunDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-run-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter resultDetail;

    private StringFilter jiraId;

    private IntegerFilter createdBy;

    private IntegerFilter executedBy;

    private LongFilter testRunId;

    private LongFilter testCaseId;

    private LongFilter statusId;

    private LongFilter testrundetailattachmentTestrundetailId;

    private LongFilter testrunstepdetailsTestrundetailId;

    private Boolean distinct;

    public TestRunDetailsCriteria() {}

    public TestRunDetailsCriteria(TestRunDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.resultDetail = other.resultDetail == null ? null : other.resultDetail.copy();
        this.jiraId = other.jiraId == null ? null : other.jiraId.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.executedBy = other.executedBy == null ? null : other.executedBy.copy();
        this.testRunId = other.testRunId == null ? null : other.testRunId.copy();
        this.testCaseId = other.testCaseId == null ? null : other.testCaseId.copy();
        this.statusId = other.statusId == null ? null : other.statusId.copy();
        this.testrundetailattachmentTestrundetailId =
            other.testrundetailattachmentTestrundetailId == null ? null : other.testrundetailattachmentTestrundetailId.copy();
        this.testrunstepdetailsTestrundetailId =
            other.testrunstepdetailsTestrundetailId == null ? null : other.testrunstepdetailsTestrundetailId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestRunDetailsCriteria copy() {
        return new TestRunDetailsCriteria(this);
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

    public StringFilter getResultDetail() {
        return resultDetail;
    }

    public StringFilter resultDetail() {
        if (resultDetail == null) {
            resultDetail = new StringFilter();
        }
        return resultDetail;
    }

    public void setResultDetail(StringFilter resultDetail) {
        this.resultDetail = resultDetail;
    }

    public StringFilter getJiraId() {
        return jiraId;
    }

    public StringFilter jiraId() {
        if (jiraId == null) {
            jiraId = new StringFilter();
        }
        return jiraId;
    }

    public void setJiraId(StringFilter jiraId) {
        this.jiraId = jiraId;
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

    public IntegerFilter getExecutedBy() {
        return executedBy;
    }

    public IntegerFilter executedBy() {
        if (executedBy == null) {
            executedBy = new IntegerFilter();
        }
        return executedBy;
    }

    public void setExecutedBy(IntegerFilter executedBy) {
        this.executedBy = executedBy;
    }

    public LongFilter getTestRunId() {
        return testRunId;
    }

    public LongFilter testRunId() {
        if (testRunId == null) {
            testRunId = new LongFilter();
        }
        return testRunId;
    }

    public void setTestRunId(LongFilter testRunId) {
        this.testRunId = testRunId;
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

    public LongFilter getStatusId() {
        return statusId;
    }

    public LongFilter statusId() {
        if (statusId == null) {
            statusId = new LongFilter();
        }
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public LongFilter getTestrundetailattachmentTestrundetailId() {
        return testrundetailattachmentTestrundetailId;
    }

    public LongFilter testrundetailattachmentTestrundetailId() {
        if (testrundetailattachmentTestrundetailId == null) {
            testrundetailattachmentTestrundetailId = new LongFilter();
        }
        return testrundetailattachmentTestrundetailId;
    }

    public void setTestrundetailattachmentTestrundetailId(LongFilter testrundetailattachmentTestrundetailId) {
        this.testrundetailattachmentTestrundetailId = testrundetailattachmentTestrundetailId;
    }

    public LongFilter getTestrunstepdetailsTestrundetailId() {
        return testrunstepdetailsTestrundetailId;
    }

    public LongFilter testrunstepdetailsTestrundetailId() {
        if (testrunstepdetailsTestrundetailId == null) {
            testrunstepdetailsTestrundetailId = new LongFilter();
        }
        return testrunstepdetailsTestrundetailId;
    }

    public void setTestrunstepdetailsTestrundetailId(LongFilter testrunstepdetailsTestrundetailId) {
        this.testrunstepdetailsTestrundetailId = testrunstepdetailsTestrundetailId;
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
        final TestRunDetailsCriteria that = (TestRunDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(resultDetail, that.resultDetail) &&
            Objects.equals(jiraId, that.jiraId) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(executedBy, that.executedBy) &&
            Objects.equals(testRunId, that.testRunId) &&
            Objects.equals(testCaseId, that.testCaseId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(testrundetailattachmentTestrundetailId, that.testrundetailattachmentTestrundetailId) &&
            Objects.equals(testrunstepdetailsTestrundetailId, that.testrunstepdetailsTestrundetailId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            resultDetail,
            jiraId,
            createdBy,
            executedBy,
            testRunId,
            testCaseId,
            statusId,
            testrundetailattachmentTestrundetailId,
            testrunstepdetailsTestrundetailId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (resultDetail != null ? "resultDetail=" + resultDetail + ", " : "") +
            (jiraId != null ? "jiraId=" + jiraId + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (executedBy != null ? "executedBy=" + executedBy + ", " : "") +
            (testRunId != null ? "testRunId=" + testRunId + ", " : "") +
            (testCaseId != null ? "testCaseId=" + testCaseId + ", " : "") +
            (statusId != null ? "statusId=" + statusId + ", " : "") +
            (testrundetailattachmentTestrundetailId != null ? "testrundetailattachmentTestrundetailId=" + testrundetailattachmentTestrundetailId + ", " : "") +
            (testrunstepdetailsTestrundetailId != null ? "testrunstepdetailsTestrundetailId=" + testrunstepdetailsTestrundetailId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
