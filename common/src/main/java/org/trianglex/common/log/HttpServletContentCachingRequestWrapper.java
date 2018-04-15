package org.trianglex.common.log;

import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class HttpServletContentCachingRequestWrapper extends ContentCachingRequestWrapper {

    private byte[] bodyBytes = null;

    public HttpServletContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (bodyBytes == null) {
            bodyBytes = StreamUtils.copyToByteArray(super.getInputStream());
        }

        return new DelegatingServletInputStream(new ByteArrayInputStream(bodyBytes));
    }

}
