package com.github.geissebn.tacticalddd.adapter.primary.http;

import com.github.geissebn.tacticalddd.application.CarApplicationService;
import com.github.geissebn.tacticalddd.application.CarRepository;
import com.github.geissebn.tacticalddd.application.NoSuchCarException;
import com.github.geissebn.tacticalddd.model.VehicleIdentificationNumber;
import com.github.geissebn.tacticalddd.util.MdcUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

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

    @GetMapping("build")
    public String build() {
        inSubMdc(carApplicationService::buildNewCar);
        return "redirect:/";
    }

    @GetMapping("action")
    public String action(@RequestParam String action, @RequestParam("vin") UUID vinRaw) throws NoSuchCarException {
        inSubMdc(MdcUtil.MdcKey.VIN, vinRaw, () -> {
            var vin = new VehicleIdentificationNumber(vinRaw);
            switch (action) {
                case "start":
                    carApplicationService.startCar(vin);
                    break;
                case "stop":
                    carApplicationService.stopCar(vin);
                    break;
                case "demolish":
                    carApplicationService.demolishCar(vin);
                    break;
            }
        });
        return "redirect:/";
    }

    @ExceptionHandler({IllegalStateException.class, NoSuchCarException.class})
    public String handleIllegalState() {
        return "redirect:/";
    }
}
