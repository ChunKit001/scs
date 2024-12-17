package com.scs.client.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ValidDTO {

    @NotNull
    private String username;

    @NotNull
    private String phone;
}
