-- 02_datos.sql — Datos ficticios de la biblioteca
-- Ejecutar con: sudo mysql biblioteca < ~/sql/02_datos.sql
-- Las fechas se calculan respecto a CURDATE() para que siempre haya
-- préstamos activos, vencidos y devueltos.

-- 10 autores
INSERT INTO autor (nombre, nacionalidad, anio_nacimiento) VALUES
('Gabriel García Márquez', 'Colombiana', 1927),
('Isabel Allende',        'Chilena',    1942),
('Jorge Luis Borges',     'Argentina',  1899),
('Mario Vargas Llosa',    'Peruana',    1936),
('Julio Cortázar',        'Argentina',  1914),
('Pablo Neruda',          'Chilena',    1904),
('Miguel de Cervantes',   'Española',   1547),
('Federico García Lorca', 'Española',   1898),
('Octavio Paz',           'Mexicana',   1914),
('Rosa Montero',          'Española',   1951);

-- 20 libros
INSERT INTO libro (isbn, titulo, anio_publicacion, genero, ejemplares, autor_id) VALUES
('9780060883287', 'Cien años de soledad',               1967, 'Novela',      3, 1),
('9780061120084', 'El amor en los tiempos del cólera',   1985, 'Novela',      2, 1),
('9780525433781', 'La casa de los espíritus',            1982, 'Novela',      2, 2),
('9780060951832', 'Eva Luna',                            1987, 'Novela',      1, 2),
('9780811216999', 'Ficciones',                           1944, 'Cuento',      3, 3),
('9780142437230', 'El Aleph',                            1949, 'Cuento',      2, 3),
('9780374530556', 'La ciudad y los perros',              1963, 'Novela',      2, 4),
('9780060732936', 'La fiesta del Chivo',                 2000, 'Novela',      1, 4),
('9780375727535', 'Rayuela',                             1963, 'Novela',      3, 5),
('9780811217620', 'Bestiario',                           1951, 'Cuento',      1, 5),
('9780811217699', 'Veinte poemas de amor',               1924, 'Poesía',      2, 6),
('9780374526788', 'Canto general',                       1950, 'Poesía',      1, 6),
('9780060934347', 'Don Quijote de la Mancha',            1605, 'Novela',      4, 7),
('9780142437858', 'Novelas ejemplares',                  1613, 'Novela corta',2, 7),
('9780811216425', 'Romancero gitano',                    1928, 'Poesía',      2, 8),
('9780811217644', 'Bodas de sangre',                     1933, 'Teatro',      1, 8),
('9780811218658', 'El laberinto de la soledad',          1950, 'Ensayo',      2, 9),
('9780811221023', 'Piedra de sol',                       1957, 'Poesía',      1, 9),
('9780307476388', 'La loca de la casa',                  2003, 'Ensayo',      2, 10),
('9780307741714', 'La ridícula idea de no volver a verte',2013,'Ensayo',      1, 10);

-- 8 socios
INSERT INTO socio (nombre, email, fecha_alta) VALUES
('Ana Torres',      'ana.torres@ejemplo.com',      CURDATE() - INTERVAL 120 DAY),
('Carlos Ruiz',     'carlos.ruiz@ejemplo.com',     CURDATE() - INTERVAL 100 DAY),
('Elena Navarro',   'elena.navarro@ejemplo.com',   CURDATE() - INTERVAL 90 DAY),
('David López',     'david.lopez@ejemplo.com',     CURDATE() - INTERVAL 80 DAY),
('Laura Fernández', 'laura.fernandez@ejemplo.com', CURDATE() - INTERVAL 60 DAY),
('Pablo Sánchez',   'pablo.sanchez@ejemplo.com',   CURDATE() - INTERVAL 45 DAY),
('María García',    'maria.garcia@ejemplo.com',    CURDATE() - INTERVAL 30 DAY),
('Javier Martín',   'javier.martin@ejemplo.com',   CURDATE() - INTERVAL 15 DAY);

-- 15 préstamos: 7 devueltos, 5 activos, 3 vencidos
INSERT INTO prestamo (libro_id, socio_id, fecha_prestamo, fecha_limite, fecha_devolucion) VALUES
-- 7 devueltos
(1,  1, CURDATE() - INTERVAL 90 DAY, CURDATE() - INTERVAL 76 DAY, CURDATE() - INTERVAL 78 DAY),
(5,  2, CURDATE() - INTERVAL 80 DAY, CURDATE() - INTERVAL 66 DAY, CURDATE() - INTERVAL 70 DAY),
(9,  3, CURDATE() - INTERVAL 70 DAY, CURDATE() - INTERVAL 56 DAY, CURDATE() - INTERVAL 58 DAY),
(13, 4, CURDATE() - INTERVAL 60 DAY, CURDATE() - INTERVAL 46 DAY, CURDATE() - INTERVAL 50 DAY),
(3,  1, CURDATE() - INTERVAL 50 DAY, CURDATE() - INTERVAL 36 DAY, CURDATE() - INTERVAL 38 DAY),
(11, 5, CURDATE() - INTERVAL 40 DAY, CURDATE() - INTERVAL 26 DAY, CURDATE() - INTERVAL 28 DAY),
(7,  2, CURDATE() - INTERVAL 35 DAY, CURDATE() - INTERVAL 21 DAY, CURDATE() - INTERVAL 22 DAY),
-- 5 activos (dentro de plazo)
(1,  3, CURDATE() - INTERVAL 10 DAY, CURDATE() + INTERVAL 4 DAY, NULL),
(5,  4, CURDATE() - INTERVAL 8 DAY,  CURDATE() + INTERVAL 6 DAY, NULL),
(9,  5, CURDATE() - INTERVAL 5 DAY,  CURDATE() + INTERVAL 9 DAY, NULL),
(13, 6, CURDATE() - INTERVAL 3 DAY,  CURDATE() + INTERVAL 11 DAY, NULL),
(15, 7, CURDATE() - INTERVAL 2 DAY,  CURDATE() + INTERVAL 12 DAY, NULL),
-- 3 vencidos (fecha_limite pasada, sin devolver)
(3,  1, CURDATE() - INTERVAL 25 DAY, CURDATE() - INTERVAL 11 DAY, NULL),
(11, 2, CURDATE() - INTERVAL 20 DAY, CURDATE() - INTERVAL 6 DAY,  NULL),
(17, 8, CURDATE() - INTERVAL 18 DAY, CURDATE() - INTERVAL 4 DAY,  NULL);
