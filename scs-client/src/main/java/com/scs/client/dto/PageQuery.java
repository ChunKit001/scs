package com.scs.client.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页约定：pageIndex 从 1 开始；pageSize 默认 10，最大 100。
 */
@Data
public class PageQuery {

    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    @Min(1)
    private Integer pageIndex = DEFAULT_PAGE_INDEX;

    @Min(1)
    @Max(MAX_PAGE_SIZE)
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    public int safePageIndex() {
        return pageIndex == null || pageIndex < 1 ? DEFAULT_PAGE_INDEX : pageIndex;
    }

    public int safePageSize() {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    /** SQL offset，从 0 开始 */
    public int offset() {
        return (safePageIndex() - 1) * safePageSize();
    }
}
