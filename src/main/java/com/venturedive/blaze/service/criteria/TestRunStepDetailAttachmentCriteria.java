package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.TestRunStepDetailAttachment} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TestRunStepDetailAttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-run-step-detail-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunStepDetailAttachmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter url;

    private LongFilter testRunStepDetailId;

    private Boolean distinct;

    public TestRunStepDetailAttachmentCriteria() {}

    public TestRunStepDetailAttachmentCriteria(TestRunStepDetailAttachmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.testRunStepDetailId = other.testRunStepDetailId == null ? null : other.testRunStepDetailId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestRunStepDetailAttachmentCriteria copy() {
        return new TestRunStepDetailAttachmentCriteria(this);
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

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public LongFilter getTestRunStepDetailId() {
        return testRunStepDetailId;
    }

    public LongFilter testRunStepDetailId() {
        if (testRunStepDetailId == null) {
            testRunStepDetailId = new LongFilter();
        }
        return testRunStepDetailId;
    }

    public void setTestRunStepDetailId(LongFilter testRunStepDetailId) {
        this.testRunStepDetailId = testRunStepDetailId;
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
        final TestRunStepDetailAttachmentCriteria that = (TestRunStepDetailAttachmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(url, that.url) &&
            Objects.equals(testRunStepDetailId, that.testRunStepDetailId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, testRunStepDetailId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunStepDetailAttachmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (testRunStepDetailId != null ? "testRunStepDetailId=" + testRunStepDetailId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
