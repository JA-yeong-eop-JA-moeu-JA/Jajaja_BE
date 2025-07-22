FROM amazoncorretto:17

# 타임존 설정
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# JAR 복사 (바깥 파일명을 내부에서는 jajaja_dev.jar로 저장)
COPY build/libs/Jajaja-0.0.1-SNAPSHOT.jar jajaja_dev.jar

# Spring profile 환경변수 기본값
ENV SPRING_PROFILES_ACTIVE=develop

# JAR 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/jajaja_dev.jar"]