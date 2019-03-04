FROM maven:3.6-jdk-8-alpine as MAVEN_TOOL_CHAIN
VOLUME /temp
COPY pom.xml /
COPY src /src/
RUN mvn package

FROM openjdk:8-jdk-alpine
VOLUME /app
ARG DEPENDENCY=target/dependency
COPY --from=MAVEN_TOOL_CHAIN ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=MAVEN_TOOL_CHAIN ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=MAVEN_TOOL_CHAIN ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-XX:MaxRAMFraction=1","-XshowSettings:vm","-cp","app:app/lib/*","com.vaga.yawa.YawaApplication"]