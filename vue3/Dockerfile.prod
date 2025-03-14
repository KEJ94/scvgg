# 1. Node.js 빌드 단계
FROM node:18 AS build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. package.json과 package-lock.json을 먼저 복사하고 의존성 설치
COPY package*.json ./
RUN npm install

# 4. 실행 권한 부여 (vue-cli-service 실행 오류 방지)
RUN chmod +x node_modules/.bin/vue-cli-service

# 5. 전체 프로젝트 파일을 복사
COPY . .

# 6. 애플리케이션 빌드
RUN chmod +x node_modules/.bin/vue-cli-service && npm run build

# 7. Nginx를 사용한 프로덕션 환경 설정
FROM nginx:alpine

# 8. 빌드된 앱을 Nginx의 기본 경로로 복사
COPY --from=build /app/dist /usr/share/nginx/html

# 9. Nginx 서버가 80 포트를 사용하도록 설정
EXPOSE 80

# 10. Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
