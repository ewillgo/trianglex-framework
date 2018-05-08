package org.trianglex.common.dto;


import org.trianglex.common.exception.BusinessCode;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = -5058196658470038394L;
    private Integer status;
    private String message;
    private T data;

    public Result() {

    }

    public Result(BusinessCode businessCode) {
        this.status = businessCode.getStatus();
        this.message = businessCode.getMessage();
    }

    public Result(BusinessCode businessCode, T data) {
        this.status = businessCode.getStatus();
        this.message = businessCode.getMessage();
        this.data = data;
    }

    public Result(Integer status, String message) {
        this(status, message, null);
    }

    public Result(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

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

    public static <T> Result<T> of(BusinessCode businessCode) {
        return of(businessCode, null);
    }

    public static <T> Result<T> of(BusinessCode businessCode, T data) {
        return new Result<>(businessCode, data);
    }

    public static <T> Result<T> of(Integer status, String message) {
        return of(status, message, null);
    }

    public static <T> Result<T> of(Integer status, String message, T data) {
        return new Result<>(status, message, data);
    }

}
