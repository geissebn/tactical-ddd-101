package com.github.geissebn.tacticalddd.domain

import com.github.geissebn.tacticalddd.Fixtures
import spock.lang.Specification

import static com.github.geissebn.tacticalddd.domain.EngineState.RUNNING
import static com.github.geissebn.tacticalddd.domain.EngineState.STOPPED
import static com.github.geissebn.tacticalddd.domain.EngineState.values

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
    car.engineState == STOPPED
  }

  def 'starts when stopped'() {
    given:
    def car = Fixtures.car([engineState: STOPPED])

    when:
    car.startEngine()

    then:
    car.engineState == RUNNING
  }

  def 'can start in state #state'() {
    expect:
    Fixtures.car([engineState: state]).canStart() == expected

    where:
    state   | expected
    STOPPED | true
    RUNNING | false
  }

  def 'cannot be started twice'() {
    given:
    def car = Fixtures.car([engineState: RUNNING])

    when:
    car.startEngine()

    then:
    thrown IllegalStateException

  }

  def 'stops in state #state'() {
    given:
    def car = Fixtures.car([engineState: state])

    when:
    car.stopEngine()

    then:
    car.engineState == STOPPED

    where:
    state << values()
  }

  def 'can stop in state #state'() {
    expect:
    Fixtures.car([engineState: state]).canStop() == expected

    where:
    state   | expected
    RUNNING | true
    STOPPED | false
  }

  def 'must not allow null as engine state'() {
    given:
    def car = Fixtures.car()

    when:
    car.engineState = null

    then:
    thrown NullPointerException.class
  }
}
