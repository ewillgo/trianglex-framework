package org.trianglex.common.dto;


public class Result<T> {

    public transient static final Integer SUCCESS = 0;
    public transient static final Integer FAIL = -1;
    public transient static final String SUCCESS_MESSAGE = "Operation success.";
    public transient static final String FAIL_MESSAGE = "Operation fail.";

    private Integer status;
    private String message;
    private T data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
