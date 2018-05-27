package org.trianglex.common.webservice;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.trianglex.common.exception.ClientApiException;

public class FeignErrorDecoder extends ErrorDecoder.Default {

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception;

        try {
            exception = super.decode(methodKey, response);
        } catch (Exception e) {
            exception = e;
        }

        return new ClientApiException("FeignErrorDecoder throw error.", exception);
    }

}
