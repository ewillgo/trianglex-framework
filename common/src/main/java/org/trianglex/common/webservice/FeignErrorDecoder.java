package org.trianglex.common.webservice;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.trianglex.common.dto.Result;
import org.trianglex.common.exception.ServiceApiException;
import org.trianglex.common.util.JsonUtils;

import static org.trianglex.common.exception.GlobalApiCode.FEIGN_MESSAGE_PARSE_ERROR;

public class FeignErrorDecoder extends ErrorDecoder.Default {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {

        Result result = null;
        Exception exception = null;
        String resultString = null;

        try {
            resultString = Util.toString(response.body().asReader());
            result = JsonUtils.parse(resultString, Result.class);
        } catch (Exception e) {
            exception = e;
        }

        LOGGER.error("The feign remote call '{}' result: {}", methodKey, resultString);

        ServiceApiException throwException = new ServiceApiException(
                result != null ? result : Result.of(FEIGN_MESSAGE_PARSE_ERROR), exception);
        throwException.setHttpStatus(HttpStatus.OK);

        return throwException;
    }

}
