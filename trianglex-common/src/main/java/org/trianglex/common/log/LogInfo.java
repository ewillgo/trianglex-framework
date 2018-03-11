package org.trianglex.common.log;

import org.trianglex.common.util.JsonUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class LogInfo {

    protected String url;
    protected Map<String, String> headers;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public static class RequestInfo extends LogInfo {
        private String ip;
        private String method;
        private Map<String, String[]> params;
        private Object data;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Map<String, String[]> getParams() {
            return params;
        }

        public void setParams(Map<String, String[]> params) {
            this.params = params;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        @Override
        public String toString() {
            Map<String, Object> map = new LinkedHashMap<>(1);
            map.put("request", this);
            return JsonUtils.toJsonString(map);
        }
    }

    public static class ResponseInfo extends LogInfo {
        private int status;
        private String time;
        private Object respdata;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Object getRespdata() {
            return respdata;
        }

        public void setRespdata(Object respdata) {
            this.respdata = respdata;
        }

        @Override
        public String toString() {
            Map<String, Object> map = new LinkedHashMap<>(1);
            map.put("response", this);
            return JsonUtils.toJsonString(map);
        }
    }
}
