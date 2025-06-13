package com.example.TalkToDo.service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import lombok.RequiredArgsConstructor;
import java.net.URL;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String region;

  private String createFileName(String originalFileName, String dirName) {
    return dirName + "/" + UUID.randomUUID() + "_" + originalFileName;
  }

  public String uploadFile(File file, String dirName) {
    String fileName = createFileName(file.getName(), dirName);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType("application/octet-stream");
    metadata.setContentLength(file.length());

    try {
      PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file)
          .withCannedAcl(CannedAccessControlList.PublicRead);
      s3Client.putObject(putObjectRequest);
      return s3Client.getUrl(bucket, fileName).toString();
    } catch (Exception e) {
      throw new RuntimeException("파일 업로드 실패", e);
    }
  }

  public String getTemporaryUrl(String fileUrl) {
    String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * 60; // 1시간
    expiration.setTime(expTimeMillis);

    URL url = s3Client.generatePresignedUrl(bucket, key, expiration);
    return url.toString();
  }

  public byte[] downloadFile(String fileUrl) {
    String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    S3Object s3Object = s3Client.getObject(bucket, key);
    S3ObjectInputStream inputStream = s3Object.getObjectContent();
    try {
      return IOUtils.toByteArray(inputStream);
    } catch (IOException e) {
      throw new RuntimeException("Error downloading file from S3", e);
    }
  }

  public void deleteFile(String url) {
    String fileName = url.substring(54);
    String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
    s3Client.deleteObject(bucket, decodedFileName);
  }
}
