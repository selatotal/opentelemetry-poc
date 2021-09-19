package br.com.selat.consumer.kinesis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

@Service
public class KinesisProcessorFactory implements ShardRecordProcessorFactory {

    private final KinesisProcessor kinesisProcessor;

    @Autowired
    public KinesisProcessorFactory(KinesisProcessor kinesisProcessor) {
        this.kinesisProcessor = kinesisProcessor;
    }

    @Override
    public ShardRecordProcessor shardRecordProcessor() {
        return kinesisProcessor;
    }
}
