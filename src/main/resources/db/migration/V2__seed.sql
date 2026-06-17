INSERT OR IGNORE INTO rol (id, nombre) VALUES
    (1, 'ESTUDIANTE'),
    (2, 'DOCENTE'),
    (3, 'ADMINISTRATIVO');

INSERT OR IGNORE INTO categoria_docente (id, nombre) VALUES
    (1, 'Titular'),
    (2, 'Adjunto'),
    (3, 'Jefe de Trabajos Practicos'),
    (4, 'Ayudante');

INSERT OR IGNORE INTO estado_inscripcion (id, nombre) VALUES
    (1, 'ACTIVA'),
    (2, 'APROBADA'),
    (3, 'DESAPROBADA'),
    (4, 'ABANDONADA');

INSERT OR IGNORE INTO usuario_academico (id, nombre, legajo, email, rol_id) VALUES
    (1, 'Juan Perez',   'EST001', 'juan@universidad.edu',   1),
    (2, 'Maria Garcia', 'EST002', 'maria@universidad.edu',  1),
    (3, 'Carlos Lopez', 'DOC001', 'carlos@universidad.edu', 2),
    (4, 'Ana Martinez', 'ADM001', 'ana@universidad.edu',    3);

INSERT OR IGNORE INTO estudiante (id, carrera, anio_ingreso) VALUES
    (1, 'Ingenieria en Sistemas',     2023),
    (2, 'Licenciatura en Matematica', 2022);

INSERT OR IGNORE INTO docente (id, departamento, categoria_id) VALUES
    (3, 'Departamento de Informatica', 1);

INSERT OR IGNORE INTO administrativo (id, sector) VALUES
    (4, 'Mesa de Entradas');

INSERT OR IGNORE INTO materia (id, nombre, codigo, anio) VALUES
    (1, 'Programacion I',  'INF-101', 1),
    (2, 'Base de Datos',   'INF-201', 2),
    (3, 'Programacion II', 'INF-301', 2);

INSERT OR IGNORE INTO inscripcion (estudiante_id, materia_id, fecha, estado_id) VALUES
    (1, 1, '2026-03-01', 1),
    (1, 2, '2026-03-01', 1),
    (2, 3, '2026-03-01', 1);

INSERT OR IGNORE INTO docente_materia (docente_id, materia_id) VALUES
    (3, 1),
    (3, 2);

INSERT OR IGNORE INTO log_acceso (usuario_id, portal) VALUES
    (1, 'Portal del Alumno'),
    (2, 'Portal Docente'),
    (3, 'Panel Administrativo');
