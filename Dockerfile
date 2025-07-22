FROM amazoncorretto:17

# 타임존 설정
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# JAR 복사
COPY build/libs/Jajaja-0.0.1-SNAPSHOT.jar jajaja_dev.jar

# Spring profile 환경변수 기본값 (main으로 변경)
ENV SPRING_PROFILES_ACTIVE=main

# JAR 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/jajaja_dev.jar"]