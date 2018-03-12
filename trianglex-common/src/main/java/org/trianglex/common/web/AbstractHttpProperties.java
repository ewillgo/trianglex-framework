package org.trianglex.common.web;


import java.util.concurrent.TimeUnit;

public abstract class AbstractHttpProperties {

    protected int maxIdleConnections = 5;
    protected long readTimeout = TimeUnit.SECONDS.toMillis(1);
    protected long connectTimeout = TimeUnit.SECONDS.toMillis(1);
    protected long writeTimeout = TimeUnit.SECONDS.toMillis(1);
    protected long keepAliveDuration = TimeUnit.SECONDS.toSeconds(61);

    protected boolean followRedirects = true;
    protected boolean followSslRedirects = true;

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

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public long getKeepAliveDuration() {
        return keepAliveDuration;
    }

    public void setKeepAliveDuration(long keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }
}
