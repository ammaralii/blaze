package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.ApplicationUser} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.ApplicationUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /application-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter password;

    private InstantFilter lastActive;

    private StringFilter status;

    private IntegerFilter createdBy;

    private InstantFilter createdAt;

    private IntegerFilter updatedBy;

    private InstantFilter updatedAt;

    private StringFilter userEmail;

    private BooleanFilter isDeleted;

    private LongFilter companyId;

    private LongFilter projectId;

    private LongFilter roleId;

    private Boolean distinct;

    public ApplicationUserCriteria() {}

    public ApplicationUserCriteria(ApplicationUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.lastActive = other.lastActive == null ? null : other.lastActive.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.userEmail = other.userEmail == null ? null : other.userEmail.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.roleId = other.roleId == null ? null : other.roleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ApplicationUserCriteria copy() {
        return new ApplicationUserCriteria(this);
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

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getPassword() {
        return password;
    }

    public StringFilter password() {
        if (password == null) {
            password = new StringFilter();
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public InstantFilter getLastActive() {
        return lastActive;
    }

    public InstantFilter lastActive() {
        if (lastActive == null) {
            lastActive = new InstantFilter();
        }
        return lastActive;
    }

    public void setLastActive(InstantFilter lastActive) {
        this.lastActive = lastActive;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
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

    public StringFilter getUserEmail() {
        return userEmail;
    }

    public StringFilter userEmail() {
        if (userEmail == null) {
            userEmail = new StringFilter();
        }
        return userEmail;
    }

    public void setUserEmail(StringFilter userEmail) {
        this.userEmail = userEmail;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            isDeleted = new BooleanFilter();
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
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

    public LongFilter getRoleId() {
        return roleId;
    }

    public LongFilter roleId() {
        if (roleId == null) {
            roleId = new LongFilter();
        }
        return roleId;
    }

    public void setRoleId(LongFilter roleId) {
        this.roleId = roleId;
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
        final ApplicationUserCriteria that = (ApplicationUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(password, that.password) &&
            Objects.equals(lastActive, that.lastActive) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(userEmail, that.userEmail) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(roleId, that.roleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            password,
            lastActive,
            status,
            createdBy,
            createdAt,
            updatedBy,
            updatedAt,
            userEmail,
            isDeleted,
            companyId,
            projectId,
            roleId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (password != null ? "password=" + password + ", " : "") +
            (lastActive != null ? "lastActive=" + lastActive + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (userEmail != null ? "userEmail=" + userEmail + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (roleId != null ? "roleId=" + roleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
