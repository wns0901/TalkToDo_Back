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

@Component
@RequiredArgsConstructor
public class Api {

  private final RestTemplate restTemplate;

  @Value("${api.url}")
  private String aiServerUrl;

  public MeetingDataDTO getMeetingData(MultipartFile audioFile) {
    try {
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("audio",
          new MultipartInputStreamFileResource(audioFile.getInputStream(), audioFile.getOriginalFilename()));

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      ResponseEntity<MeetingDataDTO> response = restTemplate.postForEntity(aiServerUrl, requestEntity,
          MeetingDataDTO.class);
      return response.getBody();
    } catch (IOException e) {
      throw new RuntimeException("파일 전송 중 오류 발생", e);
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
