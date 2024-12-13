package com.scs.api;


import com.scs.dto.ValidDTO;
import org.springframework.validation.annotation.Validated;

public interface DemoServiceI {

    String valid1(@Validated ValidDTO validDTO);
}
