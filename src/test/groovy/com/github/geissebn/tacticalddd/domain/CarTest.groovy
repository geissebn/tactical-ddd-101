package com.github.geissebn.tacticalddd.domain

import com.github.geissebn.tacticalddd.Fixtures
import spock.lang.Specification

class CarTest extends Specification {

  def 'can be created'() {
    given:
    def vin = Fixtures.vin()
    def productionDate = Fixtures.productionDate()

    when:
    def car = Car.build(vin, productionDate)

    then:
    car.vin == vin
    car.productionDate == productionDate
    car.engineState == EngineState.STOPPED
  }

  def 'can be started'() {
    given:
    def car = Fixtures.car([engineState: EngineState.STOPPED])

    when:
    car.startEngine()

    then:
    car.engineState == EngineState.RUNNING
  }

  def 'cannot be started twice'() {
    given:
    def car = Fixtures.car([engineState: EngineState.RUNNING])

    when:
    car.startEngine()

    then:
    thrown IllegalStateException

  }

  def 'can be stopped in state #state'() {
    given:
    def car = Fixtures.car([engineState: state])

    when:
    car.stopEngine()

    then:
    car.engineState == EngineState.STOPPED

    where:
    state << EngineState.values()
  }
}
