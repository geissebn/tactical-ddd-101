package com.github.geissebn.tacticalddd

import com.github.geissebn.tacticalddd.model.Car
import com.github.geissebn.tacticalddd.model.EngineState
import com.github.geissebn.tacticalddd.model.ProductionDate
import com.github.geissebn.tacticalddd.model.VehicleIdentificationNumber

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
