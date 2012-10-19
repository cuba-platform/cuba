rem Current Date
Set Tdate=%date:~6,4%_%date:~3,2%_%date:~0,2%
rem Postgresql path
set PG_DIR=C:\Program Files\PostgreSQL\8.3\bin
rem Tomcat path
set TOMCAT_DIR=D:\work\thesis2\tomcat
rem Backup root folder
set BACKUP_DIR=C:\1
rem Postgresql host and port
set PG_HOST=localhost
set PG_PORT=5432
rem Database name
set DB_NAME=docflow
rem Database user
set PG_USER=root

if exist "%Tdate%" goto okTdate
mkdir %Tdate%
:okTdate

set DUMP_FILE=%BACKUP_DIR%\\%Tdate%\\%DB_NAME%_%Tdate%.dump

"%PG_DIR%\pg_dump.exe" -i -h %PG_HOST% -p %PG_PORT% -Fc -f %DUMP_FILE% -U %PG_USER% %DB_NAME%

set TOMCAT_BACKUP_DIR=%BACKUP_DIR%\\%Tdate%\\tomcat

if exist "%TOMCAT_BACKUP_DIR%" goto okFS
mkdir %TOMCAT_BACKUP_DIR%
:okFS

xcopy %TOMCAT_DIR% %TOMCAT_BACKUP_DIR% /D /E /Y