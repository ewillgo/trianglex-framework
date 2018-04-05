package org.trianglex.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trianglex.common.dto.Result;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception e) {
        logger.error(e.getMessage(), e);
        Result<String> result = new Result<>();
        result.setStatus(Result.FAIL);
        result.setMessage(e.getMessage());
        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result<Map<String, String>> validationException(BindException e, BindingResult bindingResult) {
        logger.error(e.getMessage(), e);
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
