package com.scs.client.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PojoConverterUtil {
    PojoConverterUtil convert = Mappers.getMapper(PojoConverterUtil.class);
}
