package com.github.geissebn.tacticalddd.application;

import com.github.geissebn.tacticalddd.domain.Car;
import com.github.geissebn.tacticalddd.domain.CarEvent;
import com.github.geissebn.tacticalddd.domain.ProductionDate;
import com.github.geissebn.tacticalddd.domain.VehicleIdentificationNumber;
import com.github.geissebn.tacticalddd.metrics.CarMetrics;
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
    final CarMetrics metrics;

    public Car buildNewCar() {
        var vin = new VehicleIdentificationNumber(UUID.randomUUID());
        MDC.put(MdcKey.VIN, vin.getValue().toString());
        var prodDate = new ProductionDate(Instant.now());
        LOG.info("Building a new car");
        var car = Car.build(vin, prodDate);
        carRepository.save(car);
        carNotificationService.notify(vin, CarEvent.CREATED);
        metrics.recordCarEvent(CarEvent.CREATED);
        return car;
    }

    public void demolishCar(VehicleIdentificationNumber vin) throws NoSuchCarException {
        LOG.info("Demolishing a car");
        if (!carRepository.delete(vin)) {
            throw new NoSuchCarException(vin);
        }
        carNotificationService.notify(vin, CarEvent.DEMOLISHED);
        metrics.recordCarEvent(CarEvent.DEMOLISHED);
    }

    public Car startCar(VehicleIdentificationNumber vin) throws NoSuchCarException {
        LOG.info("Starting a car");
        var car = carRepository.find(vin).orElseThrow(() -> new NoSuchCarException(vin));
        car.startEngine();
        carRepository.save(car);
        carNotificationService.notify(vin, CarEvent.STARTED);
        metrics.recordCarEvent(CarEvent.STARTED);
        return car;
    }

    public Car stopCar(VehicleIdentificationNumber vin) throws NoSuchCarException {
        LOG.info("Stopping a car");
        var car = carRepository.find(vin).orElseThrow(() -> new NoSuchCarException(vin));
        car.stopEngine();
        carRepository.save(car);
        carNotificationService.notify(vin, CarEvent.STOPPED);
        metrics.recordCarEvent(CarEvent.STOPPED);
        return car;
    }
}
