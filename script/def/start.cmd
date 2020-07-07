@ECHO OFF
rem ---------------------------------------------------------------------------
rem Start script for the Java Server
rem ---------------------------------------------------------------------------

title build-project

setLocal

SET MODE=%1%

if "%MODE%" == "thin" (
    echo thin
	goto EXP
) else if "%MODE%" == "util" (
    echo util
    goto IMP
) else if "%MODE%" == "web" (
    echo web
    goto WEB
) else (
    echo else
    goto END
)


:EXP
echo java -Dloader.path=../lib/ -jar ../jar/assembly-thinbody-1.0.0.jar
start cmd /k java -Dloader.path=../lib/ -jar ../jar/assembly-thinbody-1.0.0.jar
goto END

:IMP
echo java -Dloader.path=../lib/ -jar ../jar/assembly-utils-1.0.0.jar
java -Dloader.path=../lib/ -jar ../jar/assembly-utils-1.0.0.jar
goto END

:WEB
echo java -Dloader.path=../lib/ -jar ../jar/assembly-web-1.0.0.jar
java -Dloader.path=../lib/ -jar ../jar/assembly-web-1.0.0.jar
goto END


:END
pause