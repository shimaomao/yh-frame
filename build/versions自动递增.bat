@echo off 

echo ��ʼ���д��

set BASE_DIR=%cd%
set WORK_DIR=%BASE_DIR%\..\
cd %WORK_DIR%
echo ��ǰ����Ŀ¼��%cd%

call mvn build-helper:parse-version --batch-mode release:update-versions -DdevelopmentVersion=${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.nextIncrementalVersion}-SNAPSHOT


pause