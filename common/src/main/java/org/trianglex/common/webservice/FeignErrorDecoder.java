package org.trianglex.common.webservice;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.trianglex.common.exception.ApiErrorException;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return new ApiErrorException("");
    }

}
