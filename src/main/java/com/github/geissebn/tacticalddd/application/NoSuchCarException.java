package com.github.geissebn.tacticalddd.application;

import com.github.geissebn.tacticalddd.domain.VehicleIdentificationNumber;

public class NoSuchCarException extends Exception {
    public NoSuchCarException(VehicleIdentificationNumber vin) {
        super(String.format("Car with VIN %s is unknown", vin.getValue()));
    }
}
