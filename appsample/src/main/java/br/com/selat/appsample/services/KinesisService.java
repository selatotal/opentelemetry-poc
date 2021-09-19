package br.com.selat.appsample.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class KinesisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KinesisService.class);

    private final KinesisAsyncClient kinesisClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public KinesisService(KinesisAsyncClient kinesisClient) {
        this.kinesisClient = kinesisClient;
    }

    public String publishMessage(String streamName, String partitionKey, Map<String, Object> map){
        try {
            String content = mapper.writeValueAsString(map);
            PutRecordRequest request = PutRecordRequest
                    .builder()
                    .streamName(streamName)
                    .partitionKey(partitionKey)
                    .data(SdkBytes.fromString(content, Charset.defaultCharset()))
                    .build();
            LOGGER.info("Publishing message {}", content);
            PutRecordResponse response = kinesisClient.putRecord(request).get();
            return response.sequenceNumber();
        } catch (JsonProcessingException e) {
            LOGGER.error("Error creating json message", e);
        } catch (InterruptedException|ExecutionException e) {
            LOGGER.error("Error sending message to kinesis", e);
        }
        return "ERROR";
    }
}
