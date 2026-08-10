package com.scs.client.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PageQueryTest {

    @Test
    void defaultsAndOffset() {
        PageQuery query = new PageQuery();
        assertThat(query.safePageIndex()).isEqualTo(1);
        assertThat(query.safePageSize()).isEqualTo(10);
        assertThat(query.offset()).isEqualTo(0);
    }

    @Test
    void capsPageSize() {
        PageQuery query = new PageQuery();
        query.setPageIndex(3);
        query.setPageSize(1000);
        assertThat(query.safePageSize()).isEqualTo(PageQuery.MAX_PAGE_SIZE);
        assertThat(query.offset()).isEqualTo(2 * PageQuery.MAX_PAGE_SIZE);
    }
}
