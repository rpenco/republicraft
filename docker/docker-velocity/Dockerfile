FROM openjdk:8-jre-alpine
MAINTAINER rpenco <rpenco@users.noreply.github.com>

ARG VELOCITY_VERSION
ARG VELOCITY_BUILD
ARG VELOCITY_MD5

ENV LANG C.UTF-8
ENV JVM_OPTS -Xms256M -Xmx512M -XX:+UseConcMarkSweepGC
ENV VELOCITY_HOME /velocity
ENV VELOCITY_VERSION $VELOCITY_VERSION
ENV VELOCITY_BUILD $VELOCITY_BUILD
ENV VELOCITY_MD5 $VELOCITY_MD5

RUN apk add --no-cache tzdata
ENV TZ=Europe/Paris

COPY start.sh /usr/bin/
COPY log4j2.xml ${VELOCITY_HOME}/
RUN chmod +x /usr/bin/start.sh

ADD target/velocity-proxy-${VELOCITY_VERSION}-all.jar ${VELOCITY_HOME}/velocity-proxy.jar

RUN mkdir /server
WORKDIR /server

ENTRYPOINT ["/usr/bin/start.sh"]

EXPOSE 25577
