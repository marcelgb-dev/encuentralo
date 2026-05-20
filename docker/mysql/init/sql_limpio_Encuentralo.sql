DROP DATABASE IF EXISTS Encuentralo;
CREATE DATABASE Encuentralo;
USE Encuentralo;

CREATE TABLE Usuario(
    id_usuario INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NULL,
    rol VARCHAR(255) NOT NULL
);

CREATE TABLE Categoria(
    id_categoria INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(255) NOT NULL
);

CREATE TABLE Etiqueta(
    id_etiqueta INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    id_usuario INT UNSIGNED NOT NULL
);

CREATE TABLE Busqueda(
    id_busqueda INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    termino_busqueda VARCHAR(255) NOT NULL,
    fecha_busqueda DATETIME NOT NULL,
    id_usuario INT UNSIGNED NOT NULL,
    ciudad VARCHAR(255) NOT NULL
);

CREATE TABLE Resultados(
    id_resultado INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    telefono VARCHAR(255) NOT NULL,
    distancia FLOAT NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    valoracion FLOAT NOT NULL,
    url VARCHAR(255) NOT NULL,
    esFavorito BOOLEAN NOT NULL,
    id_usuario INT UNSIGNED NOT NULL,
    id_busqueda INT UNSIGNED NOT NULL
);

ALTER TABLE Resultados ADD UNIQUE resultados_url_unique(url);

CREATE TABLE Resultados_Categoria(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_resultado INT UNSIGNED NOT NULL,
    id_categoria INT UNSIGNED NOT NULL
);

CREATE TABLE Etiqueta_Resultados(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_etiqueta INT UNSIGNED NOT NULL,
    id_resultado INT UNSIGNED NOT NULL
);

ALTER TABLE Etiqueta 
    ADD CONSTRAINT etiqueta_id_usuario_foreign 
    FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario);

ALTER TABLE Busqueda 
    ADD CONSTRAINT busqueda_id_usuario_foreign 
    FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario);

ALTER TABLE Resultados 
    ADD CONSTRAINT resultados_id_usuario_foreign 
    FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario);

ALTER TABLE Resultados 
    ADD CONSTRAINT resultados_id_busqueda_foreign 
    FOREIGN KEY(id_busqueda) REFERENCES Busqueda(id_busqueda);

ALTER TABLE Resultados_Categoria 
    ADD CONSTRAINT resultados_categoria_id_resultado_foreign 
    FOREIGN KEY(id_resultado) REFERENCES Resultados(id_resultado);

ALTER TABLE Resultados_Categoria 
    ADD CONSTRAINT resultados_categoria_id_categoria_foreign 
    FOREIGN KEY(id_categoria) REFERENCES Categoria(id_categoria);

ALTER TABLE Etiqueta_Resultados 
    ADD CONSTRAINT etiqueta_resultados_id_etiqueta_foreign 
    FOREIGN KEY(id_etiqueta) REFERENCES Etiqueta(id_etiqueta);

ALTER TABLE Etiqueta_Resultados 
    ADD CONSTRAINT etiqueta_resultados_id_resultado_foreign 
    FOREIGN KEY(id_resultado) REFERENCES Resultados(id_resultado);