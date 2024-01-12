package com.github.geissebn.tacticalddd.application

import com.github.geissebn.tacticalddd.Fixtures
import com.github.geissebn.tacticalddd.model.CarEvent
import com.github.geissebn.tacticalddd.model.EngineState
import spock.lang.Specification
import spock.lang.Subject

class CarApplicationServiceTest extends Specification {
  CarRepository repo = Mock()
  CarNotificationService notificationService = Mock()

  @Subject
  CarApplicationService applicationService = new CarApplicationService(repo, notificationService)


  def 'should create a new car'() {
    when:
    applicationService.buildNewCar()

    then:
    1 * repo.save(_)
    1 * notificationService.notify(_, CarEvent.CREATED)
  }

  def 'should start a car'() {
    given:
    def car = Fixtures.car([engineState: EngineState.STOPPED])
    repo.find(car.vin) >> Optional.of(car)

    when:
    applicationService.startCar(car.vin)

    then:
    1 * notificationService.notify(car.vin, CarEvent.STARTED)
    1 * repo.save(car)
    car.engineState == EngineState.RUNNING
  }

  def 'should stop a car'() {
    given:
    def car = Fixtures.car([engineState: EngineState.RUNNING])
    repo.find(car.vin) >> Optional.of(car)

    when:
    applicationService.stopCar(car.vin)

    then:
    1 * notificationService.notify(car.vin, CarEvent.STOPPED)
    1 * repo.save(car)
    car.engineState == EngineState.STOPPED
  }

  def 'should demolish a car'() {
    given:
    def vin = Fixtures.vin()

    when:
    applicationService.demolishCar(vin)

    then:
    1 * repo.delete(vin) >> true
    1 * notificationService.notify(vin, CarEvent.DEMOLISHED)
  }

  def 'should throw error when starting an unknown car'() {
    given:
    def vin = Fixtures.vin()
    repo.find(vin) >> Optional.empty()

    when:
    applicationService.startCar(vin)

    then:
    thrown NoSuchCarException
  }

  def 'should throw error when stopping an unknown car'() {
    given:
    def vin = Fixtures.vin()
    repo.find(vin) >> Optional.empty()

    when:
    applicationService.stopCar(vin)

    then:
    thrown NoSuchCarException
  }

  def 'should throw error when demolishing an unknown car'() {
    given:
    def vin = Fixtures.vin()

    when:
    applicationService.demolishCar(vin)

    then:
    1 * repo.delete(vin) >> false
    thrown NoSuchCarException
  }
}
