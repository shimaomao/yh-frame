@echo off 

echo ��ʼ���д��

set BASE_DIR=%cd%
set WORK_DIR=%BASE_DIR%\..\
cd %WORK_DIR%
echo ��ǰ����Ŀ¼��%cd%

call mvn clean package -U -Ptest -Dmaven.test.skip=true


pause