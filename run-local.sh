#!/bin/bash

# Configuration Java
export JAVA_HOME="$PWD/OpenJDK25U-jdk_x64_linux_hotspot_25.0.5_8/jdk-25.0.5+8"
export PATH="$JAVA_HOME/bin:$PATH"

# Configuration MySQL
export DB_URL="jdbc:mysql://localhost:3306/school_data"

# Configuration Tomcat
TOMCAT_HOME="$PWD/.tools/apache-tomcat-10.1.61"

# Compilation du projet
bash ./mvnw clean package -DskipTests || exit 1

# Copie du WAR dans Tomcat
cp target/projetJEE-1.0-SNAPSHOT.war "$TOMCAT_HOME/webapps/projetJEE.war" || exit 1

# Démarrage de Tomcat
exec "$TOMCAT_HOME/bin/catalina.sh" run