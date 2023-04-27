package com.venturedive.blaze.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blaze.domain.TemplateFieldType} entity. This class is used
 * in {@link com.venturedive.blaze.web.rest.TemplateFieldTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /template-field-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateFieldTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private BooleanFilter isList;

    private BooleanFilter attachments;

    private LongFilter templatefieldTemplatefieldtypeId;

    private Boolean distinct;

    public TemplateFieldTypeCriteria() {}

    public TemplateFieldTypeCriteria(TemplateFieldTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.isList = other.isList == null ? null : other.isList.copy();
        this.attachments = other.attachments == null ? null : other.attachments.copy();
        this.templatefieldTemplatefieldtypeId =
            other.templatefieldTemplatefieldtypeId == null ? null : other.templatefieldTemplatefieldtypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TemplateFieldTypeCriteria copy() {
        return new TemplateFieldTypeCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public BooleanFilter getIsList() {
        return isList;
    }

    public BooleanFilter isList() {
        if (isList == null) {
            isList = new BooleanFilter();
        }
        return isList;
    }

    public void setIsList(BooleanFilter isList) {
        this.isList = isList;
    }

    public BooleanFilter getAttachments() {
        return attachments;
    }

    public BooleanFilter attachments() {
        if (attachments == null) {
            attachments = new BooleanFilter();
        }
        return attachments;
    }

    public void setAttachments(BooleanFilter attachments) {
        this.attachments = attachments;
    }

    public LongFilter getTemplatefieldTemplatefieldtypeId() {
        return templatefieldTemplatefieldtypeId;
    }

    public LongFilter templatefieldTemplatefieldtypeId() {
        if (templatefieldTemplatefieldtypeId == null) {
            templatefieldTemplatefieldtypeId = new LongFilter();
        }
        return templatefieldTemplatefieldtypeId;
    }

    public void setTemplatefieldTemplatefieldtypeId(LongFilter templatefieldTemplatefieldtypeId) {
        this.templatefieldTemplatefieldtypeId = templatefieldTemplatefieldtypeId;
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
        final TemplateFieldTypeCriteria that = (TemplateFieldTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(isList, that.isList) &&
            Objects.equals(attachments, that.attachments) &&
            Objects.equals(templatefieldTemplatefieldtypeId, that.templatefieldTemplatefieldtypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, isList, attachments, templatefieldTemplatefieldtypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateFieldTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (isList != null ? "isList=" + isList + ", " : "") +
            (attachments != null ? "attachments=" + attachments + ", " : "") +
            (templatefieldTemplatefieldtypeId != null ? "templatefieldTemplatefieldtypeId=" + templatefieldTemplatefieldtypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
