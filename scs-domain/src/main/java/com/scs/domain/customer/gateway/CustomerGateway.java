package com.scs.domain.customer.gateway;

import com.scs.domain.customer.Customer;

public interface CustomerGateway {
    Customer getByById(String customerId);
}
