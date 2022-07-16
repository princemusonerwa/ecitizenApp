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
 * A Village.
 */
@Entity
@Table(name = "village")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Village implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "village_code", length = 255, nullable = false, unique = true)
    private String villageCode;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @OneToMany(mappedBy = "village")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "village" }, allowSetters = true)
    private Set<Umuturage> umuturages = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "villages", "sector" }, allowSetters = true)
    private Cell cell;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Village id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVillageCode() {
        return this.villageCode;
    }

    public Village villageCode(String villageCode) {
        this.setVillageCode(villageCode);
        return this;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getName() {
        return this.name;
    }

    public Village name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Umuturage> getUmuturages() {
        return this.umuturages;
    }

    public void setUmuturages(Set<Umuturage> umuturages) {
        if (this.umuturages != null) {
            this.umuturages.forEach(i -> i.setVillage(null));
        }
        if (umuturages != null) {
            umuturages.forEach(i -> i.setVillage(this));
        }
        this.umuturages = umuturages;
    }

    public Village umuturages(Set<Umuturage> umuturages) {
        this.setUmuturages(umuturages);
        return this;
    }

    public Village addUmuturage(Umuturage umuturage) {
        this.umuturages.add(umuturage);
        umuturage.setVillage(this);
        return this;
    }

    public Village removeUmuturage(Umuturage umuturage) {
        this.umuturages.remove(umuturage);
        umuturage.setVillage(null);
        return this;
    }

    public Cell getCell() {
        return this.cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Village cell(Cell cell) {
        this.setCell(cell);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Village)) {
            return false;
        }
        return id != null && id.equals(((Village) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Village{" +
            "id=" + getId() +
            ", villageCode='" + getVillageCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
