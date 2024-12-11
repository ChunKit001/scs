package com.scs.demo;

import com.scs.api.DemoServiceI;
import com.scs.dto.ValidDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@AllArgsConstructor
public class DemoService implements DemoServiceI {

    @Override
    public String valid1(ValidDTO validDTO) {
        return "s";
    }
}
