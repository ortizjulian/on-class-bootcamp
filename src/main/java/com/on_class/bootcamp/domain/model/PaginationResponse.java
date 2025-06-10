package com.on_class.bootcamp.domain.model;

import java.util.List;

public class PaginationResponse<T> {
    private Integer totalPages;
    private Integer currentPage;
    private Long totalElements;
    private List<T> elements;

    public PaginationResponse(Integer totalPages, Integer currentPage, Long totalElements, List<T> elements) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.totalElements = totalElements;
        this.elements = elements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }
}
