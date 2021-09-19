package br.com.selat.consumer.kinesis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.common.InitialPositionInStream;
import software.amazon.kinesis.common.InitialPositionInStreamExtended;
import software.amazon.kinesis.coordinator.Scheduler;
import software.amazon.kinesis.retrieval.RetrievalConfig;
import software.amazon.kinesis.retrieval.polling.PollingConfig;
import java.util.UUID;

@Service
public class KinesisScheduler {

    @Autowired
    public KinesisScheduler(
            KinesisAsyncClient kinesisAsyncClient,
            DynamoDbAsyncClient dynamoDbAsyncClient,
            CloudWatchAsyncClient cloudWatchAsyncClient,
            KinesisProcessorFactory kinesisProcessorFactory){

        String eventStreamName = "test";
        String eventApplicationName = "poc_consumer";

        ConfigsBuilder configsBuilder = new ConfigsBuilder(
                eventStreamName,
                eventApplicationName,
                kinesisAsyncClient,
                dynamoDbAsyncClient,
                cloudWatchAsyncClient,
                UUID.randomUUID().toString(),
                kinesisProcessorFactory);

        RetrievalConfig retrievalConfig = configsBuilder.retrievalConfig().retrievalSpecificConfig(new PollingConfig(eventStreamName, kinesisAsyncClient));
        retrievalConfig.initialPositionInStreamExtended(InitialPositionInStreamExtended.newInitialPosition(InitialPositionInStream.TRIM_HORIZON));

        Scheduler scheduler = new Scheduler(
                configsBuilder.checkpointConfig(),
                configsBuilder.coordinatorConfig(),
                configsBuilder.leaseManagementConfig(),
                configsBuilder.lifecycleConfig(),
                configsBuilder.metricsConfig(),
                configsBuilder.processorConfig(),
                retrievalConfig);

        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.setDaemon(true);
        schedulerThread.start();
    }

}
