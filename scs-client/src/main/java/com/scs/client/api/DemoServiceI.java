package com.scs.client.api;


import com.scs.client.dto.ValidDTO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface DemoServiceI {

    String valid1(@Valid ValidDTO validDTO);
}
