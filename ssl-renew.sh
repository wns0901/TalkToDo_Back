#!/bin/bash

# SSL 인증서 자동 갱신 스크립트
DOMAIN="api-talktodo.kro.kr"

echo "🔒 SSL 인증서 갱신을 시작합니다..."

# 인증서 갱신
sudo certbot renew --quiet

# 갱신 성공 여부 확인
if [ $? -eq 0 ]; then
    echo "✅ SSL 인증서 갱신이 완료되었습니다."
    
    # Nginx 재시작
    if docker ps | grep -q nginx; then
        echo "🔄 Nginx 컨테이너를 재시작합니다..."
        docker-compose restart nginx
    else
        echo "🔄 Nginx 서비스를 재시작합니다..."
        sudo systemctl reload nginx
    fi
    
    echo "🎉 SSL 인증서 갱신 및 서비스 재시작이 완료되었습니다."
else
    echo "❌ SSL 인증서 갱신에 실패했습니다."
    exit 1
fi 