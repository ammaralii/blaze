package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.Company} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter country;

    private StringFilter companyAddress;

    private StringFilter organization;

    private IntegerFilter expectedNoOfUsers;

    private InstantFilter createdAt;

    private LongFilter applicationuserCompanyId;

    private LongFilter projectCompanyId;

    private LongFilter templateCompanyId;

    private LongFilter templatefieldCompanyId;

    private Boolean distinct;

    public CompanyCriteria() {}

    public CompanyCriteria(CompanyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.companyAddress = other.companyAddress == null ? null : other.companyAddress.copy();
        this.organization = other.organization == null ? null : other.organization.copy();
        this.expectedNoOfUsers = other.expectedNoOfUsers == null ? null : other.expectedNoOfUsers.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.applicationuserCompanyId = other.applicationuserCompanyId == null ? null : other.applicationuserCompanyId.copy();
        this.projectCompanyId = other.projectCompanyId == null ? null : other.projectCompanyId.copy();
        this.templateCompanyId = other.templateCompanyId == null ? null : other.templateCompanyId.copy();
        this.templatefieldCompanyId = other.templatefieldCompanyId == null ? null : other.templatefieldCompanyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CompanyCriteria copy() {
        return new CompanyCriteria(this);
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

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getCompanyAddress() {
        return companyAddress;
    }

    public StringFilter companyAddress() {
        if (companyAddress == null) {
            companyAddress = new StringFilter();
        }
        return companyAddress;
    }

    public void setCompanyAddress(StringFilter companyAddress) {
        this.companyAddress = companyAddress;
    }

    public StringFilter getOrganization() {
        return organization;
    }

    public StringFilter organization() {
        if (organization == null) {
            organization = new StringFilter();
        }
        return organization;
    }

    public void setOrganization(StringFilter organization) {
        this.organization = organization;
    }

    public IntegerFilter getExpectedNoOfUsers() {
        return expectedNoOfUsers;
    }

    public IntegerFilter expectedNoOfUsers() {
        if (expectedNoOfUsers == null) {
            expectedNoOfUsers = new IntegerFilter();
        }
        return expectedNoOfUsers;
    }

    public void setExpectedNoOfUsers(IntegerFilter expectedNoOfUsers) {
        this.expectedNoOfUsers = expectedNoOfUsers;
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

    public LongFilter getApplicationuserCompanyId() {
        return applicationuserCompanyId;
    }

    public LongFilter applicationuserCompanyId() {
        if (applicationuserCompanyId == null) {
            applicationuserCompanyId = new LongFilter();
        }
        return applicationuserCompanyId;
    }

    public void setApplicationuserCompanyId(LongFilter applicationuserCompanyId) {
        this.applicationuserCompanyId = applicationuserCompanyId;
    }

    public LongFilter getProjectCompanyId() {
        return projectCompanyId;
    }

    public LongFilter projectCompanyId() {
        if (projectCompanyId == null) {
            projectCompanyId = new LongFilter();
        }
        return projectCompanyId;
    }

    public void setProjectCompanyId(LongFilter projectCompanyId) {
        this.projectCompanyId = projectCompanyId;
    }

    public LongFilter getTemplateCompanyId() {
        return templateCompanyId;
    }

    public LongFilter templateCompanyId() {
        if (templateCompanyId == null) {
            templateCompanyId = new LongFilter();
        }
        return templateCompanyId;
    }

    public void setTemplateCompanyId(LongFilter templateCompanyId) {
        this.templateCompanyId = templateCompanyId;
    }

    public LongFilter getTemplatefieldCompanyId() {
        return templatefieldCompanyId;
    }

    public LongFilter templatefieldCompanyId() {
        if (templatefieldCompanyId == null) {
            templatefieldCompanyId = new LongFilter();
        }
        return templatefieldCompanyId;
    }

    public void setTemplatefieldCompanyId(LongFilter templatefieldCompanyId) {
        this.templatefieldCompanyId = templatefieldCompanyId;
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
        final CompanyCriteria that = (CompanyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(country, that.country) &&
            Objects.equals(companyAddress, that.companyAddress) &&
            Objects.equals(organization, that.organization) &&
            Objects.equals(expectedNoOfUsers, that.expectedNoOfUsers) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(applicationuserCompanyId, that.applicationuserCompanyId) &&
            Objects.equals(projectCompanyId, that.projectCompanyId) &&
            Objects.equals(templateCompanyId, that.templateCompanyId) &&
            Objects.equals(templatefieldCompanyId, that.templatefieldCompanyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            country,
            companyAddress,
            organization,
            expectedNoOfUsers,
            createdAt,
            applicationuserCompanyId,
            projectCompanyId,
            templateCompanyId,
            templatefieldCompanyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (companyAddress != null ? "companyAddress=" + companyAddress + ", " : "") +
            (organization != null ? "organization=" + organization + ", " : "") +
            (expectedNoOfUsers != null ? "expectedNoOfUsers=" + expectedNoOfUsers + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (applicationuserCompanyId != null ? "applicationuserCompanyId=" + applicationuserCompanyId + ", " : "") +
            (projectCompanyId != null ? "projectCompanyId=" + projectCompanyId + ", " : "") +
            (templateCompanyId != null ? "templateCompanyId=" + templateCompanyId + ", " : "") +
            (templatefieldCompanyId != null ? "templatefieldCompanyId=" + templatefieldCompanyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
