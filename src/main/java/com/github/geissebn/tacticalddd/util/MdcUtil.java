package com.github.geissebn.tacticalddd.util;

import org.slf4j.MDC;

import java.util.Map;

public class MdcUtil implements AutoCloseable {
    public interface MdcKey {
        String VIN = "vin";
    }

    private final Map<String, String> originalMdc;

    private MdcUtil(String key, String value) {
        this.originalMdc = MDC.getCopyOfContextMap();
        MDC.put(key, value);
    }

    public static void inSubMdc(Runnable action) {
        var originalMdc = MDC.getCopyOfContextMap();
        try {
            action.run();
        } finally {
            MDC.clear();
            MDC.setContextMap(originalMdc);
        }

    }

    public static <E extends Exception> void inSubMdc(String key, Object value, RunnableWithException<E> action) throws E {
        try (final var ignored = new MdcUtil(key, value.toString())) {
            action.run();
        }
    }

    public interface RunnableWithException<E extends Exception> {
        void run() throws E;
    }

    @Override
    public void close() {
        MDC.clear();
        MDC.setContextMap(originalMdc);
    }
}
