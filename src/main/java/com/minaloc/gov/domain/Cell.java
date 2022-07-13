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
 * A Cell.
 */
@Entity
@Table(name = "cell")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cell implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @OneToMany(mappedBy = "cell")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "umuturages", "cell" }, allowSetters = true)
    private Set<Village> villages = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "cells", "district" }, allowSetters = true)
    private Sector sector;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cell id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cell name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Village> getVillages() {
        return this.villages;
    }

    public void setVillages(Set<Village> villages) {
        if (this.villages != null) {
            this.villages.forEach(i -> i.setCell(null));
        }
        if (villages != null) {
            villages.forEach(i -> i.setCell(this));
        }
        this.villages = villages;
    }

    public Cell villages(Set<Village> villages) {
        this.setVillages(villages);
        return this;
    }

    public Cell addVillage(Village village) {
        this.villages.add(village);
        village.setCell(this);
        return this;
    }

    public Cell removeVillage(Village village) {
        this.villages.remove(village);
        village.setCell(null);
        return this;
    }

    public Sector getSector() {
        return this.sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Cell sector(Sector sector) {
        this.setSector(sector);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cell)) {
            return false;
        }
        return id != null && id.equals(((Cell) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cell{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
