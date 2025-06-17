 #!/bin/bash

echo "🔧 외부 Nginx 설정 시작..."

# 1. Nginx 설치
echo "📦 Nginx 설치 중..."
sudo apt-get update
sudo apt-get install -y nginx

# 2. 기존 설정 백업
echo "💾 기존 설정 백업..."
sudo cp /etc/nginx/sites-available/default /etc/nginx/sites-available/default.backup 2>/dev/null || echo "기본 설정 파일이 없습니다."

# 3. HTTP 전용 설정 생성 (SSL 없이)
echo "⚙️ HTTP 전용 Nginx 설정 생성..."
cat > /tmp/nginx-http.conf << 'EOF'
# HTTP 서버 (포트 80)
server {
    listen 80;
    server_name api-talktodo.kro.kr www.api-talktodo.kro.kr;

    # Spring Boot 애플리케이션으로 프록시 (Docker 컨테이너)
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 타임아웃 설정
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
        
        # 버퍼 설정
        proxy_buffering on;
        proxy_buffer_size 4k;
        proxy_buffers 8 4k;
    }

    # API 경로별 설정
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 정적 파일 캐싱
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        proxy_pass http://localhost:8080;
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # 헬스 체크
    location /health {
        proxy_pass http://localhost:8080/actuator/health;
        access_log off;
    }
}
EOF

# 4. 설정 파일 복사
echo "📄 설정 파일 복사..."
sudo cp /tmp/nginx-http.conf /etc/nginx/sites-available/talktodo
sudo ln -sf /etc/nginx/sites-available/talktodo /etc/nginx/sites-enabled/
sudo rm -f /etc/nginx/sites-enabled/default

# 5. Nginx 설정 테스트
echo "🧪 Nginx 설정 테스트..."
sudo nginx -t

if [ $? -eq 0 ]; then
    echo "✅ Nginx 설정이 올바릅니다."
    
    # 6. Nginx 재시작
    echo "🔄 Nginx 재시작 중..."
    sudo systemctl restart nginx
    sudo systemctl enable nginx
    
    # 7. 상태 확인
    echo "📋 Nginx 상태 확인:"
    sudo systemctl status nginx --no-pager
    
    echo "🎉 외부 Nginx 설정이 완료되었습니다!"
    echo "🌐 HTTP 접속: http://api-talktodo.kro.kr"
    echo "📊 포트 상태:"
    sudo netstat -tlnp | grep -E ":80|:8080"
    
else
    echo "❌ Nginx 설정에 오류가 있습니다."
    exit 1
fi

echo "✅ 외부 Nginx 설정 완료!"