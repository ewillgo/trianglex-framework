package org.trianglex.common.exception;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.trianglex.common.dto.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.trianglex.common.exception.GlobalApiCode.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleCommonException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        LOGGER.error(ex.getMessage(), ex);
        return super.handleExceptionInternal(ex, Result.of(OPERATION_FAIL), headers, HttpStatus.OK, request);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        LOGGER.error(ex.getMessage(), ex);
        return super.handleExceptionInternal(ex, Result.of(DATABASE_ERROR), headers, HttpStatus.OK, request);
    }

    @ResponseBody
    @ExceptionHandler(ServiceApiException.class)
    public Result<Object> handleServiceApiException(ServiceApiException ex, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(ex.getApiCode().getStatus());

        if (ex.getOriginal() != null) {
            LOGGER.error(ex.getOriginal().getMessage(), ex.getOriginal());
        }

        return Result.of(ex.getApiCode(), ex.getData());
    }

    @ExceptionHandler(ClientApiException.class)
    public ResponseEntity<Object> handleClientApiException(ClientApiException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        if (ex.getOriginal() != null) {
            LOGGER.error(ex.getOriginal().getMessage(), ex.getOriginal());
        }

        return super.handleExceptionInternal(ex, ex.getApiCode() != null
                ? Result.of(ex.getApiCode(), ex.getData())
                : Result.of(OPERATION_FAIL.getStatus(), ex.getMessage(), ex.getData()), headers, HttpStatus.OK, request);
    }

    @ExceptionHandler(HystrixTimeoutException.class)
    public ResponseEntity<Object> handleHystrixTimeoutException(HystrixTimeoutException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        LOGGER.error(ex.getMessage(), ex);
        return super.handleExceptionInternal(ex, Result.of(HYSTRIX_TIMEOUT), headers, HttpStatus.OK, request);
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public ResponseEntity<Object> handleHystrixRuntimeException(HystrixRuntimeException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        LOGGER.error(ex.getMessage(), ex);
        return super.handleExceptionInternal(ex, Result.of(HYSTRIX_ERROR), headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, processBindingResult(ex.getBindingResult()), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, processBindingResult(ex.getBindingResult()), headers, status, request);
    }

    private Result<Map<String, String>> processBindingResult(BindingResult bindingResult) {
        Result<Map<String, String>> result = new Result<>();

        List<ObjectError> errorList = bindingResult.getAllErrors();
        for (ObjectError objectError : errorList) {
            try {
                String[] messages = objectError.getDefaultMessage().split("\\#");
                result.setStatus(Integer.valueOf(messages[0]));
                result.setMessage(messages[1]);
            } catch (Exception ex) {
                LOGGER.error("The '{}' message's format incorrect, usage: code#message.",
                        ((FieldError) objectError).getField());
            }
            return result;
        }

        return Result.of(GlobalApiCode.OPERATION_FAIL);
    }
}
