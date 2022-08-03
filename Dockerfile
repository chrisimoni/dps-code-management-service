#FROM openjdk:11-jre-slim

#ENV TZ Africa/Lagos

#LABEL maintainer="earnest.suru@interswitchgroup.com"

#ADD target/dps-code-management-service.jar /opt/dps-code-management-service.jar

#COPY src/main/resources /opt/resources

#WORKDIR /opt

#CMD ["java", "-jar", "dps-code-management-service.jar"]


FROM openjdk:11-jre-slim
EXPOSE 8080 5000
RUN set -e
RUN mkdir -p /opt/dps-code-management-service
RUN mkdir -p /opt/dps-code-management-service/config/
RUN cd /opt/dps-code-management-service
RUN mkdir -p /log
RUN ln -s /log logs
WORKDIR /opt/dps-code-management-service
# Set OS time zone
ENV TZ Africa/Lagos
RUN set -e
RUN cd /opt/dps-code-management-service/config/

RUN cd /opt/dps-code-management-service/
ADD target/dps-code-management-service-*.jar /opt/dps-code-management-service/dps-code-management-service-*.jar
CMD java -jar -Dspring.profiles.active=docker dps-code-management-service-*.jar