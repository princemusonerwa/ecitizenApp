package com.minaloc.gov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "organizations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "umuturage", "user", "organizations" }, allowSetters = true)
    private Set<Complain> complains = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Organization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Organization name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public Organization location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Complain> getComplains() {
        return this.complains;
    }

    public void setComplains(Set<Complain> complains) {
        if (this.complains != null) {
            this.complains.forEach(i -> i.removeOrganization(this));
        }
        if (complains != null) {
            complains.forEach(i -> i.addOrganization(this));
        }
        this.complains = complains;
    }

    public Organization complains(Set<Complain> complains) {
        this.setComplains(complains);
        return this;
    }

    public Organization addComplain(Complain complain) {
        this.complains.add(complain);
        complain.getOrganizations().add(this);
        return this;
    }

    public Organization removeComplain(Complain complain) {
        this.complains.remove(complain);
        complain.getOrganizations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
