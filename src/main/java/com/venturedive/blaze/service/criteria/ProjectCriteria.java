package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.Project} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter projectName;

    private StringFilter description;

    private BooleanFilter isactive;

    private IntegerFilter createdBy;

    private InstantFilter createdAt;

    private IntegerFilter updatedBy;

    private InstantFilter updatedAt;

    private LongFilter defaultTemplateId;

    private LongFilter companyId;

    private LongFilter milestoneProjectId;

    private LongFilter testplanProjectId;

    private LongFilter testsuiteProjectId;

    private LongFilter userId;

    private Boolean distinct;

    public ProjectCriteria() {}

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.projectName = other.projectName == null ? null : other.projectName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.isactive = other.isactive == null ? null : other.isactive.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.defaultTemplateId = other.defaultTemplateId == null ? null : other.defaultTemplateId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.milestoneProjectId = other.milestoneProjectId == null ? null : other.milestoneProjectId.copy();
        this.testplanProjectId = other.testplanProjectId == null ? null : other.testplanProjectId.copy();
        this.testsuiteProjectId = other.testsuiteProjectId == null ? null : other.testsuiteProjectId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
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

    public StringFilter getProjectName() {
        return projectName;
    }

    public StringFilter projectName() {
        if (projectName == null) {
            projectName = new StringFilter();
        }
        return projectName;
    }

    public void setProjectName(StringFilter projectName) {
        this.projectName = projectName;
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

    public BooleanFilter getIsactive() {
        return isactive;
    }

    public BooleanFilter isactive() {
        if (isactive == null) {
            isactive = new BooleanFilter();
        }
        return isactive;
    }

    public void setIsactive(BooleanFilter isactive) {
        this.isactive = isactive;
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

    public LongFilter getDefaultTemplateId() {
        return defaultTemplateId;
    }

    public LongFilter defaultTemplateId() {
        if (defaultTemplateId == null) {
            defaultTemplateId = new LongFilter();
        }
        return defaultTemplateId;
    }

    public void setDefaultTemplateId(LongFilter defaultTemplateId) {
        this.defaultTemplateId = defaultTemplateId;
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

    public LongFilter getMilestoneProjectId() {
        return milestoneProjectId;
    }

    public LongFilter milestoneProjectId() {
        if (milestoneProjectId == null) {
            milestoneProjectId = new LongFilter();
        }
        return milestoneProjectId;
    }

    public void setMilestoneProjectId(LongFilter milestoneProjectId) {
        this.milestoneProjectId = milestoneProjectId;
    }

    public LongFilter getTestplanProjectId() {
        return testplanProjectId;
    }

    public LongFilter testplanProjectId() {
        if (testplanProjectId == null) {
            testplanProjectId = new LongFilter();
        }
        return testplanProjectId;
    }

    public void setTestplanProjectId(LongFilter testplanProjectId) {
        this.testplanProjectId = testplanProjectId;
    }

    public LongFilter getTestsuiteProjectId() {
        return testsuiteProjectId;
    }

    public LongFilter testsuiteProjectId() {
        if (testsuiteProjectId == null) {
            testsuiteProjectId = new LongFilter();
        }
        return testsuiteProjectId;
    }

    public void setTestsuiteProjectId(LongFilter testsuiteProjectId) {
        this.testsuiteProjectId = testsuiteProjectId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final ProjectCriteria that = (ProjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(projectName, that.projectName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(isactive, that.isactive) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(defaultTemplateId, that.defaultTemplateId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(milestoneProjectId, that.milestoneProjectId) &&
            Objects.equals(testplanProjectId, that.testplanProjectId) &&
            Objects.equals(testsuiteProjectId, that.testsuiteProjectId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            projectName,
            description,
            isactive,
            createdBy,
            createdAt,
            updatedBy,
            updatedAt,
            defaultTemplateId,
            companyId,
            milestoneProjectId,
            testplanProjectId,
            testsuiteProjectId,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (projectName != null ? "projectName=" + projectName + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (isactive != null ? "isactive=" + isactive + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (defaultTemplateId != null ? "defaultTemplateId=" + defaultTemplateId + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (milestoneProjectId != null ? "milestoneProjectId=" + milestoneProjectId + ", " : "") +
            (testplanProjectId != null ? "testplanProjectId=" + testplanProjectId + ", " : "") +
            (testsuiteProjectId != null ? "testsuiteProjectId=" + testsuiteProjectId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
