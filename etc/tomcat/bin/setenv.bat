set CATALINA_OPTS=-Xmx512m -XX:MaxPermSize=128m -Dfile.encoding=UTF-8

set CATALINA_OPTS=%CATALINA_OPTS% -Dlog4j.configuration=file:/${catalina.home}/conf/log4j.xml -Dlog4j.configuratorClass=com.haulmont.cuba.core.sys.logging.CubaLog4jConfigurator

set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote 
rem set CATALINA_OPTS=%CATALINA_OPTS% -Djava.rmi.server.hostname=localhost 
rem set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.port=7777 
rem set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.ssl=false 
rem set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.authenticate=false
rem set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.password.file=../conf/jmxremote.password -Dcom.sun.management.jmxremote.access.file=../conf/jmxremote.access

set JPDA_OPTS=-Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n
