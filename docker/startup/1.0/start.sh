#!/bin/bash

export JAVA_OPTS="$JAVA_OPTS -Darchaius.configurationSource.additionalUrls=file:/opt/tools/tomcat/config/application.properties -XX:InitialRAMPercentage=20.0 -XX:MaxRAMPercentage=90.0 -Dmode=$DEPLOYMODE"

#UPLOAD_CONFIG_FILE



bash /opt/tools/tomcat/bin/catalina.sh run
