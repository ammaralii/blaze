package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TemplateFieldType.
 */
@Entity
@Table(name = "template_field_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateFieldType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "type", length = 255, nullable = false, unique = true)
    private String type;

    @NotNull
    @Column(name = "is_list", nullable = false)
    private Boolean isList;

    @NotNull
    @Column(name = "attachments", nullable = false)
    private Boolean attachments;

    @OneToMany(mappedBy = "templateFieldType")
    @JsonIgnoreProperties(value = { "company", "templateFieldType", "testcasefieldTemplatefields", "templates" }, allowSetters = true)
    private Set<TemplateField> templatefieldTemplatefieldtypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TemplateFieldType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public TemplateFieldType type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsList() {
        return this.isList;
    }

    public TemplateFieldType isList(Boolean isList) {
        this.setIsList(isList);
        return this;
    }

    public void setIsList(Boolean isList) {
        this.isList = isList;
    }

    public Boolean getAttachments() {
        return this.attachments;
    }

    public TemplateFieldType attachments(Boolean attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public void setAttachments(Boolean attachments) {
        this.attachments = attachments;
    }

    public Set<TemplateField> getTemplatefieldTemplatefieldtypes() {
        return this.templatefieldTemplatefieldtypes;
    }

    public void setTemplatefieldTemplatefieldtypes(Set<TemplateField> templateFields) {
        if (this.templatefieldTemplatefieldtypes != null) {
            this.templatefieldTemplatefieldtypes.forEach(i -> i.setTemplateFieldType(null));
        }
        if (templateFields != null) {
            templateFields.forEach(i -> i.setTemplateFieldType(this));
        }
        this.templatefieldTemplatefieldtypes = templateFields;
    }

    public TemplateFieldType templatefieldTemplatefieldtypes(Set<TemplateField> templateFields) {
        this.setTemplatefieldTemplatefieldtypes(templateFields);
        return this;
    }

    public TemplateFieldType addTemplatefieldTemplatefieldtype(TemplateField templateField) {
        this.templatefieldTemplatefieldtypes.add(templateField);
        templateField.setTemplateFieldType(this);
        return this;
    }

    public TemplateFieldType removeTemplatefieldTemplatefieldtype(TemplateField templateField) {
        this.templatefieldTemplatefieldtypes.remove(templateField);
        templateField.setTemplateFieldType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateFieldType)) {
            return false;
        }
        return id != null && id.equals(((TemplateFieldType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateFieldType{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", isList='" + getIsList() + "'" +
            ", attachments='" + getAttachments() + "'" +
            "}";
    }
}
