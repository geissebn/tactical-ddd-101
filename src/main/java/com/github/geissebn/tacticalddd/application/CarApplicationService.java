package com.github.geissebn.tacticalddd.application;

import com.github.geissebn.tacticalddd.model.Car;
import com.github.geissebn.tacticalddd.model.CarEvent;
import com.github.geissebn.tacticalddd.model.ProductionDate;
import com.github.geissebn.tacticalddd.model.VehicleIdentificationNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarApplicationService {

    final CarRepository carRepository;
    final CarNotificationService carNotificationService;

    public Car buildNewCar() {
        var vin = new VehicleIdentificationNumber(UUID.randomUUID());
        var prodDate = new ProductionDate(Instant.now());
        var car = Car.build(vin, prodDate);
        carRepository.save(car);
        carNotificationService.notify(vin, CarEvent.CREATED);
        return car;
    }

    public void demolishCar(VehicleIdentificationNumber vin) throws NoSuchCarException {
        if (!carRepository.delete(vin)) {
            throw new NoSuchCarException(vin);
        }
        carNotificationService.notify(vin, CarEvent.DEMOLISHED);
    }

    public void startCar(VehicleIdentificationNumber vin) throws NoSuchCarException {
        var car = carRepository.find(vin).orElseThrow(() -> new NoSuchCarException(vin));
        car.startEngine();
        carRepository.save(car);
        carNotificationService.notify(vin, CarEvent.STARTED);
    }

    public void stopCar(VehicleIdentificationNumber vin) throws NoSuchCarException {
        var car = carRepository.find(vin).orElseThrow(() -> new NoSuchCarException(vin));
        car.stopEngine();
        carRepository.save(car);
        carNotificationService.notify(vin, CarEvent.STOPPED);
    }
}
