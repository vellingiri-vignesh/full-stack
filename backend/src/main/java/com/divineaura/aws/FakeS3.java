package com.divineaura.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.InvalidObjectStateException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class FakeS3 implements S3Client {
    private static final String path = System.getProperty("user.home") + "/.fullstak/s3";

    @Override
    public String serviceName() {
        return "fake";
    }

    @Override
    public void close() {

    }

    @Override
    public PutObjectResponse putObject(PutObjectRequest putObjectRequest, RequestBody requestBody)
        throws AwsServiceException, SdkClientException, S3Exception {
        InputStream inputStream = requestBody.contentStreamProvider().newStream();
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            FileUtils.writeByteArrayToFile(
                new File(buildObjectFullPath(putObjectRequest.bucket(), putObjectRequest.key())), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return PutObjectResponse.builder().build();
    }

    @Override
    public ResponseInputStream<GetObjectResponse> getObject(GetObjectRequest getObjectRequest)
        throws NoSuchKeyException, InvalidObjectStateException, AwsServiceException, SdkClientException, S3Exception {
        try {
            FileInputStream fileInputStream =
                new FileInputStream(buildObjectFullPath(getObjectRequest.bucket(), getObjectRequest.key()));
            return new ResponseInputStream<>(GetObjectResponse.builder().build(), fileInputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private String buildObjectFullPath(String bucketName, String key) {
        return path + "/" + bucketName + "/" + key;
    }
}
