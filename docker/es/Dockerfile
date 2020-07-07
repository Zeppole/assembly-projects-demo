FROM centos:7.5.1804
MAINTAINER zeppole "zeppole@163.com"

ENV BASE_USER="basic-user" \
    BASE_GROUP="basic-user" \
    BASE_DIR="/home/basic-user" \
    JAVA_HOME="/usr/lib/jvm/java-1.8.0-openjdk" \
    JAVA="/usr/lib/jvm/java-1.8.0-openjdk/bin/java" \
    JRE_HOME="/usr/lib/jvm/java-1.8.0-openjdk/jre" \
    PATH="/usr/lib/jvm/java-1.8.0-openjdk/bin:$PATH" \
    JVM_XMS="2g" \
    JVM_XMX="2g" \
    JVM_XMN="1g" \
    JVM_MS="128m" \
    JVM_MMS="320m" \
    TOMCAT_ACCESSLOG_ENABLED="false" \
    TIME_ZONE="Asia/Shanghai"

ARG ES_VERSION=6.2.4

RUN set -x \
    && yum update -y \
    && echo "安装JDK" \
    && yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel wget iputils nc vim libcurl initscripts\
    && echo "设置时区" \
    && ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo '$TIME_ZONE' > /etc/timezone \
    && yum clean all \
    && echo "新建用户" \
    && groupadd --gid 5000 $BASE_USER \
    && useradd --home-dir /home/$BASE_USER --create-home --uid 5000 --gid 5000 --shell /bin/bash --skel /etc/skel $BASE_USER \
    && chown -R $BASE_USER:$BASE_USER $BASE_DIR

WORKDIR /$BASE_DIR

USER $BASE_USER



COPY elasticsearch-$ES_VERSION elasticsearch-$ES_VERSION

RUN chmod u+x $BASE_DIR/elasticsearch-$ES_VERSION/bin/elasticsearch \
    && chown -R $BASE_USER:$BASE_USER $BASE_DIR



EXPOSE 9200 9300
VOLUME $BASE_DIR/elasticsearch-$ES_VERSION/data/

ENTRYPOINT elasticsearch-$ES_VERSION/bin/elasticsearch -d


# docker build -f Dockerfile -t es/es:6.2.4 .
# docker run -d -p 9200:9200 --name=centos7 es/es:6.2.4