package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.Milestone} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.MilestoneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /milestones?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MilestoneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter reference;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private BooleanFilter isCompleted;

    private LongFilter parentMilestoneId;

    private LongFilter projectId;

    private LongFilter milestoneParentmilestoneId;

    private LongFilter testcaseMilestoneId;

    private LongFilter testrunMilestoneId;

    private Boolean distinct;

    public MilestoneCriteria() {}

    public MilestoneCriteria(MilestoneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.isCompleted = other.isCompleted == null ? null : other.isCompleted.copy();
        this.parentMilestoneId = other.parentMilestoneId == null ? null : other.parentMilestoneId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.milestoneParentmilestoneId = other.milestoneParentmilestoneId == null ? null : other.milestoneParentmilestoneId.copy();
        this.testcaseMilestoneId = other.testcaseMilestoneId == null ? null : other.testcaseMilestoneId.copy();
        this.testrunMilestoneId = other.testrunMilestoneId == null ? null : other.testrunMilestoneId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MilestoneCriteria copy() {
        return new MilestoneCriteria(this);
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

    public StringFilter getReference() {
        return reference;
    }

    public StringFilter reference() {
        if (reference == null) {
            reference = new StringFilter();
        }
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            startDate = new InstantFilter();
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            endDate = new InstantFilter();
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getIsCompleted() {
        return isCompleted;
    }

    public BooleanFilter isCompleted() {
        if (isCompleted == null) {
            isCompleted = new BooleanFilter();
        }
        return isCompleted;
    }

    public void setIsCompleted(BooleanFilter isCompleted) {
        this.isCompleted = isCompleted;
    }

    public LongFilter getParentMilestoneId() {
        return parentMilestoneId;
    }

    public LongFilter parentMilestoneId() {
        if (parentMilestoneId == null) {
            parentMilestoneId = new LongFilter();
        }
        return parentMilestoneId;
    }

    public void setParentMilestoneId(LongFilter parentMilestoneId) {
        this.parentMilestoneId = parentMilestoneId;
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

    public LongFilter getMilestoneParentmilestoneId() {
        return milestoneParentmilestoneId;
    }

    public LongFilter milestoneParentmilestoneId() {
        if (milestoneParentmilestoneId == null) {
            milestoneParentmilestoneId = new LongFilter();
        }
        return milestoneParentmilestoneId;
    }

    public void setMilestoneParentmilestoneId(LongFilter milestoneParentmilestoneId) {
        this.milestoneParentmilestoneId = milestoneParentmilestoneId;
    }

    public LongFilter getTestcaseMilestoneId() {
        return testcaseMilestoneId;
    }

    public LongFilter testcaseMilestoneId() {
        if (testcaseMilestoneId == null) {
            testcaseMilestoneId = new LongFilter();
        }
        return testcaseMilestoneId;
    }

    public void setTestcaseMilestoneId(LongFilter testcaseMilestoneId) {
        this.testcaseMilestoneId = testcaseMilestoneId;
    }

    public LongFilter getTestrunMilestoneId() {
        return testrunMilestoneId;
    }

    public LongFilter testrunMilestoneId() {
        if (testrunMilestoneId == null) {
            testrunMilestoneId = new LongFilter();
        }
        return testrunMilestoneId;
    }

    public void setTestrunMilestoneId(LongFilter testrunMilestoneId) {
        this.testrunMilestoneId = testrunMilestoneId;
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
        final MilestoneCriteria that = (MilestoneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(isCompleted, that.isCompleted) &&
            Objects.equals(parentMilestoneId, that.parentMilestoneId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(milestoneParentmilestoneId, that.milestoneParentmilestoneId) &&
            Objects.equals(testcaseMilestoneId, that.testcaseMilestoneId) &&
            Objects.equals(testrunMilestoneId, that.testrunMilestoneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            reference,
            startDate,
            endDate,
            isCompleted,
            parentMilestoneId,
            projectId,
            milestoneParentmilestoneId,
            testcaseMilestoneId,
            testrunMilestoneId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MilestoneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (isCompleted != null ? "isCompleted=" + isCompleted + ", " : "") +
            (parentMilestoneId != null ? "parentMilestoneId=" + parentMilestoneId + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (milestoneParentmilestoneId != null ? "milestoneParentmilestoneId=" + milestoneParentmilestoneId + ", " : "") +
            (testcaseMilestoneId != null ? "testcaseMilestoneId=" + testcaseMilestoneId + ", " : "") +
            (testrunMilestoneId != null ? "testrunMilestoneId=" + testrunMilestoneId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
