package com.scs.app.demo;

import com.scs.client.api.DemoServiceI;
import com.scs.client.dto.ValidDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@AllArgsConstructor
public class DemoService implements DemoServiceI {

    @Override
    public String valid1(@Validated ValidDTO validDTO) {
        return "s";
    }
}
