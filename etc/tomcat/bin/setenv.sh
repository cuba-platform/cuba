CATALINA_OPTS="-Xmx512m -XX:MaxPermSize=128m -Dfile.encoding=UTF-8"

CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote"

#CATALINA_OPTS="$CATALINA_OPTS \
#-Djava.rmi.server.hostname=localhost \
#-Dcom.sun.management.jmxremote.port=7777 \
#-Dcom.sun.management.jmxremote.ssl=false \
#-Dcom.sun.management.jmxremote.authenticate=false"

#CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote.password.file=../conf/jmxremote.password -Dcom.sun.management.jmxremote.access.file=../conf/jmxremote.access"

JPDA_OPTS="-Xrunjdwp:transport=dt_socket,address=9787,server=y,suspend=n"
