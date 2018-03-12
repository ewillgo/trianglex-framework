package org.trianglex.common.web;

import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.trianglex.common.log.LogInfo;
import org.trianglex.common.log.LogLevel;
import org.trianglex.common.log.LoggingProperties;
import org.trianglex.common.util.JsonUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HttpClientInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientInterceptor.class);

    private LoggingProperties properties;
    private static final String JSON_TYPE = "application/json";

    public HttpClientInterceptor(LoggingProperties properties) {
        this.properties = properties;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        LogInfo.RequestInfo requestInfo = new LogInfo.RequestInfo();
        requestInfo.setRequestName("okreq");
        requestInfo.setUrl(request.url().url().toString());
        requestInfo.setMethod(request.method().toLowerCase());
        requestInfo.setParams(getQueryString(request));
        requestInfo.setData(getRequestBody(request));
        requestInfo.setHeaders(getHeaders(request.headers()));
        logger.info("{}", requestInfo.toString());

        long t1 = System.nanoTime();
        Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();

        MediaType mediaType = response.body().contentType();
        String responseString = getResponseString(response);
        LogInfo.ResponseInfo responseInfo = new LogInfo.ResponseInfo();
        responseInfo.setResponseName("okresp");
        responseInfo.setStatus(response.code());
        responseInfo.setTime(String.format("%.1fms", (t2 - t1) / 1e6d));
        responseInfo.setUrl(response.request().url().toString());
        responseInfo.setHeaders(getHeaders(response.headers()));
        responseInfo.setRespdata(getResponseData(response, responseString));
        logger.info("{}", responseInfo.toString());

        return response.newBuilder()
                .body(ResponseBody.create(mediaType, responseString))
                .build();
    }

    private Map<String, String> getHeaders(Headers headers) {

        if (LogLevel.PROD != properties.getLogLevel()) {
            return null;
        }

        Map<String, String> headerMap = new LinkedHashMap<>();
        headers.toMultimap().forEach((key, values) ->
                headerMap.put(key, values.stream().collect(Collectors.joining(","))));

        return headerMap;
    }

    private Map<String, String[]> getQueryString(Request request) {
        HttpUrl url = request.url();
        Set<String> nameSet = url.queryParameterNames();

        if (nameSet == null) {
            return null;
        }

        Map<String, String[]> paramMap = new LinkedHashMap<>();
        nameSet.forEach((name) -> paramMap.put(name, url.queryParameterValues(name).toArray(new String[0])));

        return paramMap.size() != 0 ? paramMap : null;
    }

    private String getResponseString(Response response) {
        String responseString = "";

        try {
            responseString = response.body().string();
        } catch (Exception ignore) {

        }

        return responseString;
    }

    private Object getResponseData(Response response, String responseString) {

        if (response.body() == null) {
            return null;
        }

        if (isJsonType(response.body().contentType()) && !StringUtils.isEmpty(responseString)) {
            try {
                return JsonUtils.parse(responseString, Map.class);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return (responseString != null && responseString.length() > 0) ? responseString : null;
    }

    private Object getRequestBody(Request request) {
        String bodyString = null;

        if (request.body() == null) {
            return null;
        }

        final Buffer buffer = new Buffer();

        try {
            request.newBuilder().build().body().writeTo(buffer);
            bodyString = buffer.readUtf8();
        } catch (IOException ignore) {
        }

        if (isJsonType(request.body().contentType()) && !StringUtils.isEmpty(bodyString)) {
            return JsonUtils.parse(bodyString, Map.class);
        } else {
            return (bodyString != null && bodyString.length() > 0) ? bodyString : null;
        }
    }

    private boolean isJsonType(MediaType mediaType) {
        return mediaType != null && JSON_TYPE.equalsIgnoreCase(String.format("%s/%s", mediaType.type(), mediaType.subtype()));
    }
}
