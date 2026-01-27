FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Final image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy AppDynamics agent from local directory
# Download the agent first and extract it to ./appd-agent/ directory
COPY appd-agent/ /opt/appdynamics/

# Copy application layers
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

# Set JVM options for AppDynamics
ENV JAVA_OPTS="-javaagent:/opt/appdynamics/javaagent.jar"

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]