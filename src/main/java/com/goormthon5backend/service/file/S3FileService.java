package com.goormthon5backend.service.file;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3FileService {

    private final S3Client s3Client;
    private final String bucket;

    public S3FileService(S3Client s3Client, @Value("${aws.s3.bucket:goorm-5-bucket}") String bucket) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    public UploadFileResponse upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file is empty");
        }

        String originalFileName = Objects.requireNonNullElse(file.getOriginalFilename(), "file");
        String key = "uploads/" + UUID.randomUUID() + extractExtension(originalFileName);
        String contentType = Objects.requireNonNullElse(file.getContentType(), "application/octet-stream");

        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(contentType)
            .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        String fileUrl = s3Client.utilities()
            .getUrl(GetUrlRequest.builder().bucket(bucket).key(key).build())
            .toExternalForm();

        return new UploadFileResponse(bucket, key, fileUrl, originalFileName, contentType, file.getSize());
    }

    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex);
    }

    public record UploadFileResponse(
        String bucket,
        String key,
        String fileUrl,
        String originalFileName,
        String contentType,
        Long size
    ) {
    }
}
