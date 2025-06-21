#!/bin/bash

echo "=== CORS 디버깅 스크립트 ==="
echo ""

# 1. OPTIONS 요청 테스트 (CORS Preflight)
echo "1. OPTIONS 요청 테스트 (CORS Preflight):"
curl -X OPTIONS \
  -H "Origin: https://talktodo.kro.kr" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type,Authorization" \
  -v https://talktodo.kro.kr/api/schedules/todo/6/calendar
echo ""
echo ""

# 2. 실제 POST 요청 테스트
echo "2. 실제 POST 요청 테스트:"
curl -X POST \
  -H "Origin: https://talktodo.kro.kr" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test-token" \
  -d '{"test": "data"}' \
  -v https://talktodo.kro.kr/api/schedules/todo/6/calendar
echo ""
echo ""

# 3. Spring Boot 애플리케이션 직접 테스트
echo "3. Spring Boot 애플리케이션 직접 테스트 (localhost:8080):"
curl -X POST \
  -H "Origin: https://talktodo.kro.kr" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test-token" \
  -d '{"test": "data"}' \
  -v http://localhost:8080/api/schedules/todo/6/calendar
echo ""
echo ""

# 4. Nginx 상태 확인
echo "4. Nginx 상태 확인:"
sudo systemctl status nginx
echo ""

# 5. Spring Boot 애플리케이션 상태 확인
echo "5. Spring Boot 애플리케이션 상태 확인:"
ps aux | grep java
echo ""

# 6. 포트 확인
echo "6. 포트 확인:"
sudo netstat -tlnp | grep -E ':(80|443|8080)'
echo ""

echo "=== 디버깅 완료 ===" 