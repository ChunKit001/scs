package com.scs.customer;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.scs.api.CustomerServiceI;
import com.scs.customer.executor.CustomerAddCmdExe;
import com.scs.customer.executor.query.CustomerListByNameQryExe;
import com.scs.dto.CustomerAddCmd;
import com.scs.dto.CustomerListByNameQry;
import com.scs.dto.data.CustomerDTO;
import com.scs.handler.ScsAsyncHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
//@CatchAndLog
public class CustomerServiceImpl implements CustomerServiceI {

    @Resource
    private CustomerAddCmdExe customerAddCmdExe;

    @Resource
    private CustomerListByNameQryExe customerListByNameQryExe;

    @Autowired
    private ScsAsyncHandler scsAsyncHandler;

    public Response addCustomer(CustomerAddCmd customerAddCmd) {
        return customerAddCmdExe.execute(customerAddCmd);
    }

    @Override
    public MultiResponse<CustomerDTO> listByName(CustomerListByNameQry customerListByNameQry) {
        scsAsyncHandler.scsAsyncHandler();
        return customerListByNameQryExe.execute(customerListByNameQry);
    }

}