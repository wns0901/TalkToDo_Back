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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Api {

  private final RestTemplate restTemplate;

  @Value("${api.url}")
  private String aiServerUrl;

  public MeetingDataDTO getMeetingData(MultipartFile audioFile, String date, Long userId) {
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
      body.add("meeting_date", date);
      body.add("user_id", userId);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      log.info("AI 서버로 요청 전송 시작");
      ResponseEntity<MeetingDataDTO> response = restTemplate.postForEntity(aiServerUrl + "/process-meeting", requestEntity,
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

  public String getAiResponse(Long userId, String message) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      // JSON 요청 바디 생성
      ObjectMapper mapper = new ObjectMapper();
      ObjectNode requestBody = mapper.createObjectNode();
      requestBody.put("user_id", userId.toString());
      requestBody.put("message", message);
      String jsonBody = mapper.writeValueAsString(requestBody);

      HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
      ResponseEntity<String> response = restTemplate.postForEntity(aiServerUrl + "/chat", requestEntity, String.class);
      
      // JSON 응답에서 response 필드 추출
      JsonNode rootNode = mapper.readTree(response.getBody());
      JsonNode responseNode = rootNode.get("response");
      
      if (responseNode == null) {
        log.warn("응답에 'response' 필드가 없습니다. 전체 응답: {}", response.getBody());
        return response.getBody();
      }
      
      return responseNode.asText();
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
