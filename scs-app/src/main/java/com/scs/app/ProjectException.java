package com.scs.app;

import com.alibaba.cola.exception.BaseException;
import com.scs.client.dto.data.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectException extends BaseException {

    private String[] errMessage;

    public ProjectException(ErrorCode errorCode, String... errMessage) {
        this(errorCode.getErrCode(), errMessage);
    }

    public ProjectException(String errCode, String... errMessage) {
        super(errCode);
        this.setErrCode(errCode);
        this.setErrMessage(errMessage);
    }
}
