package com.github.geissebn.tacticalddd.application;

import com.github.geissebn.tacticalddd.domain.CarEvent;
import com.github.geissebn.tacticalddd.domain.VehicleIdentificationNumber;

public interface CarNotificationService {

    void notify(VehicleIdentificationNumber vin, CarEvent carEvent);
}
