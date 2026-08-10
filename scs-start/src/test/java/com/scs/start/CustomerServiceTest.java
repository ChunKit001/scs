package com.scs.start;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.scs.client.api.CustomerServiceI;
import com.scs.client.dto.CustomerAddCmd;
import com.scs.client.dto.CustomerListByNameQry;
import com.scs.client.dto.data.CustomerDTO;
import com.scs.client.dto.data.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerServiceTest extends AbstractMysqlIT {

    @Autowired
    private CustomerServiceI customerService;

    @Test
    void addThenListByCompanyName() {
        String company = "Acme-" + System.nanoTime();

        CustomerAddCmd cmd = new CustomerAddCmd();
        CustomerDTO dto = new CustomerDTO();
        dto.setCompanyName(company);
        dto.setSource("TEST");
        cmd.setCustomerDTO(dto);

        Response add = customerService.addCustomer(cmd);
        assertThat(add.isSuccess()).isTrue();

        CustomerListByNameQry qry = new CustomerListByNameQry();
        qry.setName(company);
        MultiResponse<CustomerDTO> listed = customerService.listByName(qry);
        assertThat(listed.isSuccess()).isTrue();
        assertThat(listed.getData()).hasSize(1);
        assertThat(listed.getData().get(0).getCompanyName()).isEqualTo(company);
        assertThat(listed.getData().get(0).getCustomerId()).isNotBlank();
    }

    @Test
    void addCustomerCompanyNameConflict() {
        CustomerAddCmd cmd = new CustomerAddCmd();
        CustomerDTO dto = new CustomerDTO();
        dto.setCompanyName("ConflictCompanyName");
        dto.setSource("TEST");
        cmd.setCustomerDTO(dto);

        Response response = customerService.addCustomer(cmd);
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getErrCode()).isEqualTo(ErrorCode.B_CUSTOMER_companyNameConflict.getErrCode());
    }

    @Test
    void duplicateCompanyNameRejected() {
        String company = "Dup-" + System.nanoTime();

        CustomerAddCmd first = new CustomerAddCmd();
        CustomerDTO dto1 = new CustomerDTO();
        dto1.setCompanyName(company);
        dto1.setSource("TEST");
        first.setCustomerDTO(dto1);
        assertThat(customerService.addCustomer(first).isSuccess()).isTrue();

        CustomerAddCmd second = new CustomerAddCmd();
        CustomerDTO dto2 = new CustomerDTO();
        dto2.setCompanyName(company);
        dto2.setSource("TEST");
        second.setCustomerDTO(dto2);
        Response response = customerService.addCustomer(second);
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getErrCode()).isEqualTo(ErrorCode.B_CUSTOMER_companyNameConflict.getErrCode());
    }
}
