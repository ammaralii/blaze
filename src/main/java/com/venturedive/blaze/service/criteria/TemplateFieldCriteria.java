package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.TemplateField} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TemplateFieldResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /template-fields?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateFieldCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fieldName;

    private LongFilter companyId;

    private LongFilter templateFieldTypeId;

    private LongFilter testcasefieldTemplatefieldId;

    private LongFilter templateId;

    private Boolean distinct;

    public TemplateFieldCriteria() {}

    public TemplateFieldCriteria(TemplateFieldCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fieldName = other.fieldName == null ? null : other.fieldName.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.templateFieldTypeId = other.templateFieldTypeId == null ? null : other.templateFieldTypeId.copy();
        this.testcasefieldTemplatefieldId = other.testcasefieldTemplatefieldId == null ? null : other.testcasefieldTemplatefieldId.copy();
        this.templateId = other.templateId == null ? null : other.templateId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TemplateFieldCriteria copy() {
        return new TemplateFieldCriteria(this);
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

    public StringFilter getFieldName() {
        return fieldName;
    }

    public StringFilter fieldName() {
        if (fieldName == null) {
            fieldName = new StringFilter();
        }
        return fieldName;
    }

    public void setFieldName(StringFilter fieldName) {
        this.fieldName = fieldName;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public LongFilter companyId() {
        if (companyId == null) {
            companyId = new LongFilter();
        }
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getTemplateFieldTypeId() {
        return templateFieldTypeId;
    }

    public LongFilter templateFieldTypeId() {
        if (templateFieldTypeId == null) {
            templateFieldTypeId = new LongFilter();
        }
        return templateFieldTypeId;
    }

    public void setTemplateFieldTypeId(LongFilter templateFieldTypeId) {
        this.templateFieldTypeId = templateFieldTypeId;
    }

    public LongFilter getTestcasefieldTemplatefieldId() {
        return testcasefieldTemplatefieldId;
    }

    public LongFilter testcasefieldTemplatefieldId() {
        if (testcasefieldTemplatefieldId == null) {
            testcasefieldTemplatefieldId = new LongFilter();
        }
        return testcasefieldTemplatefieldId;
    }

    public void setTestcasefieldTemplatefieldId(LongFilter testcasefieldTemplatefieldId) {
        this.testcasefieldTemplatefieldId = testcasefieldTemplatefieldId;
    }

    public LongFilter getTemplateId() {
        return templateId;
    }

    public LongFilter templateId() {
        if (templateId == null) {
            templateId = new LongFilter();
        }
        return templateId;
    }

    public void setTemplateId(LongFilter templateId) {
        this.templateId = templateId;
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
        final TemplateFieldCriteria that = (TemplateFieldCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fieldName, that.fieldName) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(templateFieldTypeId, that.templateFieldTypeId) &&
            Objects.equals(testcasefieldTemplatefieldId, that.testcasefieldTemplatefieldId) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fieldName, companyId, templateFieldTypeId, testcasefieldTemplatefieldId, templateId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateFieldCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fieldName != null ? "fieldName=" + fieldName + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (templateFieldTypeId != null ? "templateFieldTypeId=" + templateFieldTypeId + ", " : "") +
            (testcasefieldTemplatefieldId != null ? "testcasefieldTemplatefieldId=" + testcasefieldTemplatefieldId + ", " : "") +
            (templateId != null ? "templateId=" + templateId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
