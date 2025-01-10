package com.EmbersTrial;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.nio.file.Paths;

public class S3Manager {
    private final S3Client s3;
    private final String bucketName = "embertrials";

    public S3Manager() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("AKIAYRH5NC7XO6HV2U47", "16vwSPoXltIkuV0XApkVcTHJZJxxK6Ug0s3lh3zq");

        s3 = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .region(Region.US_WEST_2) // Replace with your region
            .build();
    }

    public void downloadFile(String objectKey, String localPath) {
        try {
            System.out.println("Attempting to download from S3: " + objectKey + " -> " + localPath);
            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
            s3.getObject(request, Paths.get(localPath));
            System.out.println("Downloaded: " + objectKey + " to " + localPath);
        } catch (Exception e) {
            System.err.println("Failed to download file: " + objectKey);
            e.printStackTrace();
        }
    }
}
