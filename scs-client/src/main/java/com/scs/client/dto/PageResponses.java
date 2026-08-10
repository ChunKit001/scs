package com.scs.client.dto;

import com.alibaba.cola.dto.PageResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * PageResponse 组装约定：of(data, totalCount, pageSize, pageIndex)。
 */
public final class PageResponses {

    private PageResponses() {
    }

    public static <T> PageResponse<T> of(Collection<T> data, int totalCount, PageQuery query) {
        Collection<T> safeData = data == null ? Collections.emptyList() : data;
        return PageResponse.of(safeData, totalCount, query.safePageSize(), query.safePageIndex());
    }

    public static <T> PageResponse<T> empty(PageQuery query) {
        return of(List.of(), 0, query);
    }
}
