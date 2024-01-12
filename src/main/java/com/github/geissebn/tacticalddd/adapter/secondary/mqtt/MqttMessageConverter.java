package com.github.geissebn.tacticalddd.adapter.secondary.mqtt;

import com.github.geissebn.tacticalddd.model.CarEvent;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
class MqttMessageConverter {
    public MqttMessage toMqttMessage(CarEvent carEvent) {
        var message = switch (carEvent) {
            case CREATED -> "created";
            case DEMOLISHED -> "demolished";
            case STARTED -> "started";
            case STOPPED -> "stopped";
        };
        return new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
    }
}
