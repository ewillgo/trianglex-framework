package org.trianglex.common.exception;

public enum GlobalApiCode implements ApiCode {

    OPERATION_SUCCESS(0, "Operation success"),
    OPERATION_FAIL(-1, "Operation fail"),
    HYSTRIX_TIMEOUT(-100, "Remote call timeout"),
    HYSTRIX_ERROR(-101, "Remote call error"),
    DATABASE_ERROR(-102, "Database occur error"),
    FEIGN_MESSAGE_PARSE_ERROR(-103, "Feign message parse error"),
    REST_TEMPLATE_MESSAGE_PARSE_ERROR(-104, "Rest template message parse error");

    private Integer status;
    private String message;

    GlobalApiCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
