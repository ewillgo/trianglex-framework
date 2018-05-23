package org.trianglex.common.web;


import java.time.Duration;

public abstract class AbstractHttpProperties {

    protected int maxIdleConnections = 5;
    protected Duration readTimeout;
    protected Duration connectTimeout;
    protected Duration writeTimeout;
    protected Duration keepAliveDuration;

    protected boolean followRedirects = true;
    protected boolean followSslRedirects = true;

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Duration getKeepAliveDuration() {
        return keepAliveDuration;
    }

    public void setKeepAliveDuration(Duration keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public boolean isFollowSslRedirects() {
        return followSslRedirects;
    }

    public void setFollowSslRedirects(boolean followSslRedirects) {
        this.followSslRedirects = followSslRedirects;
    }
}
