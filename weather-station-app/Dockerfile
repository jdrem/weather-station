FROM openjdk:17-jdk-alpine
RUN addgroup -S spring && \
    adduser -S spring -G spring && \
    RUN apk update && \
    apk upgrade && \
    apk add bashUSER spring:spring
COPY BOOT-INF/lib /app/lib
COPY META-INF /app/META-INF
COPY BOOT-INF/classes /app
CMD ["start.sh"]
