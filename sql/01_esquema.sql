-- 01_esquema.sql — Esquema de la base de datos biblioteca
-- Ejecutar con: sudo mysql < ~/sql/01_esquema.sql

CREATE DATABASE IF NOT EXISTS biblioteca
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE biblioteca;

CREATE TABLE autor (
    id              INT             AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(120)    NOT NULL,
    nacionalidad    VARCHAR(60),
    anio_nacimiento SMALLINT
) ENGINE=InnoDB;

CREATE TABLE libro (
    id                INT             AUTO_INCREMENT PRIMARY KEY,
    isbn              CHAR(13)        NOT NULL UNIQUE,
    titulo            VARCHAR(200)    NOT NULL,
    anio_publicacion  SMALLINT,
    genero            VARCHAR(40),
    ejemplares        SMALLINT UNSIGNED NOT NULL DEFAULT 1,
    autor_id          INT             NOT NULL,
    CONSTRAINT fk_libro_autor FOREIGN KEY (autor_id) REFERENCES autor(id)
) ENGINE=InnoDB;

CREATE TABLE socio (
    id          INT             AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(120)    NOT NULL,
    email       VARCHAR(160)    NOT NULL UNIQUE,
    fecha_alta  DATE            NOT NULL
) ENGINE=InnoDB;

CREATE TABLE prestamo (
    id                  INT     AUTO_INCREMENT PRIMARY KEY,
    libro_id            INT     NOT NULL,
    socio_id            INT     NOT NULL,
    fecha_prestamo      DATE    NOT NULL,
    fecha_limite        DATE    NOT NULL,
    fecha_devolucion    DATE,
    CONSTRAINT fk_prestamo_libro FOREIGN KEY (libro_id) REFERENCES libro(id),
    CONSTRAINT fk_prestamo_socio FOREIGN KEY (socio_id) REFERENCES socio(id)
) ENGINE=InnoDB;
