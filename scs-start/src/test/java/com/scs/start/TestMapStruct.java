package com.scs.start;

import com.scs.client.api.MapperStructServiceI;
import com.scs.client.dto.MapStructReq;
import com.scs.client.dto.MapStructResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TestMapStruct {
    @Autowired
    private MapperStructServiceI mapperStructService;

    @Test
    public void testMapStruct() {
        MapStructReq mapStructReq = new MapStructReq();
        mapStructReq.setName("123");
        mapStructReq.setPhone("456");
        MapStructResp test = mapperStructService.test(mapStructReq);
        log.info("resq:{}", test);
    }
}
