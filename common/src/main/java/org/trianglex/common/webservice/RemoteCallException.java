package org.trianglex.common.webservice;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class RemoteCallException extends HystrixBadRequestException {

    public RemoteCallException(String message) {
        super(message);
    }

    public RemoteCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
