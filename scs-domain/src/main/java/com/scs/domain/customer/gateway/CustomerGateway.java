package com.scs.domain.customer.gateway;

import com.scs.domain.customer.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerGateway {

    Optional<Customer> getById(String customerId);

    List<Customer> listByCompanyName(String companyName);

    boolean existsByCompanyName(String companyName);

    void save(Customer customer);
}
