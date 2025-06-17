#!/bin/bash

DOMAIN="api-talktodo.kro.kr"

echo "🔍 SSL 인증서 상태 확인 중..."
echo "도메인: $DOMAIN"

# 기존 인증서 확인
if [ -d "/etc/letsencrypt/live/$DOMAIN" ]; then
    echo "✅ 기존 SSL 인증서가 존재합니다:"
    ls -la /etc/letsencrypt/live/$DOMAIN/
    
    # 인증서 만료일 확인
    echo "📅 인증서 만료일:"
    sudo openssl x509 -in /etc/letsencrypt/live/$DOMAIN/cert.pem -text -noout | grep "Not After"
    
    # 인증서 갱신 가능 여부 확인
    echo "🔄 인증서 갱신 테스트:"
    sudo certbot renew --dry-run
else
    echo "❌ SSL 인증서가 존재하지 않습니다."
fi

# Let's Encrypt 로그 확인
echo "📋 Let's Encrypt 로그 (최근 20줄):"
sudo tail -20 /var/log/letsencrypt/letsencrypt.log

# 인증서 발급 이력 확인
echo "📊 인증서 발급 이력:"
sudo certbot certificates 