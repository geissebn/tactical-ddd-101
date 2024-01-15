package com.github.geissebn.tacticalddd.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

@NoArgsConstructor(access = AccessLevel.NONE)
public class MdcUtil {
    public interface MdcKey {
        String VIN = "vin";
    }

    public static void inSubMdc(Runnable action) {
        var originalMdc = MDC.getCopyOfContextMap();
        try {
            action.run();
        } finally {
            MDC.setContextMap(originalMdc);
        }
    }
}
