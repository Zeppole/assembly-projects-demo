## 导出某个项目依赖的jar包
```cmd
mvn dependency:copy-dependencies -pl thinbody-project -am -DoutputDirectory=./lib  -DincludeScope=runtime
```
