package org.trianglex.common.exception;

enum GlobalApiCode implements ApiCode {

    OPERATION_SUCCESS(0, "Operation success"),
    OPERATION_FAIL(-1, "Operation fail"),
    HYSTRIX_TIMEOUT(-100, "Remote call timeout"),
    HYSTRIX_ERROR(-101, "Remote call error"),
    DATABASE_ERROR(-102, "Database occur error"),
    NO_HANDLER(-103, "No handler found");

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
