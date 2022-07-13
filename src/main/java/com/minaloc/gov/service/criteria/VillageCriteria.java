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
 * Criteria class for the {@link com.minaloc.gov.domain.Village} entity. This class is used
 * in {@link com.minaloc.gov.web.rest.VillageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /villages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class VillageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter umuturageId;

    private LongFilter cellId;

    private Boolean distinct;

    public VillageCriteria() {}

    public VillageCriteria(VillageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.umuturageId = other.umuturageId == null ? null : other.umuturageId.copy();
        this.cellId = other.cellId == null ? null : other.cellId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VillageCriteria copy() {
        return new VillageCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getUmuturageId() {
        return umuturageId;
    }

    public LongFilter umuturageId() {
        if (umuturageId == null) {
            umuturageId = new LongFilter();
        }
        return umuturageId;
    }

    public void setUmuturageId(LongFilter umuturageId) {
        this.umuturageId = umuturageId;
    }

    public LongFilter getCellId() {
        return cellId;
    }

    public LongFilter cellId() {
        if (cellId == null) {
            cellId = new LongFilter();
        }
        return cellId;
    }

    public void setCellId(LongFilter cellId) {
        this.cellId = cellId;
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
        final VillageCriteria that = (VillageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(umuturageId, that.umuturageId) &&
            Objects.equals(cellId, that.cellId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, umuturageId, cellId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VillageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (umuturageId != null ? "umuturageId=" + umuturageId + ", " : "") +
            (cellId != null ? "cellId=" + cellId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
