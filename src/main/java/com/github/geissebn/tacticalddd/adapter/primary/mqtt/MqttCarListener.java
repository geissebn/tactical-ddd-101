package com.github.geissebn.tacticalddd.adapter.primary.mqtt;

import com.github.geissebn.tacticalddd.application.CarApplicationService;
import com.github.geissebn.tacticalddd.application.NoSuchCarException;
import com.github.geissebn.tacticalddd.model.VehicleIdentificationNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.github.geissebn.tacticalddd.util.MdcUtil.MdcKey;
import static com.github.geissebn.tacticalddd.util.MdcUtil.inSubMdc;


@Service
@RequiredArgsConstructor
@Slf4j
public class MqttCarListener {

    final IMqttClient mqttClient;
    final CarApplicationService applicationService;

    @Value("${mqtt.topic.inbound}")
    private String topic;

    @PostConstruct
    void connectToTopic() throws MqttException {
        mqttClient.subscribe(topic);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                MqttCarListener.this.handleMessage(topic, message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    private void handleMessage(String topic, MqttMessage message) {
        inSubMdc(() -> {
            MDC.put("mqtt.topic", topic);
            LOG.info("Handling incoming MQTT message");
            var splitted = topic.split("/");
            var vinRaw = splitted[splitted.length - 1];
            MDC.put(MdcKey.VIN, vinRaw);

            try {
                var vin = new VehicleIdentificationNumber(UUID.fromString(vinRaw));
                var action = new String(message.getPayload());
                switch (action) {
                    case "start":
                        applicationService.startCar(vin);
                        break;
                    case "stop":
                        applicationService.stopCar(vin);
                        break;
                    case "demolish":
                        applicationService.demolishCar(vin);
                        break;
                    default:
                        LOG.warn("unknown action {}", action);
                }
            } catch (NoSuchCarException e) {
                LOG.warn("unknown car", e);
            }
        });
    }
}
