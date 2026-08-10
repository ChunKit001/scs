package com.scs.infra.customer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.scs.domain.customer.Customer;
import com.scs.domain.customer.gateway.CustomerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(prefix = "scs.db", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class CustomerGatewayImpl implements CustomerGateway {

    private final CustomerMapper customerMapper;

    @Override
    public Optional<Customer> getById(String customerId) {
        return Optional.ofNullable(customerMapper.selectById(customerId)).map(this::toDomain);
    }

    @Override
    public List<Customer> listByCompanyName(String companyName) {
        return customerMapper.selectList(Wrappers.<CustomerDO>lambdaQuery()
                        .eq(CustomerDO::getCompanyName, companyName))
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCompanyName(String companyName) {
        return customerMapper.selectCount(Wrappers.<CustomerDO>lambdaQuery()
                .eq(CustomerDO::getCompanyName, companyName)) > 0;
    }

    @Override
    public void save(Customer customer) {
        customerMapper.insert(toDO(customer));
    }

    private Customer toDomain(CustomerDO source) {
        Customer target = new Customer();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    private CustomerDO toDO(Customer source) {
        CustomerDO target = new CustomerDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
