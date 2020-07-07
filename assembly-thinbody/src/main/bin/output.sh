#!/bin/bash

# 导出某个项目依赖的jar包
mvn dependency:copy-dependencies -pl nacos-thinbody -am -DoutputDirectory=D:\\temp\\thinbody\\lib  -DincludeScope=runtime


