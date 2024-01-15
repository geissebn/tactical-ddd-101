package com.github.geissebn.tacticalddd

import com.github.geissebn.tacticalddd.domain.Car
import com.github.geissebn.tacticalddd.domain.EngineState
import com.github.geissebn.tacticalddd.domain.ProductionDate
import com.github.geissebn.tacticalddd.domain.VehicleIdentificationNumber

import java.time.Instant

class Fixtures {

  static def vin() {
    new VehicleIdentificationNumber(UUID.randomUUID())
  }

  static def car(data = [:]) {
    new Car(
            data.vin ?: vin() as VehicleIdentificationNumber,
            data.productionData ?: productionDate() as ProductionDate,
            data.engineState ?: EngineState.STOPPED as EngineState
    )
  }

  static def productionDate() {
    new ProductionDate(Instant.now())
  }
}
