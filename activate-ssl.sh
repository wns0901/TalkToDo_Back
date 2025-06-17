#!/bin/bash

DOMAIN="api-talktodo.kro.kr"

echo "🔒 SSL 인증서 활성화 중..."
echo "도메인: $DOMAIN"

# SSL 인증서 확인
if [ -f "/etc/letsencrypt/live/$DOMAIN/fullchain.pem" ]; then
    echo "✅ SSL 인증서가 존재합니다."
    
    # Nginx 설치
    echo "📦 Nginx 설치 중..."
    sudo apt-get update
    sudo apt-get install -y nginx
    
    # Nginx 설정 파일 복사
    echo "⚙️ Nginx 설정 중..."
    sudo cp nginx-spring.conf /etc/nginx/sites-available/talktodo
    sudo ln -sf /etc/nginx/sites-available/talktodo /etc/nginx/sites-enabled/
    sudo rm -f /etc/nginx/sites-enabled/default
    
    # Nginx 설정 테스트
    echo "🔍 Nginx 설정 테스트 중..."
    sudo nginx -t
    
    if [ $? -eq 0 ]; then
        echo "✅ Nginx 설정이 올바릅니다."
        
        # Nginx 재시작
        echo "🔄 Nginx 재시작 중..."
        sudo systemctl restart nginx
        sudo systemctl enable nginx
        
        echo "🎉 SSL 설정이 완료되었습니다!"
        echo "🌐 HTTPS 접속 테스트: https://$DOMAIN"
        
        # HTTPS 연결 테스트
        echo "🔍 HTTPS 연결 테스트 중..."
        curl -I https://$DOMAIN --connect-timeout 10 || echo "HTTPS 연결 실패"
        
    else
        echo "❌ Nginx 설정에 오류가 있습니다."
        exit 1
    fi
    
else
    echo "❌ SSL 인증서가 존재하지 않습니다."
    exit 1
fi

echo "✅ SSL 활성화 완료!" 