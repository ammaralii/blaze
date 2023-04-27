package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.Template} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TemplateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /templates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter templateName;

    private InstantFilter createdAt;

    private IntegerFilter createdBy;

    private LongFilter companyId;

    private LongFilter templateFieldId;

    private LongFilter projectDefaulttemplateId;

    private LongFilter testcaseTemplateId;

    private Boolean distinct;

    public TemplateCriteria() {}

    public TemplateCriteria(TemplateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.templateName = other.templateName == null ? null : other.templateName.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.templateFieldId = other.templateFieldId == null ? null : other.templateFieldId.copy();
        this.projectDefaulttemplateId = other.projectDefaulttemplateId == null ? null : other.projectDefaulttemplateId.copy();
        this.testcaseTemplateId = other.testcaseTemplateId == null ? null : other.testcaseTemplateId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TemplateCriteria copy() {
        return new TemplateCriteria(this);
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

    public StringFilter getTemplateName() {
        return templateName;
    }

    public StringFilter templateName() {
        if (templateName == null) {
            templateName = new StringFilter();
        }
        return templateName;
    }

    public void setTemplateName(StringFilter templateName) {
        this.templateName = templateName;
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

    public LongFilter getTemplateFieldId() {
        return templateFieldId;
    }

    public LongFilter templateFieldId() {
        if (templateFieldId == null) {
            templateFieldId = new LongFilter();
        }
        return templateFieldId;
    }

    public void setTemplateFieldId(LongFilter templateFieldId) {
        this.templateFieldId = templateFieldId;
    }

    public LongFilter getProjectDefaulttemplateId() {
        return projectDefaulttemplateId;
    }

    public LongFilter projectDefaulttemplateId() {
        if (projectDefaulttemplateId == null) {
            projectDefaulttemplateId = new LongFilter();
        }
        return projectDefaulttemplateId;
    }

    public void setProjectDefaulttemplateId(LongFilter projectDefaulttemplateId) {
        this.projectDefaulttemplateId = projectDefaulttemplateId;
    }

    public LongFilter getTestcaseTemplateId() {
        return testcaseTemplateId;
    }

    public LongFilter testcaseTemplateId() {
        if (testcaseTemplateId == null) {
            testcaseTemplateId = new LongFilter();
        }
        return testcaseTemplateId;
    }

    public void setTestcaseTemplateId(LongFilter testcaseTemplateId) {
        this.testcaseTemplateId = testcaseTemplateId;
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
        final TemplateCriteria that = (TemplateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(templateName, that.templateName) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(templateFieldId, that.templateFieldId) &&
            Objects.equals(projectDefaulttemplateId, that.projectDefaulttemplateId) &&
            Objects.equals(testcaseTemplateId, that.testcaseTemplateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            templateName,
            createdAt,
            createdBy,
            companyId,
            templateFieldId,
            projectDefaulttemplateId,
            testcaseTemplateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (templateName != null ? "templateName=" + templateName + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (templateFieldId != null ? "templateFieldId=" + templateFieldId + ", " : "") +
            (projectDefaulttemplateId != null ? "projectDefaulttemplateId=" + projectDefaulttemplateId + ", " : "") +
            (testcaseTemplateId != null ? "testcaseTemplateId=" + testcaseTemplateId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
