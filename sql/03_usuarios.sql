-- 03_usuarios.sql — Cuentas de MySQL para la biblioteca
-- Ejecutar con: sudo mysql < ~/sql/03_usuarios.sql
-- ANTES: sustituye las dos contraseñas por las tuyas (mín. 12 caracteres).
-- DESPUÉS: shred -u ~/sql/03_usuarios.sql

USE biblioteca;

-- Cuenta de la aplicación: solo DML sobre el esquema biblioteca
CREATE USER IF NOT EXISTS 'biblioteca_app'@'localhost'
    IDENTIFIED BY '<CONTRASEÑA_APP>';
GRANT SELECT, INSERT, UPDATE, DELETE ON biblioteca.* TO 'biblioteca_app'@'localhost';

-- Cuenta de administración: todos los privilegios sobre el esquema
CREATE USER IF NOT EXISTS 'biblioteca_admin'@'localhost'
    IDENTIFIED BY '<CONTRASEÑA_ADMIN>';
GRANT ALL PRIVILEGES ON biblioteca.* TO 'biblioteca_admin'@'localhost';

FLUSH PRIVILEGES;
