@ECHO OFF&PUSHD %~DP0 &TITLE ��װ���������������
:: mode con cols=200 lines=40
:: color 2F

%~d0
cd %~dp0
cd ..

:menu
Cls
@ echo.
@ echo.  �˵�ѡ�
@ echo.
@ echo.   1.����
@ echo.
@ echo.   2.��װ���زֿ�
@ echo.
@ echo.   3.��test��
@ echo.
@ echo.   4.��prod��
@ echo.
@ echo.   5.����javadoc(Ӧ���ڹ�����)
@ echo.
@ echo.   6.����(������Ӧ���ڹ����ࡢ����jar������ģ��)
@ echo.
@ echo.   7.�����汾
@ echo.
@ echo.   0.�˳�
@ echo.
@ echo.   ��ʾ��ִ��ѡ������ȼ��maven���������Ƿ�������ȷ����mvn -version���maven�Ƿ���ȷ��װ��лл!
@ echo.
@ echo.   ��ʾ������ʧ�������Ƿ�����˽���������˺���settings.xml��
@ echo.
@ echo.
@ echo.
@ echo.
@ echo.
set /p xj= �������ְ��س���
if /i "%xj%"=="1" Goto Clean
if /i "%xj%"=="2" Goto Install
if /i "%xj%"=="3" Goto PackageTest
if /i "%xj%"=="4" Goto PackageProd
if /i "%xj%"=="5" Goto Javadoc
if /i "%xj%"=="6" Goto Deploy
if /i "%xj%"=="7" Goto Version
if /i "%xj%"=="0" Goto Quit
@ echo.
echo ����    ѡ����Ч...����������...
ping -n 2 127.1>nul


goto menu

:: ���
:Clean
@ echo.
echo  �������������ļ�...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  �������
@ echo.
echo  �밴����������ز˵�ѡ��
pause > nul
ping -n 2 140.1>nul
goto menu

:: ��װ
:Install
@ echo.
echo  ��������ԭ�����ļ�...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  ���������У����Ե�...
@ echo.
echo  ��ʼ��װ�����زֿ��У����Ե�...
call mvn install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  ��װ�����زֿ����
@ echo.
@ echo.
echo  �밴����������ز˵�ѡ��
pause > nul
ping -n 2 140.1>nul
goto menu

:: ����Ի�����
:PackageTest
@ echo.
echo  ��������ԭ�����ļ�...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  ���������У����Ե�...
@ echo.
echo  ��ʼ��������ļ��У����Ե�...
call mvn package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Ptest
@ echo.
echo  ��������ļ����
@ echo.
@ echo.
echo  �밴����������ز˵�ѡ��
pause > nul
ping -n 2 140.1>nul
goto menu

:: ����ʽ������
:PackageProd
@ echo.
echo  ��������ԭ�����ļ�...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  ���������У����Ե�...
@ echo.
echo  ��ʼ��������ļ��У����Ե�...
call mvn package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Pprod
@ echo.
echo  ��������ļ����
@ echo.
@ echo.
echo  �밴����������ز˵�ѡ��
pause > nul
ping -n 2 140.1>nul
goto menu

:: ����Javadoc�ĵ�
:Javadoc
@ echo.
echo  ��ʼ����Javadoc�ĵ�...
call mvn clean source:jar javadoc:javadoc install -Dmaven.test.skip=false -Dmaven.javadoc.skip=false
@ echo.
echo  ����Javadoc�ĵ����
@ echo.
echo  �밴����������ز˵�ѡ��
pause > nul
ping -n 2 140.1>nul
goto menu


:: ����
:Deploy
@ echo.
echo  ��������ԭ�����ļ�...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  ���������У����Ե�...
@ echo.
echo  ��ʼ����������ֿ��У����Ե�...
call mvn deploy -Dmaven.test.skip=true -Dmaven.javadoc.skip=false
@ echo.
echo  ����������ֿ����
@ echo.
@ echo.
echo  �밴����������ز˵�ѡ��
pause > nul
ping -n 2 140.1>nul
goto menu

:: �����汾
:Version
@ echo.
set /p version=������汾��:
echo ��ʾ����Ҫ�������°汾��Ϊ��%version%
call mvn versions:set -DnewVersion=%version%
set /p var=��ȷ���Ƿ������� %version% �汾(y:ȷ�ϡ�n:����):
if %var% EQU y (
echo ��ʼ�ύ�汾��Ϊ%version%
@ echo.
call mvn versions:commit
@ echo.
echo ���ύ�汾��Ϊ%version%
)
if %var% EQU n (
echo ��ʼ���ص�ԭ�汾��
@ echo.
call mvn versions:revert
@ echo.
echo �ѳ��ص�ԭ�汾��
)
echo  �밴����������ز˵�ѡ��
pause > nul
ping -n 2 140.1>nul
goto menu

:: �˳�
:Quit
exit


