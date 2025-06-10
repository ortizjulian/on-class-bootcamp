package com.on_class.bootcamp.domain.model;

public class PaginationAndFilter {
    private Integer page;
    private Integer size;
    private String sortDirection;
    private String sortField;

    public PaginationAndFilter(Integer page, Integer size, String sortDirection, String sortField) {
        this.page = page;
        this.size = size;
        this.sortDirection = sortDirection;
        this.sortField = sortField;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
}
