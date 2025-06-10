package com.example.TalkToDo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.io.InputStream;

import com.example.TalkToDo.dto.MeetingDataDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Api {

  private final RestTemplate restTemplate;

  @Value("${api.url}")
  private String aiServerUrl;

  public MeetingDataDTO getMeetingData(MultipartFile audioFile, String date) {
    try {
      if (audioFile == null || audioFile.isEmpty()) {
        throw new IllegalArgumentException("오디오 파일이 비어있습니다.");
      }

      log.info("AI 서버 URL: {}", aiServerUrl);
      log.info("파일 크기: {} bytes", audioFile.getSize());
      log.info("파일 이름: {}", audioFile.getOriginalFilename());

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("audio",
          new MultipartInputStreamFileResource(audioFile.getInputStream(), audioFile.getOriginalFilename()));
      body.add("date", date);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      log.info("AI 서버로 요청 전송 시작");
      ResponseEntity<MeetingDataDTO> response = restTemplate.postForEntity(aiServerUrl, requestEntity,
          MeetingDataDTO.class);
      log.info("AI 서버 응답 수신 완료");

      return response.getBody();
    } catch (IOException e) {
      log.error("파일 전송 중 오류 발생: {}", e.getMessage(), e);
      throw new RuntimeException("파일 전송 중 오류 발생: " + e.getMessage(), e);
    } catch (Exception e) {
      log.error("AI 서버 요청 중 오류 발생: {}", e.getMessage(), e);
      throw new RuntimeException("AI 서버 요청 중 오류 발생: " + e.getMessage(), e);
    }
  }
}

class MultipartInputStreamFileResource extends InputStreamResource {
  private final String filename;

  public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
    super(inputStream);
    this.filename = filename;
  }

  @Override
  public String getFilename() {
    return this.filename;
  }

  @Override
  public long contentLength() throws IOException {
    return -1;
  }
}
