#!/bin/bash

# Configuración de variables
CONTAINER_NAME="encuentralo-db-test"
DB_PASSWORD="1234"
# Ruta a los datos persistentes
DATA_FOLDER="$(pwd)/docker/mysql/data"
# Ruta a los scripts SQL (01-schema y 02-data)
INIT_FOLDER="$(pwd)/docker/mysql/init"
DB_NAME="ENCUENTRALO_DB"
NETWORK_NAME="encuentralo-test-network"

echo "🚀 Iniciando entorno de base de datos para Encuentralo..."

# 1. Crear la red si no existe
if [ ! "$(docker network ls | grep $NETWORK_NAME)" ]; then
  echo "🌐 Creating docker network: $NETWORK_NAME"
  docker network create $NETWORK_NAME
fi

# 1. Comprobar si el contenedor ya existe y eliminarlo
if [ "$(docker ps -aq -f name=${CONTAINER_NAME})" ]; then
    echo "⚠️ Deteniendo y eliminando contenedor previo..."
    docker rm -f ${CONTAINER_NAME} > /dev/null
fi

# 2. Crear las carpetas si no existen (para evitar que Docker las cree como root)
mkdir -p "${DATA_FOLDER}"
mkdir -p "${INIT_FOLDER}"

# 3. Arrancar el contenedor
docker run -d \
  --name ${CONTAINER_NAME} \
  --network $NETWORK_NAME \
  -p 3306:3306 \
  -e TZ=Europe/Madrid \
  -e MYSQL_ROOT_PASSWORD=${DB_PASSWORD} \
  -e MYSQL_DATABASE=${DB_NAME} \
  -v "${DATA_FOLDER}":/var/lib/mysql \
  -v "${INIT_FOLDER}":/docker-entrypoint-initdb.d \
  mysql:8.0-oracle \
  --character-set-server=utf8mb4 \
  --collation-server=utf8mb4_unicode_ci \
  --skip-character-set-client-handshake

echo "✅ Contenedor arrancado"
docker ps -f name=${CONTAINER_NAME}
