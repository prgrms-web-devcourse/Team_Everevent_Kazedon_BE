FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} Team_Everevent_Kazedon_BE-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Team_Everevent_Kazedon_BE-0.0.1-SNAPSHOT.jar"]