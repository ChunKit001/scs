package com.scs.app.demo;

import com.scs.app.util.PojoConvertUtil;
import com.scs.client.api.MapperStructServiceI;
import com.scs.client.dto.MapStructReq;
import com.scs.client.dto.MapStructResp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@AllArgsConstructor
public class MapperStructService implements MapperStructServiceI {

    @Autowired
    private PojoConvertUtil pojoConvertUtil;

    @Override
    public MapStructResp test(MapStructReq req) {
        return pojoConvertUtil.convert(req);
    }
}
