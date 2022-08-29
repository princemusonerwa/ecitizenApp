package com.minaloc.gov.service.dto;

public class ComplainCategoryDTO {

    private String categoryName;
    private Long count;

    public ComplainCategoryDTO() {}

    public ComplainCategoryDTO(String categoryName, Long count) {
        this.categoryName = categoryName;
        this.count = count;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
