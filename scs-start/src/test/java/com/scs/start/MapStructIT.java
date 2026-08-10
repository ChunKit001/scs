package com.scs.start;

import com.scs.client.api.MapperStructServiceI;
import com.scs.client.dto.MapStructReq;
import com.scs.client.dto.MapStructResp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MapStructIT extends AbstractMysqlIT {

    @Autowired
    private MapperStructServiceI mapperStructService;

    @Test
    void convertsPhoneToId() {
        MapStructReq req = new MapStructReq();
        req.setName("alice");
        req.setPhone("13800000000");

        MapStructResp resp = mapperStructService.test(req);
        assertThat(resp.getName()).isEqualTo("alice");
        assertThat(resp.getId()).isEqualTo("13800000000");
    }
}
