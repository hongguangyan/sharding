package com.simple.sharding.dataSource.Context;

/**
 * 上下文Holder
 */
@SuppressWarnings("unchecked")
public class ContextHolder<T> {

    private static final ThreadLocal contextHolder = new ThreadLocal();

    public static <T> void setContext(T context) {
        contextHolder.set(context);
    }

    public static <T> T getContext() {
        return (T) contextHolder.get();
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}
