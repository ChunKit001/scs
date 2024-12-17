package com.scs.infra.customer;

import com.scs.domain.customer.gateway.CreditGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreditGatewayImpl implements CreditGateway {
    public Credit getCredit(String customerId) {
        return null;
    }
}
