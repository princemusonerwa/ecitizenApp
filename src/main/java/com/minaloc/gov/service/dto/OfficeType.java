package com.minaloc.gov.service.dto;

public class OfficeType {
    private String officeType;

    public OfficeType() {

    }
    public OfficeType(String officeType) {
        this.officeType = officeType;
    }

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }

    @Override
    public String toString() {
        return "OfficeType{" +
            "officeType='" + officeType + '\'' +
            '}';
    }
}
