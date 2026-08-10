package com.scs.client.dto;

import com.scs.client.dto.data.CustomerDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerAddCmd {

    @NotNull
    @Valid
    private CustomerDTO customerDTO;
}
