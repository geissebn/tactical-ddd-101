package com.github.geissebn.tacticalddd.adapter.secondary.database;

import com.github.geissebn.tacticalddd.domain.Car;
import com.github.geissebn.tacticalddd.domain.EngineState;
import com.github.geissebn.tacticalddd.domain.ProductionDate;
import com.github.geissebn.tacticalddd.domain.VehicleIdentificationNumber;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class CarJpaConverter {

    public Car fromJpa(CarJpa jpa) {
        if (jpa == null) {
            return null;
        }
        return new Car(
                new VehicleIdentificationNumber(UUID.fromString(jpa.getVin())),
                new ProductionDate(jpa.getProductionDate()),
                EngineState.valueOf(jpa.getEngineState().toUpperCase())
        );
    }

    public CarJpa toJpa(Car car) {
        if (car == null) {
            return null;
        }
        return new CarJpa(
                car.getVin().getValue().toString(),
                car.getProductionDate().getValue(),
                car.getEngineState().name()
        );
    }
}
