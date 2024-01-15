package com.github.geissebn.tacticalddd.domain;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Car {

    @NonNull
    private final VehicleIdentificationNumber vin;
    @NonNull
    private final ProductionDate productionDate;
    @NonNull
    private EngineState engineState;

    public static Car build(VehicleIdentificationNumber vin, ProductionDate productionDate) {
        return new Car(vin, productionDate, EngineState.STOPPED);
    }

    public void startEngine() {
        LOG.info("Starting the engine");
        if (engineState == EngineState.RUNNING) {
            throw new IllegalStateException("Engine is already running");
        }
        LOG.info("Engine is up and running");
        engineState = EngineState.RUNNING;
    }

    public void stopEngine() {
        LOG.info("Engine is stopped now");
        engineState = EngineState.STOPPED;
    }
}
