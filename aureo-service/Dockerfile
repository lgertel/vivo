FROM java:8-jre
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/aureo-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/aureo-service.jar"]

EXPOSE 6000