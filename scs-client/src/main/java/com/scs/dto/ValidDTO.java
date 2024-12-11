package com.scs.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ValidDTO {

    @NotNull
    private String username;
}
