package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.TestCaseFieldAttachment} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TestCaseFieldAttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-case-field-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseFieldAttachmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter url;

    private LongFilter testCaseFieldId;

    private Boolean distinct;

    public TestCaseFieldAttachmentCriteria() {}

    public TestCaseFieldAttachmentCriteria(TestCaseFieldAttachmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.testCaseFieldId = other.testCaseFieldId == null ? null : other.testCaseFieldId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestCaseFieldAttachmentCriteria copy() {
        return new TestCaseFieldAttachmentCriteria(this);
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

    public LongFilter getTestCaseFieldId() {
        return testCaseFieldId;
    }

    public LongFilter testCaseFieldId() {
        if (testCaseFieldId == null) {
            testCaseFieldId = new LongFilter();
        }
        return testCaseFieldId;
    }

    public void setTestCaseFieldId(LongFilter testCaseFieldId) {
        this.testCaseFieldId = testCaseFieldId;
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
        final TestCaseFieldAttachmentCriteria that = (TestCaseFieldAttachmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(url, that.url) &&
            Objects.equals(testCaseFieldId, that.testCaseFieldId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, testCaseFieldId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseFieldAttachmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (testCaseFieldId != null ? "testCaseFieldId=" + testCaseFieldId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
