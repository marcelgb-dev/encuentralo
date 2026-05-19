-- Código inicial de creación de la Base de Datos
CREATE DATABASE IF NOT EXISTS encuentralo;

CREATE TABLE Busqueda(
                         id_busqueda INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         termino_busqueda VARCHAR(255) NOT NULL,
                         latitud DECIMAL(10, 8) NOT NULL,
                         longitud DECIMAL(10, 8) NOT NULL,
                         fecha_busqueda DATETIME NOT NULL
);

CREATE TABLE Resultados(
                           id_resultado INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           place_id VARCHAR(255) NOT NULL,
                           nombre VARCHAR(255) NOT NULL,
                           telefono VARCHAR(255) NOT NULL,
                           direccion VARCHAR(255) NOT NULL,
                           valoracion FLOAT(53) NOT NULL,
                           UNIQUE (place_id)
);

CREATE TABLE Categoria(
                          id_categoria INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          nombre_categoria VARCHAR(255) NOT NULL
);


CREATE TABLE Usuario(
                        id_usuario INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        nombre VARCHAR(255) NOT NULL,
                        apellidos VARCHAR(255) NULL,
                        id_busqueda_reciente INT UNSIGNED NULL
);

CREATE TABLE Etiqueta(
                         id_etiqueta INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(255) NOT NULL,
                         id_usuario INT UNSIGNED NOT NULL
);


CREATE TABLE Usuarios_Resultados(
                                    esFavorito BOOLEAN NOT NULL DEFAULT FALSE,
                                    id_usuario INT UNSIGNED NOT NULL,
                                    id_resultado INT UNSIGNED NOT NULL,
                                    PRIMARY KEY(id_usuario, id_resultado)
);

CREATE TABLE Resultados_Categoria(
                                     id_resultado INT UNSIGNED NOT NULL,
                                     id_categoria INT UNSIGNED NOT NULL,
                                     PRIMARY KEY(id_resultado, id_categoria)
);

CREATE TABLE Etiqueta_Resultados(
                                    id_etiqueta INT UNSIGNED NOT NULL,
                                    id_resultado INT UNSIGNED NOT NULL,
                                    PRIMARY KEY(id_etiqueta, id_resultado)
);

CREATE TABLE Busqueda_Usuario(
                                 id_busqueda INT UNSIGNED NOT NULL,
                                 id_usuario INT UNSIGNED NOT NULL,
                                 PRIMARY KEY(id_busqueda, id_usuario)
);

CREATE TABLE Busqueda_Resultados(
                                    id_busqueda INT UNSIGNED NOT NULL,
                                    id_resultado INT UNSIGNED NOT NULL,
                                    PRIMARY KEY(id_busqueda, id_resultado)
);


ALTER TABLE Etiqueta
    ADD CONSTRAINT etiqueta_id_usuario_foreign FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario);

ALTER TABLE Usuarios_Resultados
    ADD CONSTRAINT usuarios_resultados_id_usuario_foreign FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario),
    ADD CONSTRAINT usuarios_resultados_id_resultado_foreign FOREIGN KEY(id_resultado) REFERENCES Resultados(id_resultado);

ALTER TABLE Resultados_Categoria
    ADD CONSTRAINT resultados_categoria_id_categoria_foreign FOREIGN KEY(id_categoria) REFERENCES Categoria(id_categoria),
    ADD CONSTRAINT resultados_categoria_id_resultado_foreign FOREIGN KEY(id_resultado) REFERENCES Resultados(id_resultado);

ALTER TABLE Etiqueta_Resultados
    ADD CONSTRAINT etiqueta_resultados_id_etiqueta_foreign FOREIGN KEY(id_etiqueta) REFERENCES Etiqueta(id_etiqueta),
    ADD CONSTRAINT etiqueta_resultados_id_resultado_foreign FOREIGN KEY(id_resultado) REFERENCES Resultados(id_resultado);

ALTER TABLE Busqueda_Usuario
    ADD CONSTRAINT busqueda_usuario_id_busqueda_foreign FOREIGN KEY(id_busqueda) REFERENCES Busqueda(id_busqueda),
    ADD CONSTRAINT busqueda_usuario_id_usuario_foreign FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario);

ALTER TABLE Busqueda_Resultados
    ADD CONSTRAINT busqueda_resultados_id_busqueda_foreign FOREIGN KEY(id_busqueda) REFERENCES Busqueda(id_busqueda),
    ADD CONSTRAINT busqueda_resultados_id_resultado_foreign FOREIGN KEY(id_resultado) REFERENCES Resultados(id_resultado);