package com.github.geissebn.tacticalddd.adapter.secondary.mqtt;


import com.github.geissebn.tacticalddd.application.CarNotificationService;
import com.github.geissebn.tacticalddd.model.CarEvent;
import com.github.geissebn.tacticalddd.model.VehicleIdentificationNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MqttCarNotificationService implements CarNotificationService {
    private final IMqttClient mqttClient;
    private final MqttMessageConverter mqttMessageConverter;
    @Value("${mqtt.topic.outbound}")
    private String topicPattern;

    @Override
    public void notify(VehicleIdentificationNumber vin, CarEvent carEvent) {
        try {
            MDC.put("event", carEvent.name());
            LOG.info("Notifying car event to MQTT");
            mqttClient.publish(
                    topicPattern.replace("$vin", vin.getValue().toString()),
                    mqttMessageConverter.toMqttMessage(carEvent));
        } catch (MqttException e) {
            LOG.error("Cannot send MQTT notification", e);
        }
    }
}
