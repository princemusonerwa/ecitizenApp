package com.minaloc.gov.service.dto;

import com.minaloc.gov.domain.enumeration.Status;
import java.io.Serializable;

public class ComplainStatusCountDTO implements Serializable {

    private Status status;
    private Long count;

    public ComplainStatusCountDTO() {}

    public ComplainStatusCountDTO(Status status, Long count) {
        this.status = status;
        this.count = count;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
