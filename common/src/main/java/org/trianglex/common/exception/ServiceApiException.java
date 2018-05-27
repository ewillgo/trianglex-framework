package org.trianglex.common.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class ServiceApiException extends HystrixBadRequestException {

    private static final long serialVersionUID = -2874004519697114786L;

    private Throwable original;
    private ApiCode apiCode;
    private Object data;

    public ServiceApiException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.apiCode = apiCode;
    }

    public ServiceApiException(ApiCode apiCode, Throwable cause) {
        super(apiCode.getMessage(), cause);
        this.apiCode = apiCode;
        this.original = cause;
    }

    public ServiceApiException(ApiCode apiCode, Object data) {
        super(apiCode.getMessage());
        this.apiCode = apiCode;
        this.data = data;
    }

    public ServiceApiException(ApiCode apiCode, Object data, Throwable cause) {
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
