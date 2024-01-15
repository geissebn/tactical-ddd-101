package com.github.geissebn.tacticalddd.adapter.secondary.database;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name="car")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarJpa {
    @Id
    String vin;
    Instant productionDate;
    String engineState;
}
