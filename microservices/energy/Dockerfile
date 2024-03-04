FROM maven:3.8.3-openjdk-17 AS builder

COPY ./src/ /root/src
COPY ./pom.xml /root/
WORKDIR /root
RUN mvn package -DskipTests
RUN ls -l /root
RUN java -Djarmode=layertools -jar /root/target/energy-0.0.1-SNAPSHOT.jar list
RUN java -Djarmode=layertools -jar /root/target/energy-0.0.1-SNAPSHOT.jar extract
RUN ls -l /root

FROM openjdk:17-jdk-slim-buster

ENV TZ=UTC
ENV DB_IP=localhost
ENV DB_PORT=3306
ENV DB_USER=root
ENV DB_PASSWORD=root
ENV DB_DBNAME=user-db

ENV USERS_MICROSERVICE_IP=http://localhost:8080
ENV DEVICES_MICROSERVICE_IP=http://localhost:8081
ENV MONITORING_MICROSERVICE_IP=http://localhost:8082
ENV CHAT_MICROSERVICE_IP=http://localhost:8083

COPY --from=builder /root/dependencies/ ./
COPY --from=builder /root/snapshot-dependencies/ ./

RUN sleep 10
COPY --from=builder /root/spring-boot-loader/ ./
COPY --from=builder /root/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher","-XX:+UseContainerSupport -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Xms512m -Xmx512m -XX:+UseG1GC -XX:+UseSerialGC -Xss512k -XX:MaxRAM=72m"]