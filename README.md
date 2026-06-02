# Encuéntralo

<p align="center">
  <img src="https://img.shields.io/badge/Java_21-ED8B00?logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring_Boot_4.0.6-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4.0.6">
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?logo=springsecurity&logoColor=white" alt="Spring Security">
  <img src="https://img.shields.io/badge/MySQL_8.0-4479A1?logo=mysql&logoColor=white" alt="MySQL 8.0">
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=white" alt="Thymeleaf">
  <img src="https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white" alt="Docker">
  <img src="https://img.shields.io/badge/Nginx-009639?logo=nginx&logoColor=white" alt="Nginx">
  <img src="https://img.shields.io/badge/Yelp_Fusion-FF1A1A?logo=yelp&logoColor=white" alt="Yelp Fusion API">
  <img src="https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white" alt="Maven">
</p>

Aplicación web de prospección comercial que consume la API de Yelp Fusion para buscar negocios, almacenarlos en MySQL y permitir filtrar, ordenar, marcar como favoritos, etiquetar y exportar resultados. Incluye panel de administración para gestión de usuarios.

---

## Funcionalidades

- **Búsqueda Yelp** — Busca negocios por término y ubicación con radio ajustable
- **Filtros combinados** — Filtra por categorías, etiquetas, valoración mínima y favoritos
- **Ordenación** — Ordena por valoración, distancia o nombre, en ambos sentidos
- **Favoritos** — Marca resultados individuales para consultarlos después
- **Etiquetas personalizadas** — Crea y asigna tus propias etiquetas a los resultados
- **Exportación CSV** — Descarga los resultados filtrados con BOM UTF-8
- **Historial** — Consulta búsquedas anteriores
- **Panel admin** — Gestión completa de usuarios (CRUD, roles)

---

## Índice

- [Requisitos previos](#requisitos-previos)
- [Stack técnico](#stack-técnico)
- [Instalación y despliegue](#instalación-y-despliegue)
  - [1. Configurar variables de entorno](#1-configurar-variables-de-entorno)
  - [2. Configurar Nginx](#2-configurar-nginx-opcional)
  - [3. Arrancar la aplicación](#3-arrancar-la-aplicación)
  - [4. Usuarios por defecto](#4-usuarios-por-defecto)
  - [5. Desarrollo local](#5-desarrollo-local-sin-docker)
- [Arquitectura](#arquitectura)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Licencia](#licencia)

---

## Requisitos previos

- **Docker y Docker Compose** — [Guía oficial de instalación](https://docs.docker.com/engine/install/)
- **Java 21** (solo para desarrollo local sin Docker)
- **Clave de API de Yelp Fusion** — [gratuita en Yelp Developers](https://www.yelp.com/developers)

## Stack técnico

| Capa | Tecnología |
|------|------------|
| Backend | Spring Boot 4.0.6, Spring MVC, Spring Data JPA, Spring Security 6 |
| Frontend | Thymeleaf, HTML semántico, CSS plano (sin frameworks), Material Symbols |
| Base de datos | MySQL 8.0 (esquema gestionado externamente, `ddl-auto: none`) |
| API externa | Yelp Fusion API v3 (mokeable con `MOCK_JSON=true`) |
| CSV | OpenCSV 5.9 |
| Contenedores | Docker Compose (MySQL + Spring Boot + Nginx) |
| Build | Java 21, Maven (wrapper `./mvnw`) |

## Instalación y despliegue

### 1. Configurar variables de entorno

Copia el archivo de ejemplo y completa los valores:

```bash
cp .env.example .env
```

Edita `.env` con tus datos:

| Variable | Descripción |
|----------|-------------|
| `YELP_API_KEY` | Tu clave de Yelp Fusion API |
| `MAX_RESULTS` | Límite de resultados por búsqueda (por defecto 20) |
| `DB_ROOT_PASSWORD` | Contraseña del usuario root de MySQL |
| `MOCK_JSON` | `true` para usar datos mock sin llamar a la API real |
| `HOST_PORT` | Puerto HTTP del host para Nginx (por defecto 80) |
| `HOST_PORT_SSL` | Puerto HTTPS del host para Nginx (por defecto 443) |
| `SERVER_TIMEZONE` | Zona horaria del servidor (ej: `Europe/Madrid`) |

> **Importante**: Asegúrate de que `SERVER_TIMEZONE` esté definida, pues es necesaria tanto para la conexión JDBC como para la JVM.

### 2. Configurar Nginx (opcional)

El proxy inverso de Nginx se configura en `docker/nginx/conf.d/local.conf`. Por defecto redirige el tráfico del puerto 80 al contenedor de Spring Boot.

Si necesitas SSL, añade tu configuración HTTPS en ese mismo archivo y coloca los certificados en `docker/nginx/certs/`.

### 3. Arrancar la aplicación

```bash
docker compose up -d
```

Esto inicia tres servicios:

1. **db** — MySQL 8.0 con el esquema y datos de ejemplo
2. **app** — Spring Boot. Se compila y empaqueta como JAR dentro del contenedor usando un Dockerfile multietapa (Maven → JRE Alpine)
3. **proxy** — Nginx como proxy inverso

La aplicación estará disponible en `http://localhost`.

### 4. Usuarios por defecto

| Email | Contraseña | Rol |
|-------|-----------|-----|
| `admin@example.com` | `1234` | ADMIN |
| `user@example.com` | `1234` | USER |

### 5. Desarrollo local (sin Docker)

Para arrancar solo la base de datos:

```bash
./start-testdb.sh
```

Esto levanta MySQL en el puerto `5555` con los scripts de inicialización.

Luego ejecuta la aplicación Spring Boot:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Arquitectura

Consulta [`Arquitectura.md`](Arquitectura.md) para una descripción detallada del diseño interno: estructura de paquetes, modelo de datos, flujos de navegación, sistema de vistas (móvil/escritorio), seguridad y endpoints.

## Estructura del proyecto

```
├── docker/
│   ├── mysql/init/          # Scripts SQL de inicialización
│   ├── nginx/conf.d/        # Configuración del proxy
│   └── ...
├── src/
│   └── main/
│       ├── java/.../encuentralo/
│       │   ├── config/      # Seguridad, login, Yelp client
│       │   ├── controller/  # Controladores MVC
│       │   ├── dto/         # Objetos de transferencia
│       │   ├── model/       # Entidades JPA
│       │   ├── repository/  # Acceso a datos
│       │   └── service/     # Lógica de negocio
│       └── resources/
│           ├── static/      # CSS, JS, imágenes
│           └── templates/   # Plantillas Thymeleaf
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── .env.example
```

## Licencia

Pendiente de definir.
