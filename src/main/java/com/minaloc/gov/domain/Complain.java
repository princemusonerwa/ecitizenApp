package com.minaloc.gov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minaloc.gov.domain.enumeration.Priority;
import com.minaloc.gov.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Complain.
 */
@Entity
@Table(name = "complain")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Complain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ikibazo", nullable = false)
    private String ikibazo;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "icyakozwe", nullable = false)
    private String icyakozwe;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "icyakorwa")
    private String icyakorwa;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "umwanzuro")
    private String umwanzuro;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @OneToOne
    @JoinColumn(unique = true)
    private Category category;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "village" }, allowSetters = true)
    private Umuturage umuturage;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
        name = "rel_complain__organization",
        joinColumns = @JoinColumn(name = "complain_id"),
        inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organization", "complains" }, allowSetters = true)
    private Set<Organization> organizations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Complain id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIkibazo() {
        return this.ikibazo;
    }

    public Complain ikibazo(String ikibazo) {
        this.setIkibazo(ikibazo);
        return this;
    }

    public void setIkibazo(String ikibazo) {
        this.ikibazo = ikibazo;
    }

    public String getIcyakozwe() {
        return this.icyakozwe;
    }

    public Complain icyakozwe(String icyakozwe) {
        this.setIcyakozwe(icyakozwe);
        return this;
    }

    public void setIcyakozwe(String icyakozwe) {
        this.icyakozwe = icyakozwe;
    }

    public String getIcyakorwa() {
        return this.icyakorwa;
    }

    public Complain icyakorwa(String icyakorwa) {
        this.setIcyakorwa(icyakorwa);
        return this;
    }

    public void setIcyakorwa(String icyakorwa) {
        this.icyakorwa = icyakorwa;
    }

    public String getUmwanzuro() {
        return this.umwanzuro;
    }

    public Complain umwanzuro(String umwanzuro) {
        this.setUmwanzuro(umwanzuro);
        return this;
    }

    public void setUmwanzuro(String umwanzuro) {
        this.umwanzuro = umwanzuro;
    }

    public Instant getDate() {
        return this.date;
    }

    public Complain date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Status getStatus() {
        return this.status;
    }

    public Complain status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Complain priority(Priority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Complain category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Umuturage getUmuturage() {
        return this.umuturage;
    }

    public void setUmuturage(Umuturage umuturage) {
        this.umuturage = umuturage;
    }

    public Complain umuturage(Umuturage umuturage) {
        this.setUmuturage(umuturage);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Complain user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Organization> getOrganizations() {
        return this.organizations;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    public Complain organizations(Set<Organization> organizations) {
        this.setOrganizations(organizations);
        return this;
    }

    public Complain addOrganization(Organization organization) {
        this.organizations.add(organization);
        organization.getComplains().add(this);
        return this;
    }

    public Complain removeOrganization(Organization organization) {
        this.organizations.remove(organization);
        organization.getComplains().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complain)) {
            return false;
        }
        return id != null && id.equals(((Complain) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Complain{" +
            "id=" + getId() +
            ", ikibazo='" + getIkibazo() + "'" +
            ", icyakozwe='" + getIcyakozwe() + "'" +
            ", icyakorwa='" + getIcyakorwa() + "'" +
            ", umwanzuro='" + getUmwanzuro() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority='" + getPriority() + "'" +
            "}";
    }
}
