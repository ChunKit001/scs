package com.scs.customer;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.catchlog.CatchAndLog;
import com.scs.api.CustomerServiceI;
import com.scs.dto.CustomerAddCmd;
import com.scs.dto.CustomerListByNameQry;
import com.scs.dto.data.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.scs.customer.executor.CustomerAddCmdExe;
import com.scs.customer.executor.query.CustomerListByNameQryExe;

import jakarta.annotation.Resource;

@Slf4j
@Service
@CatchAndLog
public class CustomerServiceImpl implements CustomerServiceI {

    @Resource
    private CustomerAddCmdExe customerAddCmdExe;

    @Resource
    private CustomerListByNameQryExe customerListByNameQryExe;

    public Response addCustomer(CustomerAddCmd customerAddCmd) {
        return customerAddCmdExe.execute(customerAddCmd);
    }

    @Override
    public MultiResponse<CustomerDTO> listByName(CustomerListByNameQry customerListByNameQry) {
        int a = 1/0 ;
        return customerListByNameQryExe.execute(customerListByNameQry);
    }

}