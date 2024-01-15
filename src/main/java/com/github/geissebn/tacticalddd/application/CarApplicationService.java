package com.github.geissebn.tacticalddd.application;

import com.github.geissebn.tacticalddd.model.Car;
import com.github.geissebn.tacticalddd.model.CarEvent;
import com.github.geissebn.tacticalddd.model.ProductionDate;
import com.github.geissebn.tacticalddd.model.VehicleIdentificationNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static com.github.geissebn.tacticalddd.util.MdcUtil.MdcKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarApplicationService {

    final CarRepository carRepository;
    final CarNotificationService carNotificationService;

    public Car buildNewCar() {
        var vin = new VehicleIdentificationNumber(UUID.randomUUID());
        MDC.put(MdcKey.VIN, vin.getValue().toString());
        var prodDate = new ProductionDate(Instant.now());
        LOG.info("Building a new car");
        var car = Car.build(vin, prodDate);
        carRepository.save(car);
        carNotificationService.notify(vin, CarEvent.CREATED);
        return car;
    }

    public void demolishCar(VehicleIdentificationNumber vin) throws NoSuchCarException {
        LOG.info("Demolishing a car");
        if (!carRepository.delete(vin)) {
            throw new NoSuchCarException(vin);
        }
        carNotificationService.notify(vin, CarEvent.DEMOLISHED);
    }

    public void startCar(VehicleIdentificationNumber vin) throws NoSuchCarException {
        LOG.info("Starting a car");
        var car = carRepository.find(vin).orElseThrow(() -> new NoSuchCarException(vin));
        car.startEngine();
        carRepository.save(car);
        carNotificationService.notify(vin, CarEvent.STARTED);
    }

    public void stopCar(VehicleIdentificationNumber vin) throws NoSuchCarException {
        LOG.info("Stopping a car");
        var car = carRepository.find(vin).orElseThrow(() -> new NoSuchCarException(vin));
        car.stopEngine();
        carRepository.save(car);
        carNotificationService.notify(vin, CarEvent.STOPPED);
    }
}
