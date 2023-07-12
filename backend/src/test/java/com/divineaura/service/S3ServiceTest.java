package com.divineaura.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;
    private S3Service underTest;

    @BeforeEach
    void setUp() {
        underTest = new S3Service(s3Client);
    }

    @Test
    void canPutObject() throws IOException {
        //Given
        String bucket = "customer";
        String key = "foo";
        byte[] data = "hello world".getBytes();

        //When
        underTest.putObject(bucket, key, data);

        //Then
        ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor =
            ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> requestBodyArgumentCaptor = ArgumentCaptor.forClass(RequestBody.class);
        verify(s3Client).putObject(putObjectRequestArgumentCaptor.capture(), requestBodyArgumentCaptor.capture());

        PutObjectRequest putObjectRequestArgumentCaptorValue = putObjectRequestArgumentCaptor.getValue();
        assertThat(putObjectRequestArgumentCaptorValue.bucket()).isEqualTo(bucket);
        assertThat(putObjectRequestArgumentCaptorValue.key()).isEqualTo(key);

        RequestBody requestBodyArgumentCaptorValue = requestBodyArgumentCaptor.getValue();
        assertThat(requestBodyArgumentCaptorValue.contentStreamProvider().newStream().readAllBytes()).isEqualTo(
            RequestBody.fromBytes(data).contentStreamProvider().newStream().readAllBytes());

    }

    @Test
    void canGetObject() throws IOException {
        //Given
        String bucket = "customer";
        String key = "foo";
        String data = "hello world";
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

//        Used mockito-inline to mock/spy final class
//        ResponseInputStream<GetObjectResponse> expected = new ResponseInputStream<>(GetObjectResponse.builder().build(),
//            new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));

        ResponseInputStream<GetObjectResponse> res = mock(ResponseInputStream.class);
        when(res.readAllBytes()).thenReturn(data.getBytes());
        when(s3Client.getObject(request)).thenReturn(res);

        //When
        byte[] actual = underTest.getObject(bucket, key);
        System.out.println( new String(data.getBytes(), StandardCharsets.UTF_8));

        //Then
        assertThat(actual).isEqualTo(data.getBytes());
    }

    @Test
    void willThrowWhenGetObject() throws IOException {
        //Given
        String bucket = "customer";
        String key = "foo";
        String data = "hello world";
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        ResponseInputStream<GetObjectResponse> res = mock(ResponseInputStream.class);
        when(res.readAllBytes()).thenThrow(new IOException("Cannot read bytes."));
        when(s3Client.getObject(request)).thenReturn(res);

        //When
        assertThatThrownBy(() -> underTest.getObject(bucket, key))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cannot read bytes.")
            .hasRootCauseInstanceOf(IOException.class);


    }
}