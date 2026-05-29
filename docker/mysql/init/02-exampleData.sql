USE Encuentralo;

-- 1. Usuarios (Password es 'password123' en BCrypt)
INSERT INTO usuario (email, password, nombre, apellidos, rol) VALUES
                                                                  ('admin@example.com', '$2a$10$qfi5IOeaF1qvIE57i2z1EuFhFgXW6222L0Mu14JD/b82ai11w/Mgq', 'Admin', 'Example', 'ADMIN'),
                                                                  ('user@example.com', '$2a$10$qfi5IOeaF1qvIE57i2z1EuFhFgXW6222L0Mu14JD/b82ai11w/Mgq', 'User', 'Example', 'USER');