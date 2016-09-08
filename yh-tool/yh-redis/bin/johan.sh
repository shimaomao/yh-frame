#!/bin/sh 
echo "shell path:${PWD}"
cd ..
echo "JVM Root path ${PWD}"
CP=.
for file in lib/*;
do CP=${CP}:$file;
done
echo "Current cp include ${CP}"
JOHANCLASS=.
CLASSPATH=$CLASSPATH:$CP
CLASSPATH=$CLASSPATH:$JOHANCLASS/classes
echo "Current ClassPath=${CLASSPATH}"
export CLASSPATH
JAVA_OPTS=" -Dmlh-mongo -XX:InitialHeapSize=64m -XX:MaxHeapSize=64m -Dfile.encoding=UTF-8 -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -Djava.net.preferIPv4Stack=true"
echo ${JAVA_OPTS}
export JAVA_OPTS
java ${JAVA_OPTS} com.palm.vertx.core.application.Johan >> log.out &
