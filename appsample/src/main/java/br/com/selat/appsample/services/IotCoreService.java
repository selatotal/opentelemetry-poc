package br.com.selat.appsample.services;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import io.opentelemetry.extension.annotations.WithSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IotCoreService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IotCoreService.class);
    private final AWSIotMqttClient awsIotMqttConfig;

    @Autowired
    public IotCoreService(AWSIotMqttClient awsIotMqttConfig) {
        this.awsIotMqttConfig = awsIotMqttConfig;
    }

    @WithSpan
    public void health(){
        LOGGER.info("Starting iot core health");
        try {
            awsIotMqttConfig.publish("test", "test");
        } catch (AWSIotException e) {
            LOGGER.error("Error publishing message", e);
        }
    }
}
