package com.example.TalkToDo.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.RequiredArgsConstructor;

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
    try {
      if (fileUrl == null || fileUrl.trim().isEmpty()) {
        throw new RuntimeException("파일 URL이 비어있습니다.");
      }

      // URL에서 실제 S3 키 추출
      String key;
      if (fileUrl.startsWith("http")) {
        // 전체 URL이 주어진 경우
        String domain = bucket + ".s3." + region + ".amazonaws.com";
        if (!fileUrl.contains(domain)) {
          throw new RuntimeException("잘못된 S3 URL 형식입니다: " + fileUrl);
        }
        key = fileUrl.substring(fileUrl.indexOf(domain) + domain.length() + 1);
      } else {
        // 키만 주어진 경우
        key = fileUrl;
      }

      // URL 디코딩
      key = URLDecoder.decode(key, StandardCharsets.UTF_8);

      S3Object s3Object = s3Client.getObject(bucket, key);
      S3ObjectInputStream inputStream = s3Object.getObjectContent();
      return IOUtils.toByteArray(inputStream);
    } catch (AmazonS3Exception e) {
      if (e.getStatusCode() == 404) {
        throw new RuntimeException("파일을 찾을 수 없습니다: " + fileUrl, e);
      }
      throw new RuntimeException("S3 파일 다운로드 중 오류가 발생했습니다: " + fileUrl, e);
    } catch (IOException e) {
      throw new RuntimeException("파일 다운로드 중 오류가 발생했습니다: " + fileUrl, e);
    }
  }

  public void deleteFile(String url) {
    String fileName = url.substring(54);
    String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
    s3Client.deleteObject(bucket, decodedFileName);
  }

}
