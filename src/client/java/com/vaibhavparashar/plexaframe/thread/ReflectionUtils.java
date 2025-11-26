package com.vaibhavparashar.plexaframe.thread;

import java.lang.reflect.Method;

/** Small utility to call common reflection variants safely. */
public final class ReflectionUtils {

    private ReflectionUtils() {}

    // Try multiple getter names to return a double; returns NaN if none work
    public static double readDouble(Object target, String... methodNames) {
        if (target == null) return Double.NaN;
        Class<?> cls = target.getClass();
        for (String mName : methodNames) {
            try {
                Method m = cls.getMethod(mName);
                Object v = m.invoke(target);
                if (v instanceof Number) return ((Number)v).doubleValue();
                // if returned vector types, try reflection on them:
                if (v != null) {
                    try {
                        Method mx = v.getClass().getMethod("x");
                        Object ox = mx.invoke(v);
                        if (ox instanceof Number) return ((Number)ox).doubleValue();
                    } catch (Throwable ignored) {}
                }
            } catch (Throwable ignored) {}
        }
        return Double.NaN;
    }

    // Try to invoke any one of the method names on the target; return true if succeeded
    public static boolean tryInvokeAny(Object target, String... methodNames) {
        if (target == null) return false;
        Class<?> cls = target.getClass();
        for (String mName : methodNames) {
            try {
                Method m = cls.getMethod(mName);
                m.invoke(target);
                return true;
            } catch (Throwable ignored) {}
        }
        return false;
    }

    // Try a specific no-arg invocation, return true if succeeded
    public static boolean tryInvoke(Object target, String methodName) {
        if (target == null) return false;
        Class<?> cls = target.getClass();
        try {
            Method m = cls.getMethod(methodName);
            m.invoke(target);
            return true;
        } catch (Throwable ignored) {}
        return false;
    }
}
