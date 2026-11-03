#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"
export JAVA_HOME="$PWD/OpenJDK25U-jdk_x64_linux_hotspot_25.0.5_8/jdk-25.0.5+8"
export PATH="$JAVA_HOME/bin:$PATH"

bash ./mvnw clean package -DskipTests

TOMCAT_HOME="${TOMCAT_HOME:-$PWD/.tools/apache-tomcat-10.1.61}"


mkdir -p "$TOMCAT_HOME/webapps"
cp target/projetJEE-1.0-SNAPSHOT.war "$TOMCAT_HOME/webapps/projetJEE.war"
exec "$TOMCAT_HOME/bin/catalina.sh" run

#!http://localhost:8080/projetJEE/
