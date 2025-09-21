package com.start.demo.dto;

public record PageRequest(
    int page,
    int size,
    String sortBy,
    String sortDirection
) {
    public PageRequest {
        if (page < 0) page = 0;
        if (size < 1 || size > 100) size = 20;
        if (sortBy == null) sortBy = "id";
        if (sortDirection == null) sortDirection = "ASC";
    }
    
    public int getOffset() {
        return page * size;
    }
}