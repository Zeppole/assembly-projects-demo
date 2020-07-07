FROM centos:7.5.1804
MAINTAINER zeppole "zeppole@163.com"


ARG PYTHON_VERSION=3.7.5
ARG PYTHON_NAME=3.7

ENV BASE_USER="basic-user"

ENV BASE_GROUP="$BASE_USER" \
    BASE_DIR="/home/$BASE_USER" \
    PATH="/usr/bin/python3/bin:$PATH" \
    TIME_ZONE="Asia/Shanghai"


RUN set -x \
    && echo "配置yum源" \
    && mkdir /opt/centos-yum.bak \
    && mv /etc/yum.repos.d/* /opt/centos-yum.bak/ \
    && wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo \
    && yum clean all && yum makecache \
    && echo "yum安装基础组件" \
    && yum install -y libffi-devel zlib-devel bzip2-devel openssl-devel ncurses-devel sqlite-devel readline-devel \
       tk-devel zlib gcc make libpcap-devel xz-devel gdbm-devel wget iputils nc vim libcurl initscripts \
    && echo "安装Python3" \
    && wget https://www.python.org/ftp/python/$PYTHON_VERSION/Python-$PYTHON_VERSION.tgz \
    && tar -zxvf Python-$PYTHON_VERSION.tgz \
    && rm -f Python-$PYTHON_VERSION.tgz \
    && cd Python-$PYTHON_VERSION && ./configure --prefix=/usr/local/python3.7 && make && make install \
    && mv /usr/bin/python /usr/bin/python2.7.5 \
    && ln -s /usr/local/python3.7/bin/python3 /usr/bin/python3 \
    && ln -s /usr/local/python3.7/bin/pip3 /usr/bin/pip3 \
    && ln -s /usr/bin/python3 /usr/bin/python \
    && sed -i s#/usr/bin/python#/usr/bin/python2.7#g /usr/libexec/urlgrabber-ext-down \
    && sed -i s#/usr/bin/python#/usr/bin/python2.7#g /usr/bin/yum-config-manager \
    && sed -i s#/usr/bin/python#/usr/bin/python2.7#g /usr/bin/yum \
    && echo "设置时区" \
    && ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo '$TIME_ZONE' > /etc/timezone \
    && yum clean all \
    && echo "新建用户" \
    && groupadd --gid 5000 $BASE_USER \
    && useradd --home-dir $BASE_DIR --create-home --uid 5000 --gid 5000 --shell /bin/bash --skel /etc/skel $BASE_USER \
    && chown -R $BASE_USER:$BASE_USER $BASE_DIR

WORKDIR /$BASE_DIR

USER $BASE_USER



EXPOSE 9200 9300
VOLUME $BASE_DIR/data/ $BASE_DIR/conf/

ENTRYPOINT ping 127.0.0.1


# docker build -f Dockerfile -t centos-python/centos-python:3.7.5 .
# docker run -d -p 9200:9200 --name=centos7-python centos-python/centos-python:3.7.5