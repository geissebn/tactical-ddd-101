package com.github.geissebn.tacticalddd.model;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;

@Data
@Log
public class Car {

    @NonNull private final VehicleIdentificationNumber vin;
    @NonNull private final ProductionDate productionDate;
    @NonNull private EngineState engineState;

    public static Car build(VehicleIdentificationNumber vin, ProductionDate productionDate) {
        return new Car(vin, productionDate, EngineState.STOPPED);
    }

    public void startEngine() throws EngineFailure {
        log.info("Starting the engine");
        if (engineState == EngineState.RUNNING) {
            throw new IllegalStateException("Engine is already running");
        }
        if (Math.random() < .01) {
            engineState = EngineState.FAILURE;
            throw new EngineFailure("Oups, the engine is damaged");
        }
        log.info("Engine is up and running");
        engineState = EngineState.RUNNING;
    }

    public void stopEngine() {
        engineState = EngineState.STOPPED;
    }
}
