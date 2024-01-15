package com.github.geissebn.tacticalddd.adapter.secondary.database

import com.github.geissebn.tacticalddd.Fixtures
import com.github.geissebn.tacticalddd.domain.EngineState
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant


class CarJpaConverterTest extends Specification {

  @Subject
  CarJpaConverter converter =  new CarJpaConverter()

  def 'should convert to JPA (engineState: #engineState)'() {
    given:
    def car = Fixtures.car([engineState: engineState])

    when:
    def jpa = converter.toJpa(car)

    then:
    jpa.vin == car.vin.value.toString()
    jpa.engineState == car.engineState.name()
    jpa.productionDate == car.productionDate.value

    where:
    engineState << EngineState.values()
  }

  def 'should convert null value to JPA'() {
    expect:
    converter.toJpa(null) == null
  }

  def 'should convert from JPA'() {
    given:
    def jpa = new CarJpa(UUID.randomUUID().toString(), Instant.now(), engineState.name())

    when:
    def car = converter.fromJpa(jpa)

    then:
    car.vin.value == UUID.fromString(jpa.vin)
    car.productionDate.value == jpa.productionDate
    car.engineState == EngineState.valueOf(jpa.engineState)

    where:
    engineState << EngineState.values()
  }

  def 'should convert null from JPA'() {
    expect:
    converter.fromJpa(null) == null
  }
}
