package com.example.TalkToDo.service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

  public String uploadFile(File file, String folder) {
    try {
      String fileName = addTimestampToFilename(file.getName());
      String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
      String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + folder + "/"
          + encodedFileName;

      ObjectMetadata metadata = new ObjectMetadata();

      // content-type을 파일 확장자에 따라 지정 (예: mp3)
      String contentType = "application/octet-stream";
      if (fileName.endsWith(".mp3")) {
        contentType = "audio/mpeg";
      }
      metadata.setContentType(contentType);
      metadata.setContentLength(file.length());

      try (FileInputStream fis = new FileInputStream(file)) {
        amazonS3Client.putObject(bucket, "audio" + "/" + fileName, fis, metadata);
      }

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
