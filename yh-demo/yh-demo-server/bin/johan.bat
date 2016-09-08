@echo off

rem  run JVM
echo jvm start
echo shell path:%CD%
set WORK_DIR=%CD%\..\
cd %WORK_DIR%

echo CURRENT CLASSPATH
set CLASSPATH
@pause

set base=%CD%
set class=%base%\classes
set CLASSPATH=%CLASSPATH%;%class%;
set CLASSPATH
@pause

FOR %%F IN (%cd%\lib\*.jar) DO call :addcp %%F
goto extlibe
:addcp
SET CLASSPATH=%CLASSPATH%;%1
goto :eof
:extlibe
SET CLASSPATH

java  -XX:InitialHeapSize=1g -XX:MaxHeapSize=1g -Dfile.encoding=UTF-8 -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -Djava.net.preferIPv4Stack=true -Dmlh-redis com.palm.vertx.core.application.Johan

@pause
