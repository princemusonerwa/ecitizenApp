package com.minaloc.gov.service.dto;

import com.minaloc.gov.domain.enumeration.Priority;

public class ComplainPriorityCount {

    private Priority priority;
    private Long count;

    public ComplainPriorityCount() {}

    public ComplainPriorityCount(Priority priority, Long count) {
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
