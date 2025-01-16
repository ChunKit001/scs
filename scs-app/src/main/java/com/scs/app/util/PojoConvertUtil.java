package com.scs.app.util;

import com.scs.client.dto.MapStructReq;
import com.scs.client.dto.MapStructResp;
//import org.mapstruct.Mapper;

//@Mapper
public interface PojoConvertUtil {
    MapStructResp convert(MapStructReq mapStructResp);

}
