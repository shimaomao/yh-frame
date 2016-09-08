@echo off 

echo begin palm-vertx deploy

set BASE_DIR=%cd%
set WORK_DIR=%BASE_DIR%\..\
cd %WORK_DIR%
echo folder£º%cd%

call mvn clean deploy -Ppkg -U -Dmaven.test.skip=true


pause