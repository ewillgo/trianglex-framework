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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.trianglex.common.dto.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.trianglex.common.constant.PropertiesConstant.ERROR_REQUEST_URI;
import static org.trianglex.common.exception.GlobalApiCode.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleCommonException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        LOGGER.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, Result.of(OPERATION_FAIL), headers, HttpStatus.OK, request);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        LOGGER.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, Result.of(DATABASE_ERROR), headers, HttpStatus.OK, request);
    }

    @ResponseBody
    @ExceptionHandler(ServiceApiException.class)
    public Result<Object> handleServiceApiException(ServiceApiException ex, HttpServletRequest request, HttpServletResponse response) {

        response.setHeader(ERROR_REQUEST_URI, request.getRequestURI());
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(ex.getHttpStatus() != null ? ex.getHttpStatus().value() : ex.getApiCode().getStatus());

        if (ex.getOriginal() != null) {
            LOGGER.error(ex.getOriginal().getMessage(), ex.getOriginal());
        }

        return Result.of(ex.getApiCode(), ex.getData());
    }

    @ExceptionHandler(ClientApiException.class)
    public ResponseEntity<Object> handleClientApiException(ClientApiException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex.getOriginal() != null) {
            LOGGER.error(ex.getOriginal().getMessage(), ex.getOriginal());
        }

        return handleExceptionInternal(ex, ex.getApiCode() != null
                ? Result.of(ex.getApiCode(), ex.getData())
                : Result.of(OPERATION_FAIL.getStatus(), ex.getMessage(), ex.getData()), headers, HttpStatus.OK, request);
    }

    @ExceptionHandler(HystrixTimeoutException.class)
    public ResponseEntity<Object> handleHystrixTimeoutException(HystrixTimeoutException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        LOGGER.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, Result.of(HYSTRIX_TIMEOUT), headers, HttpStatus.OK, request);
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public ResponseEntity<Object> handleHystrixRuntimeException(HystrixRuntimeException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        LOGGER.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, Result.of(HYSTRIX_ERROR), headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpStatus[] httpStatus = new HttpStatus[1];
        httpStatus[0] = status;
        return handleExceptionInternal(ex, processBindingResult(
                ex.getBindingResult(), httpStatus), headers, httpStatus[0], request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpStatus[] httpStatus = new HttpStatus[1];
        httpStatus[0] = status;
        return handleExceptionInternal(ex, processBindingResult(
                ex.getBindingResult(), httpStatus), headers, httpStatus[0], request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (request instanceof ServletWebRequest) {
            HttpServletRequest httpServletRequest = ((ServletWebRequest) request).getRequest();
            HttpServletResponse httpServletResponse = ((ServletWebRequest) request).getResponse();
            httpServletResponse.setHeader(ERROR_REQUEST_URI, httpServletRequest.getRequestURI());
        }

        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Result<Map<String, String>> processBindingResult(BindingResult bindingResult, HttpStatus[] httpStatus) {
        Result<Map<String, String>> result = new Result<>();

        List<ObjectError> errorList = bindingResult.getAllErrors();
        for (ObjectError objectError : errorList) {
            try {
                String[] messages = objectError.getDefaultMessage().split("\\#");
                result.setStatus(Integer.valueOf(messages[0]));
                result.setMessage(messages[1]);

                if (result.getStatus() >= 1000) {
                    httpStatus[0] = HttpStatus.OK;
                }

            } catch (Exception ex) {
                LOGGER.error("The '{}' message's format incorrect, usage: code#message.",
                        ((FieldError) objectError).getField());
            }
            return result;
        }

        return Result.of(GlobalApiCode.OPERATION_FAIL);
    }
}
