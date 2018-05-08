package org.trianglex.common.exception;

public class BusinessException extends RuntimeException {

    private Throwable original;
    private BusinessCode businessCode;
    private Object data;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.original = cause;
    }

    public BusinessException(BusinessCode businessCode) {
        super(businessCode.getMessage());
        this.businessCode = businessCode;
    }

    public BusinessException(BusinessCode businessCode, Throwable cause) {
        super(businessCode.getMessage(), cause);
        this.businessCode = businessCode;
        this.original = cause;
    }

    public BusinessException(BusinessCode businessCode, Object data) {
        super(businessCode.getMessage());
        this.businessCode = businessCode;
        this.data = data;
    }

    public BusinessException(BusinessCode businessCode, Object data, Throwable cause) {
        super(businessCode.getMessage(), cause);
        this.businessCode = businessCode;
        this.data = data;
        this.original = cause;
    }

    public Throwable getOriginal() {
        return original;
    }

    public BusinessCode getBusinessCode() {
        return businessCode;
    }

    public Object getData() {
        return data;
    }
}
