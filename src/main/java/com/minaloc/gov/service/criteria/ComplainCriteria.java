package com.minaloc.gov.service.criteria;

import com.minaloc.gov.domain.enumeration.Priority;
import com.minaloc.gov.domain.enumeration.Status;
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
 * Criteria class for the {@link com.minaloc.gov.domain.Complain} entity. This class is used
 * in {@link com.minaloc.gov.web.rest.ComplainResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /complains?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ComplainCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    /**
     * Class for filtering Priority
     */
    public static class PriorityFilter extends Filter<Priority> {

        public PriorityFilter() {}

        public PriorityFilter(PriorityFilter filter) {
            super(filter);
        }

        @Override
        public PriorityFilter copy() {
            return new PriorityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter date;

    private StatusFilter status;

    private PriorityFilter priority;

    private LongFilter categoryId;

    private LongFilter umuturageId;

    private LongFilter userId;

    private LongFilter organizationId;

    private Boolean distinct;

    public ComplainCriteria() {}

    public ComplainCriteria(ComplainCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.umuturageId = other.umuturageId == null ? null : other.umuturageId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ComplainCriteria copy() {
        return new ComplainCriteria(this);
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

    public InstantFilter getDate() {
        return date;
    }

    public InstantFilter date() {
        if (date == null) {
            date = new InstantFilter();
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public PriorityFilter getPriority() {
        return priority;
    }

    public PriorityFilter priority() {
        if (priority == null) {
            priority = new PriorityFilter();
        }
        return priority;
    }

    public void setPriority(PriorityFilter priority) {
        this.priority = priority;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public LongFilter organizationId() {
        if (organizationId == null) {
            organizationId = new LongFilter();
        }
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
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
        final ComplainCriteria that = (ComplainCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(umuturageId, that.umuturageId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(organizationId, that.organizationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, status, priority, categoryId, umuturageId, userId, organizationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComplainCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (priority != null ? "priority=" + priority + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (umuturageId != null ? "umuturageId=" + umuturageId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
