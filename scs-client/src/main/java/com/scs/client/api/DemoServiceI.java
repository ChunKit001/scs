package com.scs.client.api;


import com.scs.client.dto.ValidDTO;
import org.springframework.validation.annotation.Validated;

public interface DemoServiceI {

    String valid1(ValidDTO validDTO);
}
