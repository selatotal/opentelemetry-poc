package br.com.selat.appsample.config;

import software.amazon.awssdk.regions.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

import java.util.Optional;

@Configuration
public class KinesisConfig {

    private static final Region AWS_REGION = Region.of(Optional.ofNullable(System.getenv("AWS_REGION")).orElse("us-east-1"));

    @Bean
    public KinesisAsyncClient kinesisAsyncClient(){
        return KinesisAsyncClient
                .builder()
                .region(AWS_REGION)
                .build();
    }
}
