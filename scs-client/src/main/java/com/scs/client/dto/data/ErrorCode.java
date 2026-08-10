package com.scs.client.dto.data;

/**
 * 统一错误码约定（COLA 风格前缀）：
 * <ul>
 *   <li>S_ 系统异常</li>
 *   <li>P_ 参数/协议异常</li>
 *   <li>B_ 业务异常</li>
 * </ul>
 * 文案优先走 i18n（messages*.properties 的 key = errCode）。
 */
public enum ErrorCode {

    S_INTERNAL("S_INTERNAL", "系统内部错误"),

    P_METHOD_NOT_ALLOWED("P_METHOD_NOT_ALLOWED", "请求方法不支持"),
    P_VALIDATION("P_VALIDATION", "参数校验失败"),
    P_PAGE_INVALID("P_PAGE_INVALID", "分页参数非法"),
    P_IDEMPOTENCY_KEY_REQUIRED("P_IDEMPOTENCY_KEY_REQUIRED", "缺少幂等键 X-Idempotency-Key"),

    B_DEMO_SAMPLE("B_DEMO_SAMPLE", "演示业务错误 {0},{1}"),
    B_CUSTOMER_companyNameConflict("B_CUSTOMER_companyNameConflict", "客户公司名冲突"),
    B_IDEMPOTENCY_IN_PROGRESS("B_IDEMPOTENCY_IN_PROGRESS", "相同请求正在处理中"),
    B_IDEMPOTENCY_CONFLICT("B_IDEMPOTENCY_CONFLICT", "幂等键冲突");

    private final String errCode;
    private final String errDesc;

    ErrorCode(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }
}
