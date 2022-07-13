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
 * Criteria class for the {@link com.minaloc.gov.domain.Umurimo} entity. This class is used
 * in {@link com.minaloc.gov.web.rest.UmurimoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /umurimos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class UmurimoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter umurimo;

    private StringFilter urwego;

    private Boolean distinct;

    public UmurimoCriteria() {}

    public UmurimoCriteria(UmurimoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.umurimo = other.umurimo == null ? null : other.umurimo.copy();
        this.urwego = other.urwego == null ? null : other.urwego.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UmurimoCriteria copy() {
        return new UmurimoCriteria(this);
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

    public StringFilter getUmurimo() {
        return umurimo;
    }

    public StringFilter umurimo() {
        if (umurimo == null) {
            umurimo = new StringFilter();
        }
        return umurimo;
    }

    public void setUmurimo(StringFilter umurimo) {
        this.umurimo = umurimo;
    }

    public StringFilter getUrwego() {
        return urwego;
    }

    public StringFilter urwego() {
        if (urwego == null) {
            urwego = new StringFilter();
        }
        return urwego;
    }

    public void setUrwego(StringFilter urwego) {
        this.urwego = urwego;
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
        final UmurimoCriteria that = (UmurimoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(umurimo, that.umurimo) &&
            Objects.equals(urwego, that.urwego) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, umurimo, urwego, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UmurimoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (umurimo != null ? "umurimo=" + umurimo + ", " : "") +
            (urwego != null ? "urwego=" + urwego + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
