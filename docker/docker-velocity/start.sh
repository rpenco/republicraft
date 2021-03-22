#!/usr/bin/env sh

echo "Starting Velocity..."
echo "VELOCITY_VERSION=${VELOCITY_VERSION}"
echo "VELOCITY_BUILD=${VELOCITY_BUILD}"
echo "VELOCITY_MD5=${VELOCITY_MD5}"
echo "java -Dlog4j.configurationFile=${VELOCITY_HOME}/log4j2.xml \"$JVM_OPTS\" -jar \"${VELOCITY_HOME}/velocity-proxy.jar\" \"$@\""
echo ""
java -Dlog4j.configurationFile=${VELOCITY_HOME}/log4j2.xml ${JVM_OPTS} -jar ${VELOCITY_HOME}/velocity-proxy.jar $@
