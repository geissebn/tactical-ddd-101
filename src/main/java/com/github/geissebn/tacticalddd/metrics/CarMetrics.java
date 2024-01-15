package com.github.geissebn.tacticalddd.metrics;


import com.github.geissebn.tacticalddd.domain.CarEvent;

public interface CarMetrics {

    void recordCarEvent(CarEvent event);
}
