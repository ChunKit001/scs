package com.scs.infra.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("customer")
public class CustomerDO {

    @TableId(value = "customer_id", type = IdType.INPUT)
    private String customerId;
    private String memberId;
    private String globalId;
    private long registeredCapital;
    private String companyName;
}
