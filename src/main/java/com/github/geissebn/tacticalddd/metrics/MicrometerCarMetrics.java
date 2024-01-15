package com.github.geissebn.tacticalddd.metrics;

import com.github.geissebn.tacticalddd.model.CarEvent;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MicrometerCarMetrics implements CarMetrics {

    private final MeterRegistry meterRegistry;

    @Override
    public void recordCarEvent(CarEvent event) {
        meterRegistry.counter("com.acme.cars", "event", event.name().toLowerCase()).increment();
    }
}
