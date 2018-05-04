package org.trianglex.common.exception;

public class BusinessException extends RuntimeException {

    private Throwable original;
    private Integer status;
    private String message;
    private Object data;

    public BusinessException(Integer status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public BusinessException(Integer status, String message, Object data) {
        super(message);
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public BusinessException(Integer status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message = message;
        this.original = cause;
    }

    public BusinessException(Integer status, String message, Object data, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message = message;
        this.data = data;
        this.original = cause;
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.original = cause;
    }

    public Throwable getOriginal() {
        return original;
    }

    public Integer getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }
}
