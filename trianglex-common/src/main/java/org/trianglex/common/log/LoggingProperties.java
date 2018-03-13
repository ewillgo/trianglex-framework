package org.trianglex.common.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@ConfigurationProperties(prefix = "log.config")
public class LoggingProperties {

    private LogLevel logLevel;
    private Set<String> ignoreUrls = new HashSet<>();
    private Set<String> ignoreSuffixs = new HashSet<>();

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public Set<String> getIgnoreUrls() {
        return ignoreUrls;
    }

    public void setIgnoreUrls(Set<String> ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public Set<String> getIgnoreSuffixs() {
        return ignoreSuffixs;
    }

    public void setIgnoreSuffixs(Set<String> ignoreSuffixs) {
        this.ignoreSuffixs = ignoreSuffixs;
    }
}
