-- 04_verificacion.sql — Comprobación del estado de la base de datos
-- Ejecutar con: sudo mysql biblioteca < ~/sql/04_verificacion.sql

SELECT 'Autores' AS entidad, COUNT(*) AS total FROM autor
UNION ALL
SELECT 'Libros',  COUNT(*) FROM libro
UNION ALL
SELECT 'Socios',  COUNT(*) FROM socio
UNION ALL
SELECT 'Préstamos', COUNT(*) FROM prestamo;

SELECT
    SUM(fecha_devolucion IS NOT NULL) AS devueltos,
    SUM(fecha_devolucion IS NULL AND fecha_limite >= CURDATE()) AS activos,
    SUM(fecha_devolucion IS NULL AND fecha_limite < CURDATE()) AS vencidos
FROM prestamo;
