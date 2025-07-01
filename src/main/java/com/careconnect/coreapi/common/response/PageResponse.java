package com.careconnect.coreapi.common.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;


@AllArgsConstructor
@Getter
@Setter
public class PageResponse<T> {
    private int pageNumber;
    private int pageSize;
    List<T> content;
    private int totalPages;
    private boolean lastPage;


    public PageResponse(Page<T> childrenPage) {
        this.pageNumber = childrenPage.getNumber();
        this.pageSize = childrenPage.getSize();
        this.content = childrenPage.getContent();
        this.totalPages = childrenPage.getTotalPages();
        this.lastPage = childrenPage.isLast();
    }
}
