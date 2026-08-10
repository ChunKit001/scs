package com.scs.app.demo;

import com.scs.app.util.PojoConvertUtil;
import com.scs.client.api.MapperStructServiceI;
import com.scs.client.dto.MapStructReq;
import com.scs.client.dto.MapStructResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapperStructService implements MapperStructServiceI {

    private final PojoConvertUtil pojoConvertUtil;

    @Override
    public MapStructResp test(MapStructReq req) {
        return pojoConvertUtil.convert(req);
    }
}
