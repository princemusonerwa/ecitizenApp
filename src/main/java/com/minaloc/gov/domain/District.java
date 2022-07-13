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
 * A District.
 */
@Entity
@Table(name = "district")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class District implements Serializable {

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

    @OneToMany(mappedBy = "district")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cells", "district" }, allowSetters = true)
    private Set<Sector> sectors = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "districts" }, allowSetters = true)
    private Province province;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public District id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public District name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Sector> getSectors() {
        return this.sectors;
    }

    public void setSectors(Set<Sector> sectors) {
        if (this.sectors != null) {
            this.sectors.forEach(i -> i.setDistrict(null));
        }
        if (sectors != null) {
            sectors.forEach(i -> i.setDistrict(this));
        }
        this.sectors = sectors;
    }

    public District sectors(Set<Sector> sectors) {
        this.setSectors(sectors);
        return this;
    }

    public District addSector(Sector sector) {
        this.sectors.add(sector);
        sector.setDistrict(this);
        return this;
    }

    public District removeSector(Sector sector) {
        this.sectors.remove(sector);
        sector.setDistrict(null);
        return this;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District province(Province province) {
        this.setProvince(province);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof District)) {
            return false;
        }
        return id != null && id.equals(((District) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "District{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
