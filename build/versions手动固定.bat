@echo off 

echo ��ʼ���д��

set BASE_DIR=%cd%
set WORK_DIR=%BASE_DIR%\..\
cd %WORK_DIR%
echo ��ǰ����Ŀ¼��%cd%

call mvn --batch-mode release:update-versions -DdevelopmentVersion=1.3.0-SNAPSHOT


pause