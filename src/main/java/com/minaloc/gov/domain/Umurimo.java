package com.minaloc.gov.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Umurimo.
 */
@Entity
@Table(name = "umurimo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Umurimo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "umurimo", length = 100, nullable = false)
    private String umurimo;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "urwego", length = 100, nullable = false)
    private String urwego;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Umurimo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUmurimo() {
        return this.umurimo;
    }

    public Umurimo umurimo(String umurimo) {
        this.setUmurimo(umurimo);
        return this;
    }

    public void setUmurimo(String umurimo) {
        this.umurimo = umurimo;
    }

    public String getUrwego() {
        return this.urwego;
    }

    public Umurimo urwego(String urwego) {
        this.setUrwego(urwego);
        return this;
    }

    public void setUrwego(String urwego) {
        this.urwego = urwego;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Umurimo)) {
            return false;
        }
        return id != null && id.equals(((Umurimo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Umurimo{" +
            "id=" + getId() +
            ", umurimo='" + getUmurimo() + "'" +
            ", urwego='" + getUrwego() + "'" +
            "}";
    }
}
