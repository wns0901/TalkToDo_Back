package com.example.TalkToDo.service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String region;

  public String uploadFile(MultipartFile file) {
    try {
      String fileName = addTimestampToFilename(file.getOriginalFilename());
      String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
      String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + "audio" + "/"
          + encodedFileName;

      ObjectMetadata metadata = new ObjectMetadata();

      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());

      amazonS3Client.putObject(bucket, "audio" + "/" + fileName, file.getInputStream(), metadata);

      return fileUrl;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  private String addTimestampToFilename(String filename) {
    String timestamp = String.valueOf(System.currentTimeMillis());
    int lastDotIndex = filename.lastIndexOf(".");
    if (lastDotIndex == -1) {
      return filename + "_" + timestamp;
    } else {
      return filename.substring(0, lastDotIndex) + "_" + timestamp + filename.substring(lastDotIndex);
    }
  }

  public void deleteFile(String url) {
    String fileName = url.substring(54);
    String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
    amazonS3Client.deleteObject(bucket, decodedFileName);
  }
}
