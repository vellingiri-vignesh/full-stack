package com.divineaura.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.region}")
    private String region ;

    @Value("${aws.profile}")
    private String profile;

    @Value("${aws.s3.mock}")
    private boolean mock;

    @Bean
    public S3Client s3Client() {
        if (mock) {
            return new FakeS3();
        }
        DefaultCredentialsProvider credentialsProvider =
            DefaultCredentialsProvider.builder().profileName(profile).build();
        return S3Client.builder()
            .credentialsProvider(credentialsProvider)
            .region(Region.of(region))
            .build();
    }
}
