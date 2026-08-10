package com.scs.adapter.web;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.scs.client.api.CustomerServiceI;
import com.scs.client.dto.CustomerAddCmd;
import com.scs.client.dto.CustomerListByNameQry;
import com.scs.client.dto.data.CustomerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer 真读写入口：Cmd → App → Gateway → MyBatis-Plus → DB。
 */
@Tag(name = "Customer", description = "客户新增 / 按公司名查询（需 scs.db.enabled=true）")
@RestController
@RequestMapping("customer")
@ConditionalOnProperty(prefix = "scs.db", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceI customerService;

    @Operation(summary = "新增客户（落库）")
    @PostMapping
    public Response add(@Valid @RequestBody CustomerAddCmd cmd) {
        return customerService.addCustomer(cmd);
    }

    @Operation(summary = "按公司名精确查询")
    @GetMapping
    public MultiResponse<CustomerDTO> listByName(@RequestParam("name") String name) {
        CustomerListByNameQry qry = new CustomerListByNameQry();
        qry.setName(name);
        return customerService.listByName(qry);
    }
}
