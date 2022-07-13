package com.minaloc.gov.service.criteria;

import com.minaloc.gov.domain.enumeration.Gender;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.minaloc.gov.domain.Umuturage} entity. This class is used
 * in {@link com.minaloc.gov.web.rest.UmuturageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /umuturages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class UmuturageCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter indangamuntu;

    private StringFilter amazina;

    private InstantFilter dob;

    private GenderFilter gender;

    private StringFilter ubudeheCategory;

    private StringFilter phone;

    private StringFilter email;

    private LongFilter userId;

    private LongFilter villageId;

    private Boolean distinct;

    public UmuturageCriteria() {}

    public UmuturageCriteria(UmuturageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.indangamuntu = other.indangamuntu == null ? null : other.indangamuntu.copy();
        this.amazina = other.amazina == null ? null : other.amazina.copy();
        this.dob = other.dob == null ? null : other.dob.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.ubudeheCategory = other.ubudeheCategory == null ? null : other.ubudeheCategory.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.villageId = other.villageId == null ? null : other.villageId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UmuturageCriteria copy() {
        return new UmuturageCriteria(this);
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

    public StringFilter getIndangamuntu() {
        return indangamuntu;
    }

    public StringFilter indangamuntu() {
        if (indangamuntu == null) {
            indangamuntu = new StringFilter();
        }
        return indangamuntu;
    }

    public void setIndangamuntu(StringFilter indangamuntu) {
        this.indangamuntu = indangamuntu;
    }

    public StringFilter getAmazina() {
        return amazina;
    }

    public StringFilter amazina() {
        if (amazina == null) {
            amazina = new StringFilter();
        }
        return amazina;
    }

    public void setAmazina(StringFilter amazina) {
        this.amazina = amazina;
    }

    public InstantFilter getDob() {
        return dob;
    }

    public InstantFilter dob() {
        if (dob == null) {
            dob = new InstantFilter();
        }
        return dob;
    }

    public void setDob(InstantFilter dob) {
        this.dob = dob;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public GenderFilter gender() {
        if (gender == null) {
            gender = new GenderFilter();
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getUbudeheCategory() {
        return ubudeheCategory;
    }

    public StringFilter ubudeheCategory() {
        if (ubudeheCategory == null) {
            ubudeheCategory = new StringFilter();
        }
        return ubudeheCategory;
    }

    public void setUbudeheCategory(StringFilter ubudeheCategory) {
        this.ubudeheCategory = ubudeheCategory;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getVillageId() {
        return villageId;
    }

    public LongFilter villageId() {
        if (villageId == null) {
            villageId = new LongFilter();
        }
        return villageId;
    }

    public void setVillageId(LongFilter villageId) {
        this.villageId = villageId;
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
        final UmuturageCriteria that = (UmuturageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(indangamuntu, that.indangamuntu) &&
            Objects.equals(amazina, that.amazina) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(ubudeheCategory, that.ubudeheCategory) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(villageId, that.villageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indangamuntu, amazina, dob, gender, ubudeheCategory, phone, email, userId, villageId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UmuturageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (indangamuntu != null ? "indangamuntu=" + indangamuntu + ", " : "") +
            (amazina != null ? "amazina=" + amazina + ", " : "") +
            (dob != null ? "dob=" + dob + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (ubudeheCategory != null ? "ubudeheCategory=" + ubudeheCategory + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (villageId != null ? "villageId=" + villageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
