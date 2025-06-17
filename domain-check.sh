#!/bin/bash

DOMAIN="api-talktodo.kro.kr"

echo "🔍 도메인 연결 상태 확인 중..."
echo "도메인: $DOMAIN"

# DNS 조회
echo "📡 DNS 조회 결과:"
nslookup $DOMAIN

# HTTP 연결 테스트
echo "🌐 HTTP 연결 테스트:"
curl -I http://$DOMAIN --connect-timeout 10 || echo "HTTP 연결 실패"

# HTTPS 연결 테스트 (SSL 인증서 발급 후)
echo "🔒 HTTPS 연결 테스트:"
curl -I https://$DOMAIN --connect-timeout 10 || echo "HTTPS 연결 실패"

# 포트 스캔
echo "🔌 포트 상태 확인:"
nc -zv $DOMAIN 80 2>&1 || echo "포트 80 연결 실패"
nc -zv $DOMAIN 443 2>&1 || echo "포트 443 연결 실패"

echo "✅ 도메인 확인 완료" 