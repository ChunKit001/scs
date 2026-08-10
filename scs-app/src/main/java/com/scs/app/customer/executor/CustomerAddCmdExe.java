package com.scs.app.customer.executor;

import com.alibaba.cola.dto.Response;
import com.scs.client.dto.CustomerAddCmd;
import com.scs.client.dto.data.CustomerDTO;
import com.scs.client.dto.data.ErrorCode;
import com.scs.domain.customer.Customer;
import com.scs.domain.customer.gateway.CustomerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "scs.db", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class CustomerAddCmdExe {

    private final CustomerGateway customerGateway;

    public Response execute(CustomerAddCmd cmd) {
        CustomerDTO dto = cmd.getCustomerDTO();
        if (dto == null || !StringUtils.hasText(dto.getCompanyName())) {
            return Response.buildFailure(ErrorCode.P_VALIDATION.getErrCode(), ErrorCode.P_VALIDATION.getErrDesc());
        }

        Customer customer = new Customer();
        customer.setCustomerId(StringUtils.hasText(dto.getCustomerId()) ? dto.getCustomerId() : UUID.randomUUID().toString());
        customer.setMemberId(dto.getMemberId());
        customer.setCompanyName(dto.getCompanyName());
        customer.setRegisteredCapital(0L);

        // 演示用冲突名 + 库内同名冲突
        if ("ConflictCompanyName".equals(customer.getCompanyName())
                || customerGateway.existsByCompanyName(customer.getCompanyName())) {
            return Response.buildFailure(
                    ErrorCode.B_CUSTOMER_companyNameConflict.getErrCode(),
                    ErrorCode.B_CUSTOMER_companyNameConflict.getErrDesc());
        }

        customerGateway.save(customer);
        return Response.buildSuccess();
    }
}
