package com.github.geissebn.tacticalddd.application;

import com.github.geissebn.tacticalddd.model.CarEvent;
import com.github.geissebn.tacticalddd.model.VehicleIdentificationNumber;

public interface CarNotificationService {

    void notify(VehicleIdentificationNumber vin, CarEvent carEvent);
}
