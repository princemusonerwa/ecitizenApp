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
 * A Sector.
 */
@Entity
@Table(name = "sector")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "sector_code", length = 255, nullable = false, unique = true)
    private String sectorCode;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @OneToMany(mappedBy = "sector")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "villages", "sector" }, allowSetters = true)
    private Set<Cell> cells = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sectors", "province" }, allowSetters = true)
    private District district;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sector id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectorCode() {
        return this.sectorCode;
    }

    public Sector sectorCode(String sectorCode) {
        this.setSectorCode(sectorCode);
        return this;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getName() {
        return this.name;
    }

    public Sector name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Cell> getCells() {
        return this.cells;
    }

    public void setCells(Set<Cell> cells) {
        if (this.cells != null) {
            this.cells.forEach(i -> i.setSector(null));
        }
        if (cells != null) {
            cells.forEach(i -> i.setSector(this));
        }
        this.cells = cells;
    }

    public Sector cells(Set<Cell> cells) {
        this.setCells(cells);
        return this;
    }

    public Sector addCell(Cell cell) {
        this.cells.add(cell);
        cell.setSector(this);
        return this;
    }

    public Sector removeCell(Cell cell) {
        this.cells.remove(cell);
        cell.setSector(null);
        return this;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Sector district(District district) {
        this.setDistrict(district);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sector)) {
            return false;
        }
        return id != null && id.equals(((Sector) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sector{" +
            "id=" + getId() +
            ", sectorCode='" + getSectorCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
