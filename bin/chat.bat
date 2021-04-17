@ECHO OFF&PUSHD %~DP0 &TITLE 安装、打包、发布工具
:: mode con cols=200 lines=40
:: color 2F

%~d0
cd %~dp0
cd ..

:menu
Cls
@ echo.
@ echo.  菜单选项：
@ echo.
@ echo.   1.清理
@ echo.
@ echo.   2.安装本地仓库
@ echo.
@ echo.   3.打test包
@ echo.
@ echo.   4.打prod包
@ echo.
@ echo.   5.生成javadoc(应用在工具类)
@ echo.
@ echo.   6.发布(发布是应用在工具类、核心jar、基础模块)
@ echo.
@ echo.   7.升级版本
@ echo.
@ echo.   0.退出
@ echo.
@ echo.   提示：执行选项报错请先检查maven环境变量是否配置正确，请mvn -version检查maven是否正确安装，谢谢!
@ echo.
@ echo.   提示：发布失败请检查是否配置私服发布的账号在settings.xml中
@ echo.
@ echo.
@ echo.
@ echo.
@ echo.
set /p xj= 输入数字按回车：
if /i "%xj%"=="1" Goto Clean
if /i "%xj%"=="2" Goto Install
if /i "%xj%"=="3" Goto PackageTest
if /i "%xj%"=="4" Goto PackageProd
if /i "%xj%"=="5" Goto Javadoc
if /i "%xj%"=="6" Goto Deploy
if /i "%xj%"=="7" Goto Version
if /i "%xj%"=="0" Goto Quit
@ echo.
echo 　　    选择无效...请重新输入...
ping -n 2 127.1>nul


goto menu

:: 清除
:Clean
@ echo.
echo  正在清理生成文件...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  清理完成
@ echo.
echo  请按键任意键返回菜单选项
pause > nul
ping -n 2 140.1>nul
goto menu

:: 安装
:Install
@ echo.
echo  正在清理原生成文件...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  正在清理中，请稍等...
@ echo.
echo  开始安装到本地仓库中，请稍等...
call mvn install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  安装到本地仓库完成
@ echo.
@ echo.
echo  请按键任意键返回菜单选项
pause > nul
ping -n 2 140.1>nul
goto menu

:: 打测试环境包
:PackageTest
@ echo.
echo  正在清理原生成文件...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  正在清理中，请稍等...
@ echo.
echo  开始打包生成文件中，请稍等...
call mvn package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Ptest
@ echo.
echo  打包生成文件完成
@ echo.
@ echo.
echo  请按键任意键返回菜单选项
pause > nul
ping -n 2 140.1>nul
goto menu

:: 打正式环境包
:PackageProd
@ echo.
echo  正在清理原生成文件...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  正在清理中，请稍等...
@ echo.
echo  开始打包生成文件中，请稍等...
call mvn package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Pprod
@ echo.
echo  打包生成文件完成
@ echo.
@ echo.
echo  请按键任意键返回菜单选项
pause > nul
ping -n 2 140.1>nul
goto menu

:: 生成Javadoc文档
:Javadoc
@ echo.
echo  开始生成Javadoc文档...
call mvn clean source:jar javadoc:javadoc install -Dmaven.test.skip=false -Dmaven.javadoc.skip=false
@ echo.
echo  生成Javadoc文档完成
@ echo.
echo  请按键任意键返回菜单选项
pause > nul
ping -n 2 140.1>nul
goto menu


:: 发布
:Deploy
@ echo.
echo  正在清理原生成文件...
call mvn clean -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
@ echo.
echo  正在清理中，请稍等...
@ echo.
echo  开始发布到中央仓库中，请稍等...
call mvn deploy -Dmaven.test.skip=true -Dmaven.javadoc.skip=false
@ echo.
echo  发布到中央仓库完成
@ echo.
@ echo.
echo  请按键任意键返回菜单选项
pause > nul
ping -n 2 140.1>nul
goto menu

:: 升级版本
:Version
@ echo.
set /p version=请输入版本号:
echo 提示：需要升级的新版本号为：%version%
call mvn versions:set -DnewVersion=%version%
set /p var=请确认是否升级到 %version% 版本(y:确认、n:撤回):
if %var% EQU y (
echo 开始提交版本号为%version%
@ echo.
call mvn versions:commit
@ echo.
echo 已提交版本号为%version%
)
if %var% EQU n (
echo 开始撤回到原版本号
@ echo.
call mvn versions:revert
@ echo.
echo 已撤回到原版本号
)
echo  请按键任意键返回菜单选项
pause > nul
ping -n 2 140.1>nul
goto menu

:: 退出
:Quit
exit


