package com.minaloc.gov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minaloc.gov.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Umuturage.
 */
@Entity
@Table(name = "umuturage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Umuturage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 16, max = 16)
    @Column(name = "indangamuntu", length = 16, nullable = false, unique = true)
    private String indangamuntu;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "amazina", length = 255, nullable = false)
    private String amazina;

    @NotNull
    @Column(name = "dob", nullable = false)
    private Instant dob;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ubudehe_category", length = 1, nullable = false)
    private String ubudeheCategory;

    @Size(min = 13, max = 13)
    @Column(name = "phone", length = 13)
    private String phone;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "umuturages", "cell" }, allowSetters = true)
    private Village village;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Umuturage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndangamuntu() {
        return this.indangamuntu;
    }

    public Umuturage indangamuntu(String indangamuntu) {
        this.setIndangamuntu(indangamuntu);
        return this;
    }

    public void setIndangamuntu(String indangamuntu) {
        this.indangamuntu = indangamuntu;
    }

    public String getAmazina() {
        return this.amazina;
    }

    public Umuturage amazina(String amazina) {
        this.setAmazina(amazina);
        return this;
    }

    public void setAmazina(String amazina) {
        this.amazina = amazina;
    }

    public Instant getDob() {
        return this.dob;
    }

    public Umuturage dob(Instant dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Umuturage gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getUbudeheCategory() {
        return this.ubudeheCategory;
    }

    public Umuturage ubudeheCategory(String ubudeheCategory) {
        this.setUbudeheCategory(ubudeheCategory);
        return this;
    }

    public void setUbudeheCategory(String ubudeheCategory) {
        this.ubudeheCategory = ubudeheCategory;
    }

    public String getPhone() {
        return this.phone;
    }

    public Umuturage phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Umuturage email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Umuturage user(User user) {
        this.setUser(user);
        return this;
    }

    public Village getVillage() {
        return this.village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public Umuturage village(Village village) {
        this.setVillage(village);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Umuturage)) {
            return false;
        }
        return id != null && id.equals(((Umuturage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Umuturage{" +
            "id=" + getId() +
            ", indangamuntu='" + getIndangamuntu() + "'" +
            ", amazina='" + getAmazina() + "'" +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            ", ubudeheCategory='" + getUbudeheCategory() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
