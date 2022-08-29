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
 * Criteria class for the {@link com.minaloc.gov.domain.Cell} entity. This class is used
 * in {@link com.minaloc.gov.web.rest.CellResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cells?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CellCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sectorCode;

    private StringFilter name;

    private LongFilter villageId;

    private LongFilter sectorId;

    private Boolean distinct;

    public CellCriteria() {}

    public CellCriteria(CellCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sectorCode = other.sectorCode == null ? null : other.sectorCode.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.villageId = other.villageId == null ? null : other.villageId.copy();
        this.sectorId = other.sectorId == null ? null : other.sectorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CellCriteria copy() {
        return new CellCriteria(this);
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

    public StringFilter getSectorCode() {
        return sectorCode;
    }

    public StringFilter sectorCode() {
        if (sectorCode == null) {
            sectorCode = new StringFilter();
        }
        return sectorCode;
    }

    public void setSectorCode(StringFilter sectorCode) {
        this.sectorCode = sectorCode;
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

    public LongFilter getSectorId() {
        return sectorId;
    }

    public LongFilter sectorId() {
        if (sectorId == null) {
            sectorId = new LongFilter();
        }
        return sectorId;
    }

    public void setSectorId(LongFilter sectorId) {
        this.sectorId = sectorId;
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
        final CellCriteria that = (CellCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sectorCode, that.sectorCode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(villageId, that.villageId) &&
            Objects.equals(sectorId, that.sectorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sectorCode, name, villageId, sectorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CellCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sectorCode != null ? "sectorCode=" + sectorCode + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (villageId != null ? "villageId=" + villageId + ", " : "") +
            (sectorId != null ? "sectorId=" + sectorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
