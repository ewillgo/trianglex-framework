package org.trianglex.common.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EmptyStackException;
import java.util.Stack;

public abstract class DynamicDataSourceContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
    private static final ThreadLocal<Stack<String>> HOLDER = ThreadLocal.withInitial(Stack<String>::new);

    private DynamicDataSourceContextHolder() {
    }

    public static boolean containsDataSourceName(String dataSourceName) {
        return HOLDER.get().contains(dataSourceName);
    }

    public static String peekCurrentDataSourceName() {
        String dataSourceName = null;
        try {
            dataSourceName = HOLDER.get().peek();
            if (logger.isDebugEnabled()) {
                logger.debug("Current data source: {}, stack: {}", dataSourceName, HOLDER.get().toString());
            }
        } catch (EmptyStackException ignore) {
        }
        return dataSourceName;
    }

    public static String removeCurrentDataSourceName() {
        String dataSourceName = HOLDER.get().pop();
        if (logger.isDebugEnabled()) {
            logger.debug("Removed data source: {}, stack: {}", dataSourceName, HOLDER.get().toString());
        }
        return dataSourceName;
    }

    public static void setCurrentDataSourceName(String dataSourceName) {
        HOLDER.get().push(dataSourceName);
        if (logger.isDebugEnabled()) {
            logger.debug("Changed to data source: {}, stack: {}", dataSourceName, HOLDER.get().toString());
        }
    }
}
