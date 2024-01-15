package com.github.geissebn.tacticalddd.domain;

import lombok.NonNull;
import lombok.Value;

import java.time.Instant;

@Value
public class ProductionDate {
    @NonNull Instant value;
}
