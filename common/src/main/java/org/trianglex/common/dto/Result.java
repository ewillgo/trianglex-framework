package org.trianglex.common.dto;


import org.trianglex.common.exception.ApiCode;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = -5058196658470038394L;
    private Integer status;
    private String message;
    private T data;

    public Result() {

    }

    public Result(ApiCode apiCode) {
        this.status = apiCode.getStatus();
        this.message = apiCode.getMessage();
    }

    public Result(ApiCode apiCode, T data) {
        this.status = apiCode.getStatus();
        this.message = apiCode.getMessage();
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

    public static <T> Result<T> of(ApiCode apiCode) {
        return of(apiCode, null);
    }

    public static <T> Result<T> of(ApiCode apiCode, T data) {
        return new Result<>(apiCode, data);
    }

    public static <T> Result<T> of(Integer status) {
        return of(status, null, null);
    }

    public static <T> Result<T> of(String message) {
        return of(null, message, null);
    }

    public static <T> Result<T> of(Integer status, String message) {
        return of(status, message, null);
    }

    public static <T> Result<T> of(Integer status, String message, T data) {
        return new Result<>(status, message, data);
    }

}
