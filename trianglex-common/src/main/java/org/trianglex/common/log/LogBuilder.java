package org.trianglex.common.log;

import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;
import org.trianglex.common.util.JsonUtils;
import org.trianglex.common.util.ToolUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LogBuilder {

    private final LogLevel logLevel;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private long startTime;
    private long endTime;

    LogBuilder(HttpServletRequest request, HttpServletResponse response, LogLevel logLevel) {
        this.request = LogHelper.logRequestBodyIfNecessary(request);
        this.response = LogHelper.logResponseBodyIfNecessary(request, response);
        this.logLevel = logLevel;
    }

    HttpServletRequest getHttpServletRequest() {
        return request;
    }

    HttpServletResponse getHttpServletResponse() {
        return response;
    }

    String buildRequestLog() {
        LogInfo.RequestInfo requestInfo = new LogInfo.RequestInfo();
        requestInfo.setUrl(getUrlWithQueryString());
        requestInfo.setIp(ToolUtils.getRemoteIp(request));
        requestInfo.setMethod(request.getMethod().toLowerCase());
        requestInfo.setParams(getParameterMap());
        requestInfo.setData(getRequestBody(request.getContentType()));
        requestInfo.setHeaders(logLevel == LogLevel.PROD ? getRequestHeaders() : null);
        return requestInfo.toString();
    }

    String buildResponseLog(String contentType) {
        LogInfo.ResponseInfo responseInfo = new LogInfo.ResponseInfo();
        responseInfo.setStatus(response.getStatus());
        responseInfo.setTime(getSpendTime());
        responseInfo.setUrl(getUrlWithQueryString());
        responseInfo.setHeaders(logLevel == LogLevel.PROD ? getResponseHeaders() : null);
        responseInfo.setRespdata(getResponseData(contentType));
        return responseInfo.toString();
    }

    LogBuilder setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    LogBuilder setEndTime(long endTime) {
        this.endTime = endTime;
        return this;
    }

    private Map<String, String[]> getParameterMap() {
        Map<String, String[]> paramMap = request.getParameterMap();
        return paramMap.size() > 0 ? paramMap : null;
    }

    private boolean isLogRequestBody() {
        return request instanceof ContentCachingRequestWrapper;
    }

    private boolean isLogResponseBody(String contentType) {
        return response instanceof ContentCachingResponseWrapper && LogHelper.isLogResponseBody(contentType);
    }

    private String getSpendTime() {
        return String.format("%.1fms", (endTime - startTime) / 1e6d);
    }

    private Object getRequestBody(String contentType) {

        if (!isLogRequestBody()) {
            return null;
        }

        String requestBody = null;
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);

        try {
            requestBody = wrapper.getReader().lines().collect(Collectors.joining());
            if (!StringUtils.isEmpty(requestBody) && contentType.contains("json")) {
                return JsonUtils.parse(requestBody, Map.class);
            }
        } catch (Exception e) {
        }

        return requestBody;
    }

    private Object getResponseData(String contentType) {

        if (!isLogResponseBody(contentType)) {
            return null;
        }

        String responseData = null;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);

        try {
            byte[] bytes = wrapper.getContentAsByteArray();
            responseData = new String(bytes, 0, bytes.length, wrapper.getCharacterEncoding());
            if (!StringUtils.isEmpty(responseData) && contentType.contains("json")) {
                return JsonUtils.parse(responseData, Map.class);
            }
        } catch (Exception e) {

        } finally {
            try {
                wrapper.copyBodyToResponse();
            } catch (IOException e) {
            }
        }

        return responseData;
    }

    private Map<String, String> getRequestHeaders() {
        Map<String, String> headerMap = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String headerName = enumeration.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap.isEmpty() ? null : headerMap;
    }

    private Map<String, String> getResponseHeaders() {
        Map<String, String> headerMap = new LinkedHashMap<>();
        response.getHeaderNames().forEach((name) -> {
            headerMap.put(name, response.getHeader(name));
        });
        return headerMap.isEmpty() ? null : headerMap;
    }

    /**
     * Get annotation string
     *
     * @param annotations Annotation object
     * @return Annotation string
     */
    private String getAnnotationString(Annotation[] annotations) {
        String annotationString = "";
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                annotationString += "@" + annotation.annotationType().getSimpleName() + " ";
            }
        }
        return annotationString;
    }

    /**
     * Get http request url with query string
     *
     * @return Query string
     */
    private String getUrlWithQueryString() {
        String queryString = request.getQueryString();
        return ToolUtils.decodeUrl(request.getRequestURL().toString()) + (queryString != null ? "?" + queryString : "");
    }
}
