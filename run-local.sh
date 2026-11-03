#!/usr/bin/env bash
set -Eeuo pipefail

PROJECT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

export JAVA_HOME="${JAVA_HOME:-$PROJECT_DIR/OpenJDK25U-jdk_x64_linux_hotspot_25.0.5_8/jdk-25.0.5+8}"
export PATH="$JAVA_HOME/bin:$PATH"
export DB_URL="${DB_URL:-jdbc:mysql://localhost:3306/school_data}"

if [[ ! -x "$JAVA_HOME/bin/java" ]]; then
    printf 'JDK introuvable : %s\n' "$JAVA_HOME" >&2
    exit 1
fi

TOMCAT_HOME="${TOMCAT_HOME:-$PROJECT_DIR/.tools/apache-tomcat-10.1.61}"
TOMCAT_BIN="$TOMCAT_HOME/bin/catalina.sh"
WAR_SOURCE="$PROJECT_DIR/target/projetJEE-1.0-SNAPSHOT.war"
WAR_TARGET="$TOMCAT_HOME/webapps/projetJEE.war"

if [[ ! -x "$TOMCAT_BIN" ]]; then
    printf 'Tomcat introuvable : %s\n' "$TOMCAT_HOME" >&2
    printf 'Définissez TOMCAT_HOME avec le chemin de votre installation.\n' >&2
    exit 1
fi

bash ./mvnw clean package -DskipTests

if [[ ! -f "$WAR_SOURCE" ]]; then
    printf 'WAR généré introuvable : %s\n' "$WAR_SOURCE" >&2
    exit 1
fi

mkdir -p "$TOMCAT_HOME/webapps"
cp -- "$WAR_SOURCE" "$WAR_TARGET"

printf 'Application disponible sur http://localhost:8080/projetJEE/\n'
exec "$TOMCAT_BIN" run
