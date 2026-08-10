package com.scs.app.customer.executor.query;

import com.alibaba.cola.dto.MultiResponse;
import com.scs.client.dto.CustomerListByNameQry;
import com.scs.client.dto.data.CustomerDTO;
import com.scs.domain.customer.Customer;
import com.scs.domain.customer.gateway.CustomerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(prefix = "scs.db", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class CustomerListByNameQryExe {

    private final CustomerGateway customerGateway;

    public MultiResponse<CustomerDTO> execute(CustomerListByNameQry qry) {
        if (qry == null || !StringUtils.hasText(qry.getName())) {
            return MultiResponse.of(Collections.emptyList());
        }
        List<CustomerDTO> list = customerGateway.listByCompanyName(qry.getName()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return MultiResponse.of(list);
    }

    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setMemberId(customer.getMemberId());
        dto.setCompanyName(customer.getCompanyName());
        // DTO 历史字段：用公司名回填展示名
        dto.setCustomerName(customer.getCompanyName());
        return dto;
    }
}
