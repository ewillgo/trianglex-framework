package org.trianglex.common.web;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.trianglex.common.exception.ClientApiException;

import java.io.IOException;

public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        try {
            super.handleError(response);
        } catch (Exception e) {
            throw new ClientApiException("RestTemplateErrorHandler error.", e);
        }
    }
}
