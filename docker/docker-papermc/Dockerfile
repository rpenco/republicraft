FROM openjdk:8-jre-alpine
MAINTAINER rpenco <rpenco@users.noreply.github.com>

ARG PAPER_VERSION
ARG PAPER_BUILD
ARG PAPER_MD5

ENV LANG C.UTF-8
ENV JVM_OPTS -Xms256M -Xmx512M -XX:+UseConcMarkSweepGC
ENV PAPER_HOME /papermc
ENV PAPER_VERSION $PAPER_VERSION
ENV PAPER_BUILD $PAPER_BUILD
ENV PAPER_MD5 $PAPER_MD5

RUN apk add --no-cache tzdata
ENV TZ=Europe/Paris

COPY start.sh /usr/bin/
COPY log4j2.xml ${PAPER_HOME}/
RUN chmod +x /usr/bin/start.sh

ADD target/paperclip-${PAPER_BUILD}.jar ${PAPER_HOME}/paper.jar

RUN mkdir /server
WORKDIR /server

ENTRYPOINT ["/usr/bin/start.sh"]

EXPOSE 25577
