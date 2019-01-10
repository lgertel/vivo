FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd

ADD ./target/aureo-0.0.1-SNAPSHOT.jar /app/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh

EXPOSE 6000