package com.github.geissebn.tacticalddd.adapter.secondary.mqtt


import com.github.geissebn.tacticalddd.domain.CarEvent
import spock.lang.Specification
import spock.lang.Subject

class MqttMessageConverterTest extends Specification {

  @Subject
  MqttMessageConverter converter = new MqttMessageConverter()


  def 'shall convert a Car Event of type #carEvent to status #expectedStatus'() {
    when:
    def message = converter.toMqttMessage(carEvent as CarEvent)

    then:
    message.payload == expectedStatus.bytes

    where:
    carEvent            | expectedStatus
    CarEvent.CREATED    | 'created'
    CarEvent.STARTED    | 'started'
    CarEvent.STOPPED    | 'stopped'
    CarEvent.DEMOLISHED | 'demolished'
  }
}
