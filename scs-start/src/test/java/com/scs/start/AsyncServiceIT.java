package com.scs.start;

import com.scs.app.demo.AsyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class AsyncServiceIT extends AbstractMysqlIT {

    @Autowired
    private AsyncService asyncService;

    @Test
    void asyncRunsOnVirtualThread() throws Exception {
        String result = asyncService.scsAsyncHandler().get(3, TimeUnit.SECONDS);
        assertThat(result).contains("virtual=true");
    }
}
