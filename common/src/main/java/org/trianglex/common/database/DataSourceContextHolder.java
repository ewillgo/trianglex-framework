package org.trianglex.common.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class DataSourceContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceContextHolder.class);
    private static final ThreadLocal<Stack<String>> HOLDER = ThreadLocal.withInitial(Stack<String>::new);
    private static final Set<String> DATA_SOURCE_NAMES = new HashSet<>();

    private DataSourceContextHolder() {
    }

    static Set<String> getDataSourceNames() {
        return Collections.unmodifiableSet(DATA_SOURCE_NAMES);
    }

    static void setDataSourceName(String dataSourceName) {
        DATA_SOURCE_NAMES.add(dataSourceName);
    }

    public static boolean containsDataSourceName(String dataSourceName) {
        return DATA_SOURCE_NAMES.contains(dataSourceName);
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
