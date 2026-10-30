#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"
mvn clean package -DskipTests
cp target/projetJEE-1.0-SNAPSHOT.war /tmp/tomcat-local/webapps/projetJEE.war
exec /tmp/tomcat-local/bin/catalina.sh run
