@echo off
FOR %%F IN (./lib/*.jar) DO call :addcp %%F
goto extlibe
:addcp
SET CLASSPATH=%CLASSPATH%;"lib/"%1
goto :eof
:extlibe
set CLASSPATH=%CLASSPATH%;./config
echo %CLASSPATH%
java -classpath %CLASSPATH% com.icloudmoo.home.async.msg.center.Start