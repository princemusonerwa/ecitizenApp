package com.minaloc.gov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minaloc.gov.domain.enumeration.OfficeType;
import java.io.Serializable;
import java.time.LocalDate;
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

    @Column(name = "parent_id")
    private String parentId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "office_type", nullable = false)
    private OfficeType officeType;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @OneToOne
    @JoinColumn(unique = true)
    private User office;

    @OneToMany(mappedBy = "children")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "office", "parents", "children" }, allowSetters = true)
    private Set<Office> parents = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "office", "parents", "children" }, allowSetters = true)
    private Office children;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Office id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentId() {
        return this.parentId;
    }

    public Office parentId(String parentId) {
        this.setParentId(parentId);
        return this;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Office createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public User getOffice() {
        return this.office;
    }

    public void setOffice(User user) {
        this.office = user;
    }

    public Office office(User user) {
        this.setOffice(user);
        return this;
    }

    public Set<Office> getParents() {
        return this.parents;
    }

    public void setParents(Set<Office> offices) {
        if (this.parents != null) {
            this.parents.forEach(i -> i.setChildren(null));
        }
        if (offices != null) {
            offices.forEach(i -> i.setChildren(this));
        }
        this.parents = offices;
    }

    public Office parents(Set<Office> offices) {
        this.setParents(offices);
        return this;
    }

    public Office addParent(Office office) {
        this.parents.add(office);
        office.setChildren(this);
        return this;
    }

    public Office removeParent(Office office) {
        this.parents.remove(office);
        office.setChildren(null);
        return this;
    }

    public Office getChildren() {
        return this.children;
    }

    public void setChildren(Office office) {
        this.children = office;
    }

    public Office children(Office office) {
        this.setChildren(office);
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
            ", parentId='" + getParentId() + "'" +
            ", name='" + getName() + "'" +
            ", officeType='" + getOfficeType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
