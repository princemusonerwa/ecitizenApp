package com.minaloc.gov.service.dto;

import com.minaloc.gov.domain.enumeration.Priority;

public class ComplainPriorityCountDTO {

    private Priority priority;
    private Long count;

    public ComplainPriorityCountDTO() {}

    public ComplainPriorityCountDTO(Priority priority, Long count) {
        this.priority = priority;
        this.count = count;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
