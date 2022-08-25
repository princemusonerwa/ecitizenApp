package com.minaloc.gov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minaloc.gov.domain.enumeration.OfficeType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Office.
 */
@Entity
@Table(name = "office")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Office implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "office_type", nullable = false)
    private OfficeType officeType;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "children", "parent" }, allowSetters = true)
    private Set<Office> children = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "children", "parent" }, allowSetters = true)
    private Office parent;

    @OneToMany(mappedBy = "office")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Office id(Long id) {
        this.setId(id);
        return this;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OfficeType getOfficeType() {
        return this.officeType;
    }

    public Office officeType(OfficeType officeType) {
        this.setOfficeType(officeType);
        return this;
    }

    public void setOfficeType(OfficeType officeType) {
        this.officeType = officeType;
    }

    public String getName() {
        return this.name;
    }

    public Office name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Office createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Office> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Office> offices) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (offices != null) {
            offices.forEach(i -> i.setParent(this));
        }
        this.children = offices;
    }

    public Office children(Set<Office> offices) {
        this.setChildren(offices);
        return this;
    }

    public Office addChildren(Office office) {
        this.children.add(office);
        office.setParent(this);
        return this;
    }

    public Office removeChildren(Office office) {
        this.children.remove(office);
        office.setParent(null);
        return this;
    }

    public Office getParent() {
        return this.parent;
    }

    public void setParent(Office office) {
        this.parent = office;
    }

    public Office parent(Office office) {
        this.setParent(office);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Office)) {
            return false;
        }
        return id != null && id.equals(((Office) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Office{" +
            "id=" + getId() +
            ", officeType='" + getOfficeType() + "'" +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
