package com.github.geissebn.tacticalddd.adapter.secondary.database;

import com.github.geissebn.tacticalddd.application.CarRepository;
import com.github.geissebn.tacticalddd.model.Car;
import com.github.geissebn.tacticalddd.model.VehicleIdentificationNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class DatabaseCarRepository implements CarRepository {

final CarJpaRepository repository;
final CarJpaConverter jpaConverter;

    @Override
    public void save(Car car) {
        repository.save(jpaConverter.toJpa(car));
    }

    @Override
    public boolean delete(VehicleIdentificationNumber vin) {
        final var vinAsString = vin.getValue().toString();
        final var carExists = repository.existsById(vinAsString);
        if (carExists) {
            repository.deleteById(vinAsString);
        }
        return carExists;
    }

    @Override
    public Optional<Car> find(VehicleIdentificationNumber vin) {
        return repository.findById(vin.getValue().toString())
                .map(jpaConverter::fromJpa);
    }

    @Override
    public Stream<Car> getAllCars() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(jpaConverter::fromJpa);
    }
}
