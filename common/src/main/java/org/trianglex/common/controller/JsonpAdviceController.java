package org.trianglex.common.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

@ControllerAdvice
public class JsonpAdviceController extends AbstractJsonpResponseBodyAdvice {

    public JsonpAdviceController() {
        super("callback", "jsonp");
    }
}
