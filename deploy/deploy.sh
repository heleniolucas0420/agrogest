#!/bin/bash
# Compila o projecto e publica o WAR no Tomcat (Linux).
# Uso: ./deploy/deploy.sh            (Tomcat em /opt/tomcat)
#      TOMCAT=/outro/caminho ./deploy/deploy.sh
set -e

TOMCAT="${TOMCAT:-/opt/tomcat}"
cd "$(dirname "$0")/.."

echo ">> A compilar com Maven..."
mvn -q clean package

echo ">> A parar o Tomcat (grava o estado em ficheiro)..."
sudo systemctl stop tomcat || true

echo ">> A copiar o WAR..."
sudo rm -rf "$TOMCAT/webapps/agrogest" "$TOMCAT/webapps/agrogest.war"
sudo cp target/agrogest.war "$TOMCAT/webapps/"
sudo chown tomcat:tomcat "$TOMCAT/webapps/agrogest.war"

echo ">> A arrancar o Tomcat..."
sudo systemctl start tomcat

echo ">> Pronto: http://localhost:8081/agrogest"
