package com.scs.infra.customer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 单表 CRUD 用 {@link BaseMapper}；复杂连表在接口声明方法，并在 {@code classpath*:mybatis/*.xml} 写 SQL。
 * 仅在 {@code scs.db.enabled=true} 时由 {@code DbConfiguration} 扫描注册。
 */
@Mapper
public interface CustomerMapper extends BaseMapper<CustomerDO> {
}
