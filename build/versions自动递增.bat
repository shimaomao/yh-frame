@echo off 

echo 开始进行打包

set BASE_DIR=%cd%
set WORK_DIR=%BASE_DIR%\..\
cd %WORK_DIR%
echo 当前运行目录：%cd%

call mvn build-helper:parse-version --batch-mode release:update-versions -DdevelopmentVersion=${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.nextIncrementalVersion}-SNAPSHOT


pause