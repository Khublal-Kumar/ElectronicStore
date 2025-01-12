package com.red.ElectronicStore.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponse<T>{

    private int pageNumber;      // Current page number
    private int pageSize;        // Number of items per page
    private long totalElements;  // Total number of elements
    private int totalPages;      // Total number of pages
    private boolean lastPage;    // Flag to indicate if this is the last page
    private List<T> content;     // List to hold the content for the current page


}
