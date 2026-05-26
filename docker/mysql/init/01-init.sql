USE Encuentralo;

-- Tablas base (sin dependencias)
CREATE TABLE usuario (
    id_usuario INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NULL,
    rol VARCHAR(255) NOT NULL
);

CREATE TABLE categoria (
    id_categoria INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(255) NOT NULL
);

-- Tablas con dependencias (FOREIGN KEYS integradas)
CREATE TABLE etiqueta (
    id_etiqueta INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    id_usuario INT UNSIGNED NOT NULL,
    CONSTRAINT etiqueta_id_usuario_foreign FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE busqueda (
    id_busqueda INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    termino_busqueda VARCHAR(255) NOT NULL,
    fecha_busqueda DATETIME NOT NULL,
    id_usuario INT UNSIGNED NOT NULL,
    ciudad VARCHAR(255) NOT NULL,
    CONSTRAINT busqueda_id_usuario_foreign FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE resultados (
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
    id_busqueda INT UNSIGNED NOT NULL,
    CONSTRAINT resultados_id_usuario_foreign FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT resultados_id_busqueda_foreign FOREIGN KEY(id_busqueda) REFERENCES busqueda(id_busqueda)
);

CREATE TABLE resultados_categoria (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_resultado INT UNSIGNED NOT NULL,
    id_categoria INT UNSIGNED NOT NULL,
    CONSTRAINT resultados_categoria_id_resultado_foreign FOREIGN KEY(id_resultado) REFERENCES resultados(id_resultado),
    CONSTRAINT resultados_categoria_id_categoria_foreign FOREIGN KEY(id_categoria) REFERENCES categoria(id_categoria)
);

CREATE TABLE etiqueta_resultados (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_etiqueta INT UNSIGNED NOT NULL,
    id_resultado INT UNSIGNED NOT NULL,
    CONSTRAINT etiqueta_resultados_id_etiqueta_foreign FOREIGN KEY(id_etiqueta) REFERENCES etiqueta(id_etiqueta),
    CONSTRAINT etiqueta_resultados_id_resultado_foreign FOREIGN KEY(id_resultado) REFERENCES resultados(id_resultado)
);