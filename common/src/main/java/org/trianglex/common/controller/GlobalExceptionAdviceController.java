package org.trianglex.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.trianglex.common.dto.Result;
import org.trianglex.common.exception.BusinessException;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionAdviceController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdviceController.class);

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public Result<String> exceptionHandler(Exception e) {
        Result<String> result = new Result<>();

        boolean isLogException = true;
        if (e instanceof DataAccessException) {
            result.setMessage("Database occur error.");
        } else if (e instanceof NoHandlerFoundException) {
            result.setMessage("No handler found.");
            isLogException = false;
        } else {
            result.setMessage(e.getMessage());
        }

        if (isLogException) {
            logger.error(e.getMessage(), e);
        }

        result.setStatus(Result.FAIL);
        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Result<Object> businessException(BusinessException e) {
        if (e.getOriginal() != null) {
            logger.error(e.getOriginal().getMessage(), e.getOriginal());
        }
        return Result.of(e.getStatus(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result<Map<String, String>> validationException(BindException e, BindingResult bindingResult) {
        Result<Map<String, String>> result = new Result<>();

        List<ObjectError> errorList = bindingResult.getAllErrors();
        for (ObjectError objectError : errorList) {
            try {
                String[] messages = objectError.getDefaultMessage().split("\\#");
                result.setStatus(Integer.valueOf(messages[0]));
                result.setMessage(messages[1]);
            } catch (Exception ex) {
                logger.error("DTO message format incorrect.");
            }

            return result;
        }

        result.setStatus(Result.FAIL);
        result.setMessage(Result.FAIL_MESSAGE);
        return result;
    }

}
