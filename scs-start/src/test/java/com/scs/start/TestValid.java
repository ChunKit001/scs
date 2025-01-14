package com.scs.start;

import com.scs.client.api.DemoServiceI;
import com.scs.client.dto.ValidDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TestValid {
    @Autowired
    private DemoServiceI demoServiceI;

    @Test
    public void testValid() {
        ValidDTO validDTO = new ValidDTO();
        String s = demoServiceI.valid1(validDTO);
        log.info("result:{}", s);
    }
}
