package com.vaibhavparashar.plexaframe.client;

import com.vaibhavparashar.plexaframe.PlexaClient;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TurboOptimizer - aggressive FPS boost via client options reflection.
 *
 * Designed for Yarn 1.21.1+build.3 but written to tolerate slight API differences
 * by using reflection to set options/fields and to call SimpleOption#setValue where available.
 *
 * WARNING: This changes client-side options in-memory. If you want to persist them,
 * write them to disk via your normal config save mechanism.
 */
public final class TurboOptimizer {

    /** Call this once from your client initializer to apply the ultra boost. */
    public static void applyUltraBoost() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null) return;

            Object options = getOptionsObject(client);
            if (options == null) {
                PlexaClient.LOGGER.warn("TurboOptimizer: client.options not found");
                return;
            }

            // Aggressive disables / minimal quality
            // Particle setting -> minimal / decreased
            setOptionValue(options, "particles", 0); // 0 = minimal in many mappings
            // Clouds -> off
            setOptionValue(options, "clouds", 0);
            // Fancy graphics / fancy leaves -> off (booleans or int modes)
            setOptionValue(options, "fancyGraphics", false);
            setOptionValue(options, "fancyLeaves", false);
            // Entity shadows -> off (boolean)
            setOptionValue(options, "entityShadows", false);
            // Ambient occlusion -> off (some mappings use enum or int)
            setOptionValue(options, "ambientOcclusion", 0);
            // Smooth lighting -> off/low
            setOptionValue(options, "smoothLighting", 0);
            // Graphics mode -> fast (if enum or int)
            setOptionValue(options, "graphicsMode", 0); // 0 usually = fast
            // FOV -> small
            setNumericOption(options, "fov", 70.0f); // set small-ish FOV
            // Render / view distance -> low (2-4 chunks)
            setNumericOption(options, "renderDistance", 4); // some mappings call it renderDistance
            setNumericOption(options, "viewDistance", 4);   // fallback name
            // Option names may differ — try several candidates for the same effect
            setOptionValue(options, "particlesMode", 0);
            setOptionValue(options, "cloudRenderMode", 0);

            // Try to call options.save() or client.options.write() if available - optional
            tryCallMethod(options, "save");
            tryCallMethod(client, "scheduleStop"); // probably not present; safe noop

            PlexaClient.LOGGER.info("TurboOptimizer: Ultra boost applied.");
        } catch (Throwable t) {
            PlexaClient.LOGGER.warn("TurboOptimizer: failed to apply all optimizations", t);
        }
    }

    // ----------------------
    // Helpers (reflective, tolerant)
    // ----------------------

    private static Object getOptionsObject(MinecraftClient client) {
        // try common field names
        try {
            // MinecraftClient.options is the standard
            Field f = MinecraftClient.class.getDeclaredField("options");
            f.setAccessible(true);
            return f.get(client);
        } catch (NoSuchFieldException | IllegalAccessException ignored) { }
        // fallback: try getOptions() method if present
        try {
            Method m = MinecraftClient.class.getDeclaredMethod("getOptions");
            m.setAccessible(true);
            return m.invoke(client);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) { }
        return null;
    }

    /**
     * Attempts (in order):
     *  - set a public/protected/private field named fieldName on options object
     *  - if the field is a SimpleOption-like object, call setValue(value) on it
     *  - call set<FieldName>(value) setter if present
     *  - set a primitive/boxed numeric field
     */
    private static void setOptionValue(Object options, String fieldName, Object value) {
        if (options == null) return;

        // 1) direct field
        try {
            Field f = findFieldIgnoreCase(options.getClass(), fieldName);
            if (f != null) {
                f.setAccessible(true);
                Class<?> t = f.getType();
                // If field is some option object (SimpleOption), try to call setValue
                Object current = f.get(options);
                if (current != null && callSetValueIfExists(current, value)) {
                    return;
                }
                // else try to set primitive/boxed value
                if (isAssignable(t, value)) {
                    f.set(options, value);
                    return;
                } else {
                    // Attempt conversion for numeric types
                    Number num = toNumber(value);
                    if (num != null) {
                        setNumericFieldValue(f, options, num);
                        return;
                    }
                }
            }
        } catch (Throwable ignored) { }

        // 2) try setter method setXxx
        String setter = "set" + capitalize(fieldName);
        try {
            Method m = findMethodIgnoreCase(options.getClass(), setter, value == null ? new Class<?>[]{} : new Class<?>[]{ value.getClass() });
            if (m != null) {
                m.setAccessible(true);
                if (value == null) {
                    m.invoke(options);
                } else {
                    m.invoke(options, value);
                }
                return;
            }
        } catch (Throwable ignored) { }

        // 3) try to find SimpleOption field by name and call setValue
        try {
            Field f2 = findFieldIgnoreCase(options.getClass(), fieldName + "Option");
            if (f2 != null) {
                f2.setAccessible(true);
                Object opt = f2.get(options);
                if (opt != null && callSetValueIfExists(opt, value)) return;
            }
        } catch (Throwable ignored) { }

        // 4) try to find an option object by same name and call setValue on it
        try {
            Field f3 = findFieldIgnoreCase(options.getClass(), fieldName);
            if (f3 != null) {
                Object opt = f3.get(options);
                if (opt != null && callSetValueIfExists(opt, value)) return;
            }
        } catch (Throwable ignored) { }
    }

    private static boolean callSetValueIfExists(Object optionObj, Object value) {
        if (optionObj == null) return false;
        try {
            // Try setValue(...)
            Method setValue = findMethodIgnoreCase(optionObj.getClass(), "setValue", value == null ? new Class<?>[]{} : new Class<?>[]{ value.getClass() });
            if (setValue != null) {
                setValue.setAccessible(true);
                setValue.invoke(optionObj, value);
                return true;
            }
            // Try set(...) or accept(...)
            Method[] methods = optionObj.getClass().getMethods();
            for (Method m : methods) {
                if (m.getName().equalsIgnoreCase("setValue") || m.getName().equalsIgnoreCase("set") || m.getName().equalsIgnoreCase("accept")) {
                    Class<?>[] params = m.getParameterTypes();
                    if (params.length == 1 && params[0].isAssignableFrom(value == null ? Object.class : value.getClass())) {
                        m.invoke(optionObj, value);
                        return true;
                    }
                }
            }
        } catch (Throwable ignored) { }
        return false;
    }

    private static void setNumericOption(Object options, String fieldName, Number numericValue) {
        // Try field names that commonly hold numeric options
        try {
            Field f = findFieldIgnoreCase(options.getClass(), fieldName);
            if (f != null) {
                f.setAccessible(true);
                setNumericFieldValue(f, options, numericValue);
                return;
            }
        } catch (Throwable ignored) { }

        // fallback: try property named like fieldNameOption and call setValue
        try {
            Field f2 = findFieldIgnoreCase(options.getClass(), fieldName + "Option");
            if (f2 != null) {
                f2.setAccessible(true);
                Object opt = f2.get(options);
                if (opt != null && callSetValueIfExists(opt, numericValue)) return;
            }
        } catch (Throwable ignored) { }
    }

    private static void setNumericFieldValue(Field f, Object instance, Number num) {
        try {
            Class<?> t = f.getType();
            if (t == int.class || t == Integer.class) f.setInt(instance, num.intValue());
            else if (t == float.class || t == Float.class) f.setFloat(instance, num.floatValue());
            else if (t == double.class || t == Double.class) f.setDouble(instance, num.doubleValue());
            else if (t == long.class || t == Long.class) f.setLong(instance, num.longValue());
            else {
                // try set as object
                if (t.isAssignableFrom(num.getClass())) f.set(instance, num);
            }
        } catch (Throwable ignored) { }
    }

    private static boolean isAssignable(Class<?> target, Object value) {
        if (value == null) return !target.isPrimitive();
        return target.isAssignableFrom(value.getClass()) ||
               (target.isPrimitive() && primitiveWrapper(target).isAssignableFrom(value.getClass()));
    }

    private static Class<?> primitiveWrapper(Class<?> primitive) {
        if (primitive == int.class) return Integer.class;
        if (primitive == boolean.class) return Boolean.class;
        if (primitive == long.class) return Long.class;
        if (primitive == float.class) return Float.class;
        if (primitive == double.class) return Double.class;
        if (primitive == byte.class) return Byte.class;
        if (primitive == char.class) return Character.class;
        if (primitive == short.class) return Short.class;
        return Object.class;
    }

    private static Number toNumber(Object v) {
        if (v instanceof Number) return (Number) v;
        try {
            return Integer.parseInt(String.valueOf(v));
        } catch (Exception ignored) { }
        try {
            return Double.parseDouble(String.valueOf(v));
        } catch (Exception ignored) { }
        return null;
    }

    private static Field findFieldIgnoreCase(Class<?> cls, String name) {
        for (Class<?> c = cls; c != null; c = c.getSuperclass()) {
            Field[] fs = c.getDeclaredFields();
            for (Field f : fs) {
                if (f.getName().equalsIgnoreCase(name)) return f;
            }
        }
        return null;
    }

    private static Method findMethodIgnoreCase(Class<?> cls, String name, Class<?>[] paramTypes) {
        // try exact param match
        for (Class<?> c = cls; c != null; c = c.getSuperclass()) {
            Method[] ms = c.getDeclaredMethods();
            for (Method m : ms) {
                if (m.getName().equalsIgnoreCase(name) && parametersMatch(m.getParameterTypes(), paramTypes)) {
                    return m;
                }
            }
        }
        // fallback: try any method with same name and single param assignable
        for (Class<?> c = cls; c != null; c = c.getSuperclass()) {
            Method[] ms = c.getDeclaredMethods();
            for (Method m : ms) {
                if (m.getName().equalsIgnoreCase(name)) return m;
            }
        }
        return null;
    }

    private static boolean parametersMatch(Class<?>[] a, Class<?>[] b) {
        if (a == null) a = new Class<?>[0];
        if (b == null) b = new Class<?>[0];
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (!a[i].isAssignableFrom(b[i])) return false;
        }
        return true;
    }

    private static void tryCallMethod(Object target, String methodName, Object... args) {
        if (target == null) return;
        for (Method m : target.getClass().getMethods()) {
            if (m.getName().equalsIgnoreCase(methodName) && m.getParameterCount() == args.length) {
                try {
                    m.setAccessible(true);
                    m.invoke(target, args);
                    return;
                } catch (IllegalAccessException | InvocationTargetException ignored) { }
            }
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static void optimize(MinecraftClient client) {
        
        throw new UnsupportedOperationException("Unimplemented method 'optimize'");
    }
}
