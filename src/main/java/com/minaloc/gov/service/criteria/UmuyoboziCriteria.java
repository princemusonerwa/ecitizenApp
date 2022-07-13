package com.minaloc.gov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.minaloc.gov.domain.Umuyobozi} entity. This class is used
 * in {@link com.minaloc.gov.web.rest.UmuyoboziResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /umuyobozis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class UmuyoboziCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter phoneOne;

    private StringFilter phoneTwo;

    private StringFilter email;

    private LongFilter umurimoId;

    private Boolean distinct;

    public UmuyoboziCriteria() {}

    public UmuyoboziCriteria(UmuyoboziCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.phoneOne = other.phoneOne == null ? null : other.phoneOne.copy();
        this.phoneTwo = other.phoneTwo == null ? null : other.phoneTwo.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.umurimoId = other.umurimoId == null ? null : other.umurimoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UmuyoboziCriteria copy() {
        return new UmuyoboziCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getPhoneOne() {
        return phoneOne;
    }

    public StringFilter phoneOne() {
        if (phoneOne == null) {
            phoneOne = new StringFilter();
        }
        return phoneOne;
    }

    public void setPhoneOne(StringFilter phoneOne) {
        this.phoneOne = phoneOne;
    }

    public StringFilter getPhoneTwo() {
        return phoneTwo;
    }

    public StringFilter phoneTwo() {
        if (phoneTwo == null) {
            phoneTwo = new StringFilter();
        }
        return phoneTwo;
    }

    public void setPhoneTwo(StringFilter phoneTwo) {
        this.phoneTwo = phoneTwo;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getUmurimoId() {
        return umurimoId;
    }

    public LongFilter umurimoId() {
        if (umurimoId == null) {
            umurimoId = new LongFilter();
        }
        return umurimoId;
    }

    public void setUmurimoId(LongFilter umurimoId) {
        this.umurimoId = umurimoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UmuyoboziCriteria that = (UmuyoboziCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(phoneOne, that.phoneOne) &&
            Objects.equals(phoneTwo, that.phoneTwo) &&
            Objects.equals(email, that.email) &&
            Objects.equals(umurimoId, that.umurimoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phoneOne, phoneTwo, email, umurimoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UmuyoboziCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (phoneOne != null ? "phoneOne=" + phoneOne + ", " : "") +
            (phoneTwo != null ? "phoneTwo=" + phoneTwo + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (umurimoId != null ? "umurimoId=" + umurimoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
