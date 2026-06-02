USE Encuentralo;

-- 1. Usuarios (Password es '1234' en BCrypt)
INSERT INTO usuario (email, password, nombre, apellidos, rol) VALUES
                                                                  ('admin@example.com', '$2a$10$nyq9.f/vet24pdzrIvgXyeEXwttkvOGEdnMMZ9KtEL7oiHQa59UjS', 'Admin', 'Example', 'ADMIN'),
                                                                  ('user@example.com', '$2a$10$nyq9.f/vet24pdzrIvgXyeEXwttkvOGEdnMMZ9KtEL7oiHQa59UjS', 'User', 'Example', 'USER');