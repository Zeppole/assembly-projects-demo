FROM openjdk:8u181-jdk-alpine

MAINTAINER freak "freak@gmail.com"

RUN echo "https://mirrors.ustc.edu.cn/alpine/v3.9/main" > /etc/apk/repositories \
        && echo "https://mirrors.ustc.edu.cn/alpine/v3.9/community" >> /etc/apk/repositories \
        && apk update && apk add --no-cache bash curl tzdata \
        && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
        && rm -rf /var/cache/apk/*
        
ARG workdir=/home/demo/service
VOLUME ${workdir}
WORKDIR ${workdir}
EXPOSE 49303

ADD nacos-consumer-1.0.0.jar consumer.jar
ENTRYPOINT java -jar consumer.jar --server.port=49303 >> consumer.log 2>&1 &

# docker build -t nacos-demo/consumer:1.0.0 .
# docker run -d -p 8082:8082 -p 8083:8082 --name=consumer nacos-demo/consumer:1.0.0