package com.github.geissebn.tacticalddd.application

import com.github.geissebn.tacticalddd.Fixtures
import com.github.geissebn.tacticalddd.domain.Car
import com.github.geissebn.tacticalddd.domain.CarEvent
import com.github.geissebn.tacticalddd.domain.EngineState
import com.github.geissebn.tacticalddd.metrics.CarMetrics
import spock.lang.Specification
import spock.lang.Subject

class CarApplicationServiceTest extends Specification {
  CarRepository repo = Mock()
  CarNotificationService notificationService = Mock()
  CarMetrics metrics = Mock()

  @Subject
  CarApplicationService applicationService = new CarApplicationService(repo, notificationService, metrics)


  def 'should create a new car'() {
    given:
    def savedCar = null

    when:
    def returnedCar = applicationService.buildNewCar()

    then:
    1 * repo.save(_) >> { args ->
      savedCar = args[0] as Car
    }
    1 * notificationService.notify(_, CarEvent.CREATED)
    returnedCar == savedCar
  }

  def 'should start a car'() {
    given:
    def car = Fixtures.car([engineState: EngineState.STOPPED])
    repo.find(car.vin) >> Optional.of(car)

    when:
    def returnedCar = applicationService.startCar(car.vin)

    then:
    1 * notificationService.notify(car.vin, CarEvent.STARTED)
    1 * repo.save(car)
    1 * metrics.recordCarEvent(CarEvent.STARTED)
    returnedCar.engineState == EngineState.RUNNING
    returnedCar == car
  }

  def 'should stop a car'() {
    given:
    def car = Fixtures.car([engineState: EngineState.RUNNING])
    repo.find(car.vin) >> Optional.of(car)

    when:
    def returnedCar = applicationService.stopCar(car.vin)

    then:
    1 * notificationService.notify(car.vin, CarEvent.STOPPED)
    1 * repo.save(car)
    1 * metrics.recordCarEvent(CarEvent.STOPPED)
    returnedCar.engineState == EngineState.STOPPED
    returnedCar == car
  }

  def 'should demolish a car'() {
    given:
    def vin = Fixtures.vin()

    when:
    applicationService.demolishCar(vin)

    then:
    1 * repo.delete(vin) >> true
    1 * notificationService.notify(vin, CarEvent.DEMOLISHED)
    1 * metrics.recordCarEvent(CarEvent.DEMOLISHED)
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
