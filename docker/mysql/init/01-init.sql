DROP DATABASE IF EXISTS Encuentralo;
CREATE DATABASE Encuentralo;
USE Encuentralo;

CREATE TABLE usuario
(
    id_usuario INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NULL,
    rol VARCHAR(255) NOT NULL
);

CREATE TABLE categoria(
    id_categoria INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(255) NOT NULL
);

CREATE TABLE etiqueta(
    id_etiqueta INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    id_usuario INT UNSIGNED NOT NULL
);

CREATE TABLE busqueda(
    id_busqueda INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    termino_busqueda VARCHAR(255) NOT NULL,
    fecha_busqueda DATETIME NOT NULL,
    id_usuario INT UNSIGNED NOT NULL,
    ciudad VARCHAR(255) NOT NULL
);

CREATE TABLE resultados(
    id_resultado INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    telefono VARCHAR(255) NOT NULL,
    distancia FLOAT NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    valoracion FLOAT NOT NULL,
    num_reviews INT UNSIGNED NOT NULL,
    url VARCHAR(255) NOT NULL,
    es_favorito BOOLEAN NOT NULL,
    id_usuario INT UNSIGNED NOT NULL,
    id_busqueda INT UNSIGNED NOT NULL
);

CREATE TABLE resultados_categoria(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_resultado INT UNSIGNED NOT NULL,
    id_categoria INT UNSIGNED NOT NULL
);

CREATE TABLE etiqueta_resultados(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_etiqueta INT UNSIGNED NOT NULL,
    id_resultado INT UNSIGNED NOT NULL
);

ALTER TABLE etiqueta
    ADD CONSTRAINT etiqueta_id_usuario_foreign 
    FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario);

ALTER TABLE busqueda
    ADD CONSTRAINT busqueda_id_usuario_foreign 
    FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario);

ALTER TABLE resultados
    ADD CONSTRAINT resultados_id_usuario_foreign 
    FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario);

ALTER TABLE resultados
    ADD CONSTRAINT resultados_id_busqueda_foreign 
    FOREIGN KEY(id_busqueda) REFERENCES busqueda(id_busqueda);

ALTER TABLE resultados_categoria
    ADD CONSTRAINT resultados_categoria_id_resultado_foreign 
    FOREIGN KEY(id_resultado) REFERENCES resultados(id_resultado);

ALTER TABLE resultados_categoria
    ADD CONSTRAINT resultados_categoria_id_categoria_foreign 
    FOREIGN KEY(id_categoria) REFERENCES categoria(id_categoria);

ALTER TABLE etiqueta_resultados
    ADD CONSTRAINT etiqueta_resultados_id_etiqueta_foreign 
    FOREIGN KEY(id_etiqueta) REFERENCES etiqueta(id_etiqueta);

ALTER TABLE etiqueta_resultados
    ADD CONSTRAINT etiqueta_resultados_id_resultado_foreign 
    FOREIGN KEY(id_resultado) REFERENCES resultados(id_resultado);