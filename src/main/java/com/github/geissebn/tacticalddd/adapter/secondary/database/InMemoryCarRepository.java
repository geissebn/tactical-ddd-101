package com.github.geissebn.tacticalddd.adapter.secondary.database;

import com.github.geissebn.tacticalddd.application.CarRepository;
import com.github.geissebn.tacticalddd.model.Car;
import com.github.geissebn.tacticalddd.model.VehicleIdentificationNumber;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class InMemoryCarRepository implements CarRepository {
    private final Map<VehicleIdentificationNumber, Car> cars = new HashMap<>();

    @Override
    public void save(Car car) {
        cars.put(car.getVin(), car);
    }

    @Override
    public boolean delete(VehicleIdentificationNumber vin) {
        var prev = cars.remove(vin);
        return prev != null;
    }

    @Override
    public Optional<Car> find(VehicleIdentificationNumber vin) {
        return Optional.ofNullable(cars.get(vin));
    }

    @Override
    public Stream<Car> getAllCars() {
        return cars.values().stream();
    }
}
