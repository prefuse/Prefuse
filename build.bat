@echo off
echo building prefuse...

if "%JAVA_HOME%" == "" goto error

set LOCALCLASSPATH=%JAVA_HOME%\lib\tools.jar;.\lib\ant.jar;%CLASSPATH%
set ANT_HOME=./lib

echo ... using classpath %LOCALCLASSPATH%

%JAVA_HOME%\bin\java.exe -Dant.home="%ANT_HOME%" -classpath "%LOCALCLASSPATH%" org.apache.tools.ant.Main %1 %2 %3 %4 %5

goto end

:error
echo "... BUILD FAILED"
echo " The JAVA_HOME environment variable was not found."
echo " Please set the environment variable JAVA_HOME to the location"
echo " of your preferred Java installation."

:end
set LOCALCLASSPATH=