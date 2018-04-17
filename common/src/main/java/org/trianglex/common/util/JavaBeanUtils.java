package org.trianglex.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class JavaBeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(JavaBeanUtils.class);

    private JavaBeanUtils() {

    }

    public static Map<String, Object> beanToMap(Object bean) {

        if (bean == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    map.put(key, getter.invoke(bean));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return map;
    }
}
