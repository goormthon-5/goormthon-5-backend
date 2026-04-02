package com.goormthon5backend.web.file;

import com.goormthon5backend.service.file.S3FileService;
import java.io.IOException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/files")
public class FileUploadController {

    private final S3FileService s3FileService;

    public FileUploadController(S3FileService s3FileService) {
        this.s3FileService = s3FileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public S3FileService.UploadFileResponse upload(@RequestPart("file") MultipartFile file) throws IOException {
        return s3FileService.upload(file);
    }
}
