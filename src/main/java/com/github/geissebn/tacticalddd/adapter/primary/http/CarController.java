package com.github.geissebn.tacticalddd.adapter.primary.http;

import com.github.geissebn.tacticalddd.application.CarApplicationService;
import com.github.geissebn.tacticalddd.application.CarRepository;
import com.github.geissebn.tacticalddd.domain.VehicleIdentificationNumber;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

import static com.github.geissebn.tacticalddd.util.MdcUtil.MdcKey;
import static com.github.geissebn.tacticalddd.util.MdcUtil.inSubMdc;

@Service
@Controller
@RequiredArgsConstructor
public class CarController {

    final CarRepository carRepository;
    final CarApplicationService carApplicationService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cars", carRepository.getAllCars());
        return "index";
    }

    @HxRequest
    @PostMapping("/car/new")
    public String build(Model model) {
        inSubMdc(() -> {
            var car = carApplicationService.buildNewCar();
            model.addAttribute("car", car);
        });
        return "car :: car-table-row";
    }
    @HxRequest
    @PostMapping("/car/{vinRaw}/{action}")
    public String hxAction(@PathVariable String action, @PathVariable UUID vinRaw, Model model) {
        inSubMdc(() -> {
            MDC.put(MdcKey.VIN, vinRaw.toString());
            var vin = new VehicleIdentificationNumber(vinRaw);
            var car = switch (action) {
                case "start":
                    yield carApplicationService.startCar(vin);
                case "stop":
                    yield carApplicationService.stopCar(vin);
                case "demolish":
                    carApplicationService.demolishCar(vin);
                    yield null;
                default:
                    throw new IllegalStateException("Unexpected value: " + action);
            };
            model.addAttribute("car", car);
        });
        return "car :: car-table-row";
    }
}
