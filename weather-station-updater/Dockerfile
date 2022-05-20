FROM openjdk:17-jdk-alpine
RUN addgroup -S spring && \
    adduser -S spring -G spring && \
    apk update && \
    apk upgrade && \
    apk add bash
USER spring:spring
COPY BOOT-INF/lib /app/lib
COPY META-INF /app/META-INF
COPY BOOT-INF/classes /app
COPY *.sh /app/bin
ENV PATH="/app/bin:${PATH}"
CMD ["start.sh"]
