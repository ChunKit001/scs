package com.scs.infra.customer;

import com.scs.domain.customer.Customer;
import com.scs.domain.customer.gateway.CustomerGateway;
import lombok.AllArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerGatewayImpl implements CustomerGateway {
    private final CustomerMapper customerMapper;

    public Customer getByById(String customerId) {
        CustomerDO customerDO = customerMapper.getById(customerId);
        //Convert to Customer
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDO, customer);
        return customer;
    }
}