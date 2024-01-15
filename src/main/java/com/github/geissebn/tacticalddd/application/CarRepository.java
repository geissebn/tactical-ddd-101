package com.github.geissebn.tacticalddd.application;

import com.github.geissebn.tacticalddd.domain.Car;
import com.github.geissebn.tacticalddd.domain.VehicleIdentificationNumber;

import java.util.Optional;
import java.util.stream.Stream;

public interface CarRepository {
    void save(Car car);

    boolean delete(VehicleIdentificationNumber vin);

    Optional<Car> find(VehicleIdentificationNumber vin);

    Stream<Car> getAllCars();
}
