package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "role_name", length = 255, nullable = false, unique = true)
    private String roleName;

    @Column(name = "isdefault")
    private Boolean isdefault;

    @ManyToMany
    @JoinTable(
        name = "rel_role__permission",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    @JsonIgnoreProperties(value = { "company", "projects", "roles" }, allowSetters = true)
    private Set<ApplicationUser> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public Role roleName(String roleName) {
        this.setRoleName(roleName);
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getIsdefault() {
        return this.isdefault;
    }

    public Role isdefault(Boolean isdefault) {
        this.setIsdefault(isdefault);
        return this;
    }

    public void setIsdefault(Boolean isdefault) {
        this.isdefault = isdefault;
    }

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Role permissions(Set<Permission> permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public Role addPermission(Permission permission) {
        this.permissions.add(permission);
        permission.getRoles().add(this);
        return this;
    }

    public Role removePermission(Permission permission) {
        this.permissions.remove(permission);
        permission.getRoles().remove(this);
        return this;
    }

    public Set<ApplicationUser> getUsers() {
        return this.users;
    }

    public void setUsers(Set<ApplicationUser> applicationUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeRole(this));
        }
        if (applicationUsers != null) {
            applicationUsers.forEach(i -> i.addRole(this));
        }
        this.users = applicationUsers;
    }

    public Role users(Set<ApplicationUser> applicationUsers) {
        this.setUsers(applicationUsers);
        return this;
    }

    public Role addUser(ApplicationUser applicationUser) {
        this.users.add(applicationUser);
        applicationUser.getRoles().add(this);
        return this;
    }

    public Role removeUser(ApplicationUser applicationUser) {
        this.users.remove(applicationUser);
        applicationUser.getRoles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", roleName='" + getRoleName() + "'" +
            ", isdefault='" + getIsdefault() + "'" +
            "}";
    }
}
