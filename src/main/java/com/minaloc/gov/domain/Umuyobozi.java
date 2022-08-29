package com.minaloc.gov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Umuyobozi.
 */
@Entity
@Table(name = "umuyobozi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Umuyobozi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @NotNull
    @Size(max = 13)
    @Column(name = "phone_one", length = 13, nullable = false)
    private String phoneOne;

    @NotNull
    @Size(max = 13)
    @Column(name = "phone_two", length = 13, nullable = false)
    private String phoneTwo;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne
    @JoinColumn(unique = true)
    private Umurimo umurimo;

    @JsonIgnoreProperties(value = { "user", "children", "parent" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Office office;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Umuyobozi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Umuyobozi firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Umuyobozi lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneOne() {
        return this.phoneOne;
    }

    public Umuyobozi phoneOne(String phoneOne) {
        this.setPhoneOne(phoneOne);
        return this;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    public String getPhoneTwo() {
        return this.phoneTwo;
    }

    public Umuyobozi phoneTwo(String phoneTwo) {
        this.setPhoneTwo(phoneTwo);
        return this;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo;
    }

    public String getEmail() {
        return this.email;
    }

    public Umuyobozi email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Umurimo getUmurimo() {
        return this.umurimo;
    }

    public void setUmurimo(Umurimo umurimo) {
        this.umurimo = umurimo;
    }

    public Umuyobozi umurimo(Umurimo umurimo) {
        this.setUmurimo(umurimo);
        return this;
    }

    public Office getOffice() {
        return this.office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Umuyobozi office(Office office) {
        this.setOffice(office);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Umuyobozi)) {
            return false;
        }
        return id != null && id.equals(((Umuyobozi) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Umuyobozi{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneOne='" + getPhoneOne() + "'" +
            ", phoneTwo='" + getPhoneTwo() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
