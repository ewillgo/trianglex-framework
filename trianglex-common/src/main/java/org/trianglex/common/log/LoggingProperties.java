package org.trianglex.common.log;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ConfigurationProperties(prefix = "log.config")
public class LoggingProperties implements InitializingBean {

    private LogLevel logLevel;
    private String ignoreUrls;
    private String ignoreSuffixs;

    private Set<String> ignoreUrlSet = new HashSet<>();
    private Set<String> ignoreSuffixSet = new HashSet<>();

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getIgnoreUrls() {
        return ignoreUrls;
    }

    public void setIgnoreUrls(String ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public String getIgnoreSuffixs() {
        return ignoreSuffixs;
    }

    public void setIgnoreSuffixs(String ignoreSuffixs) {
        this.ignoreSuffixs = ignoreSuffixs;
    }

    public Set<String> getIgnoreUrlSet() {
        return ignoreUrlSet;
    }

    public void setIgnoreUrlSet(Set<String> ignoreUrlSet) {
        this.ignoreUrlSet = ignoreUrlSet;
    }

    public Set<String> getIgnoreSuffixSet() {
        return ignoreSuffixSet;
    }

    public void setIgnoreSuffixSet(Set<String> ignoreSuffixSet) {
        this.ignoreSuffixSet = ignoreSuffixSet;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ignoreUrlSet.addAll(Arrays.asList(ignoreUrls.replaceAll("\\s+", "").split(",")));
        ignoreSuffixSet.addAll(Arrays.asList(ignoreSuffixs.replaceAll("\\s+", "").split(",")));
    }
}
