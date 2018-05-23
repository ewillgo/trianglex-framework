package org.trianglex.common.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class ApiErrorException extends HystrixBadRequestException {

    private Throwable original;
    private ApiCode apiCode;
    private Object data;

    public ApiErrorException(String message) {
        super(message);
    }

    public ApiErrorException(String message, Throwable cause) {
        super(message, cause);
        this.original = cause;
    }

    public ApiErrorException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.apiCode = apiCode;
    }

    public ApiErrorException(ApiCode apiCode, Throwable cause) {
        super(apiCode.getMessage(), cause);
        this.apiCode = apiCode;
        this.original = cause;
    }

    public ApiErrorException(ApiCode apiCode, Object data) {
        super(apiCode.getMessage());
        this.apiCode = apiCode;
        this.data = data;
    }

    public ApiErrorException(ApiCode apiCode, Object data, Throwable cause) {
        super(apiCode.getMessage(), cause);
        this.apiCode = apiCode;
        this.data = data;
        this.original = cause;
    }

    public Throwable getOriginal() {
        return original;
    }

    public ApiCode getApiCode() {
        return apiCode;
    }

    public Object getData() {
        return data;
    }
}
