package com.github.geissebn.tacticalddd.config;


import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

@Configuration
public class MqttConfiguration {

    @Value("${mqtt.broker.url}")
    private String mqttBrokerUrl;

    @Value("${mqtt.client.id}")
    private String mqttClientId;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setUserName("username");
//        options.setPassword("password".toCharArray());
//        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    IMqttClient mqttClient() throws MqttException {
        var client = mqttClientFactory().getClientInstance(mqttBrokerUrl, mqttClientId);
        client.connect();
        return client;
    }
}
