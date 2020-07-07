@ECHO OFF
::--------------
:: build clean
:: build package
::--------------

title build

SET command=%1%

mvn %command% -DskipTests -Pdev
