package com.minaloc.gov.service.dto;

public class ComplainProvinceCountDTO {

    private String provinceName;
    private Long count;

    public ComplainProvinceCountDTO() {}

    public ComplainProvinceCountDTO(String provinceName, Long count) {
        this.provinceName = provinceName;
        this.count = count;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
