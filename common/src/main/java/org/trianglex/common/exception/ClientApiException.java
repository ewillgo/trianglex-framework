package org.trianglex.common.exception;

public class ClientApiException extends RuntimeException {

    private static final long serialVersionUID = 8376708483049204840L;
    private Throwable original;
    private ApiCode apiCode;
    private Object data;

    public ClientApiException(String message) {
        super(message);
    }

    public ClientApiException(String message, Throwable cause) {
        super(message, cause);
        this.original = cause;
    }

    public ClientApiException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.apiCode = apiCode;
    }

    public ClientApiException(ApiCode apiCode, Throwable cause) {
        super(apiCode.getMessage(), cause);
        this.apiCode = apiCode;
        this.original = cause;
    }

    public ClientApiException(ApiCode apiCode, Object data) {
        super(apiCode.getMessage());
        this.apiCode = apiCode;
        this.data = data;
    }

    public ClientApiException(ApiCode apiCode, Object data, Throwable cause) {
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
