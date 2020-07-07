@ECHO OFF
rem ---------------------------------------------------------------------------
rem Start script for the Java Server
rem ---------------------------------------------------------------------------



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
echo java -Dloader.path=../lib/ -jar ../lib/assembly-thinbody-1.0.0.jar
java -Dloader.path=../lib/ -jar ../lib/assembly-thinbody-1.0.0.jar
goto END

:IMP
echo java -Dloader.path=../lib/ -jar ../lib/assembly-utils-1.0.0.jar
java -Dloader.path=../lib/ -jar ../lib/assembly-utils-1.0.0.jar
goto END

:WEB
echo java -Dloader.path=../lib/ -jar ../lib/assembly-web-1.0.0.jar
java -Dloader.path=../lib/ -jar ../lib/assembly-web-1.0.0.jar
goto END


:END
pause