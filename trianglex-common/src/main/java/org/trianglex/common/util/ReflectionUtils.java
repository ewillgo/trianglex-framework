package org.trianglex.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class ReflectionUtils {

    private ReflectionUtils() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object instance, String name) {
        Object[] objects = new Object[1];
        org.springframework.util.ReflectionUtils.doWithFields(instance.getClass(), field -> {
            field.setAccessible(true);
            objects[0] = field.get(instance);
        }, (field) -> field.getName().equals(name));
        return (T) objects[0];
    }

    public static void setFieldValue(Object instance, String name, Object value) {
        Field[] fields = new Field[1];
        org.springframework.util.ReflectionUtils.doWithFields(instance.getClass(), field -> {
            fields[0] = field;
        }, field -> field.getName().equals(name));
        fields[0].setAccessible(true);
        org.springframework.util.ReflectionUtils.setField(fields[0], instance, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Object instance, String methodName, Class<?>[] paramTypes, Object[] args) {
        Method method = org.springframework.util.ReflectionUtils.findMethod(instance.getClass(), methodName, paramTypes);
        method.setAccessible(true);
        return (T) org.springframework.util.ReflectionUtils.invokeMethod(method, instance, args);
    }
}
