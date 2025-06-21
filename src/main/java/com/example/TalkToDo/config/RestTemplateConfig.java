package com.example.TalkToDo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // 연결 타임아웃: 30초 (기본값 5초)
        factory.setConnectTimeout(30000);

        // 읽기 타임아웃: 10분 (600초) - AI 처리 시간이 길 수 있으므로 충분히 설정
        factory.setReadTimeout(600000);

        return new RestTemplate(factory);
    }
}
