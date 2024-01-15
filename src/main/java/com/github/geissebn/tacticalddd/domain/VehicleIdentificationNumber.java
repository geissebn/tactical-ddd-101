package com.github.geissebn.tacticalddd.domain;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class VehicleIdentificationNumber {

    @NonNull UUID value;

}
