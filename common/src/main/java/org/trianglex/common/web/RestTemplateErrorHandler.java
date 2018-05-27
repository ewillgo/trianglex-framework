package org.trianglex.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.trianglex.common.dto.Result;
import org.trianglex.common.exception.ServiceApiException;
import org.trianglex.common.util.JsonUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static org.trianglex.common.constant.PropertiesConstant.T_REQUEST_URI;
import static org.trianglex.common.exception.GlobalApiCode.REST_TEMPLATE_MESSAGE_PARSE_ERROR;

public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateErrorHandler.class);

    @Override
    public void handleError(ClientHttpResponse response) {

        Result result = null;
        String resultString = null;
        Exception exception = null;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody()))) {
            resultString = FileCopyUtils.copyToString(bufferedReader);
            if (!StringUtils.isEmpty(resultString)) {
                result = JsonUtils.parse(resultString, Result.class);
            }
        } catch (Exception e) {
            exception = e;
        }

        if (result != null) {
            List<String> headers = response.getHeaders().get(T_REQUEST_URI);
            LOGGER.error("The rest template remote call '{}' result: {}",
                    CollectionUtils.isEmpty(headers) ? "" : headers.get(0), resultString);
        } else {
            LOGGER.error("The rest template remote call result: {}", resultString);
        }

        ServiceApiException throwException = new ServiceApiException(
                result != null ? result : Result.of(REST_TEMPLATE_MESSAGE_PARSE_ERROR), exception);
        throwException.setHttpStatus(HttpStatus.OK);

        throw throwException;
    }
}
