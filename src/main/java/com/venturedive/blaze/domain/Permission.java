package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Permission.
 */
@Entity
@Table(name = "permission")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "permission_name", length = 255, nullable = false, unique = true)
    private String permissionName;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnoreProperties(value = { "permissions", "users" }, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Permission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionName() {
        return this.permissionName;
    }

    public Permission permissionName(String permissionName) {
        this.setPermissionName(permissionName);
        return this;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        if (this.roles != null) {
            this.roles.forEach(i -> i.removePermission(this));
        }
        if (roles != null) {
            roles.forEach(i -> i.addPermission(this));
        }
        this.roles = roles;
    }

    public Permission roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public Permission addRole(Role role) {
        this.roles.add(role);
        role.getPermissions().add(this);
        return this;
    }

    public Permission removeRole(Role role) {
        this.roles.remove(role);
        role.getPermissions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Permission)) {
            return false;
        }
        return id != null && id.equals(((Permission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Permission{" +
            "id=" + getId() +
            ", permissionName='" + getPermissionName() + "'" +
            "}";
    }
}
