FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.4_11 as packager

VOLUME /tmp

RUN mkdir -p /usr/local/dockerize

#quando for pelo eclipse @project.build.finalName@.jar
ARG JAR_FILE=cursomc-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]







#RUN apk update && apk upgrade
#copiar o pacote da aplicação para uma pasta apropriada
#RUN mkdir -p /usr/local/dockerize
#ADD  @project.build.finalName@.jar /usr/local/dockerize/