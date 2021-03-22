#!/usr/bin/env sh

echo "Starting PaperMC..."
echo "PAPER_VERSION=${PAPER_VERSION}"
echo "PAPER_BUILD=${PAPER_BUILD}"
echo "PAPER_MD5=${PAPER_MD5}"
echo "java \"$JVM_OPTS\" -jar \"${PAPER_HOME}/paper.jar\" \"$@\""
echo ""
echo eula=true >eula.txt && java -Dlog4j.configurationFile=${PAPER_HOME}/log4j2.xml ${JVM_OPTS} -jar ${PAPER_HOME}/paper.jar $@
