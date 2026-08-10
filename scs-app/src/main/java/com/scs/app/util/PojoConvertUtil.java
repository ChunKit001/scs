package com.scs.app.util;

import com.scs.client.dto.MapStructReq;
import com.scs.client.dto.MapStructResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct 演示：编译期生成实现类，componentModel=spring 以便注入。
 */
@Mapper(componentModel = "spring")
public interface PojoConvertUtil {

    @Mapping(source = "phone", target = "id")
    MapStructResp convert(MapStructReq req);
}
