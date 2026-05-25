USE Encuentralo;

-- 1. Usuarios (Password es 'password123' en BCrypt)
INSERT INTO usuario (email, password, nombre, apellidos, rol) VALUES
                                                                  ('admin@encuentralo.com', '$2a$10$8.UnVuG9HHgffUDAlk8q6uy5akLPNndzqBzv6Dxv5.S1.p.f1A5m6', 'Marcel', 'Admin', 'ADMIN'),
                                                                  ('user@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8q6uy5akLPNndzqBzv6Dxv5.S1.p.f1A5m6', 'Juan', 'García', 'USER');

-- 2. Categorías
INSERT INTO categoria (nombre_categoria) VALUES
                                             ('Restaurantes'),
                                             ('Talleres Mecánicos'),
                                             ('Gimnasios'),
                                             ('Cafeterías');

-- 3. Etiquetas (Personalizadas por el usuario)
INSERT INTO etiqueta (nombre, id_usuario) VALUES
                                              ('Barato', 1),
                                              ('Premium', 1),
                                              ('Cerca del trabajo', 2);

-- 4. Búsquedas realizadas
INSERT INTO busqueda (termino_busqueda, fecha_busqueda, id_usuario, ciudad) VALUES
                                                                                ('Pizza artesana', '2026-05-25 10:30:00', 1, 'Barcelona'),
                                                                                ('Crossfit', '2026-05-25 12:00:00', 2, 'Madrid');

-- 5. Resultados obtenidos
INSERT INTO resultados (nombre, telefono, distancia, direccion, valoracion, num_reviews, url, es_favorito, id_usuario, id_busqueda) VALUES
                                                                                                                                       ('Pizzería Napolitana', '932112233', 0.5, 'Carrer de Mallorca, 123', 4.8, 150, 'https://napolitana.es', true, 1, 1),
                                                                                                                                       ('Gimnasio Iron', '914445566', 1.2, 'Calle de Atocha, 45', 4.2, 85, 'https://irongym.com', false, 2, 2),
                                                                                                                                       ('Sushi Express', '933009988', 2.1, 'Via Laietana, 5', 3.9, 210, 'https://sushiexpress.es', false, 1, 1);

-- 6. Relación Resultados con Categorías
INSERT INTO resultados_categoria (id_resultado, id_categoria) VALUES
                                                                  (1, 1), -- Pizzería es Restaurante
                                                                  (2, 3), -- Iron es Gimnasio
                                                                  (3, 1); -- Sushi es Restaurante

-- 7. Relación Resultados con Etiquetas
INSERT INTO etiqueta_resultados (id_etiqueta, id_resultado) VALUES
                                                                (1, 1), -- Pizzería es "Barato"
                                                                (2, 1), -- Pizzería es "Premium" (doble etiqueta)
                                                                (3, 2); -- Gimnasio está "Cerca del trabajo"