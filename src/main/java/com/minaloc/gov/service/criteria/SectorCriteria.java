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
 * Criteria class for the {@link com.minaloc.gov.domain.Sector} entity. This class is used
 * in {@link com.minaloc.gov.web.rest.SectorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sectors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SectorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sectorCode;

    private StringFilter name;

    private LongFilter cellId;

    private LongFilter districtId;

    private Boolean distinct;

    public SectorCriteria() {}

    public SectorCriteria(SectorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sectorCode = other.sectorCode == null ? null : other.sectorCode.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.cellId = other.cellId == null ? null : other.cellId.copy();
        this.districtId = other.districtId == null ? null : other.districtId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SectorCriteria copy() {
        return new SectorCriteria(this);
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

    public LongFilter getDistrictId() {
        return districtId;
    }

    public LongFilter districtId() {
        if (districtId == null) {
            districtId = new LongFilter();
        }
        return districtId;
    }

    public void setDistrictId(LongFilter districtId) {
        this.districtId = districtId;
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
        final SectorCriteria that = (SectorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sectorCode, that.sectorCode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(cellId, that.cellId) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sectorCode, name, cellId, districtId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SectorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sectorCode != null ? "sectorCode=" + sectorCode + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (cellId != null ? "cellId=" + cellId + ", " : "") +
            (districtId != null ? "districtId=" + districtId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
