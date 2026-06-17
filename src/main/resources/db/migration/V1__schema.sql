CREATE TABLE IF NOT EXISTS rol (
    id     INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categoria_docente (
    id     INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS estado_inscripcion (
    id     INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuario_academico (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre     TEXT    NOT NULL,
    legajo     TEXT    NOT NULL UNIQUE,
    email      TEXT    NOT NULL UNIQUE,
    rol_id     INTEGER NOT NULL,
    activo     INTEGER NOT NULL DEFAULT 1 CHECK (activo IN (0, 1)),
    created_at TEXT    NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT    NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (rol_id) REFERENCES rol(id),
    CHECK (length(nombre) > 0),
    CHECK (length(legajo) > 0)
);

CREATE INDEX IF NOT EXISTS idx_usuario_rol ON usuario_academico(rol_id);

CREATE TABLE IF NOT EXISTS estudiante (
    id           INTEGER PRIMARY KEY,
    carrera      TEXT    NOT NULL,
    anio_ingreso INTEGER NOT NULL CHECK (anio_ingreso BETWEEN 1900 AND 2099),
    FOREIGN KEY (id) REFERENCES usuario_academico(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS docente (
    id            INTEGER PRIMARY KEY,
    departamento  TEXT    NOT NULL,
    categoria_id  INTEGER NOT NULL,
    FOREIGN KEY (id)           REFERENCES usuario_academico(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categoria_docente(id),
    CHECK (length(departamento) > 0)
);

CREATE INDEX IF NOT EXISTS idx_docente_categoria ON docente(categoria_id);

CREATE TABLE IF NOT EXISTS administrativo (
    id     INTEGER PRIMARY KEY,
    sector TEXT    NOT NULL,
    FOREIGN KEY (id) REFERENCES usuario_academico(id) ON DELETE CASCADE,
    CHECK (length(sector) > 0)
);

CREATE TABLE IF NOT EXISTS materia (
    id     INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT    NOT NULL,
    codigo TEXT    NOT NULL UNIQUE,
    anio   INTEGER NOT NULL CHECK (anio BETWEEN 1 AND 10),
    CHECK (length(nombre) > 0)
);

CREATE TABLE IF NOT EXISTS docente_materia (
    docente_id INTEGER NOT NULL,
    materia_id INTEGER NOT NULL,
    PRIMARY KEY (docente_id, materia_id),
    FOREIGN KEY (docente_id) REFERENCES docente(id)  ON DELETE CASCADE,
    FOREIGN KEY (materia_id) REFERENCES materia(id)  ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_docente_materia_materia ON docente_materia(materia_id);

CREATE TABLE IF NOT EXISTS inscripcion (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    estudiante_id INTEGER NOT NULL,
    materia_id    INTEGER NOT NULL,
    fecha         TEXT    NOT NULL,
    estado_id     INTEGER NOT NULL,
    nota_final    REAL    DEFAULT NULL CHECK (nota_final IS NULL OR nota_final BETWEEN 0 AND 10),
    UNIQUE (estudiante_id, materia_id),
    FOREIGN KEY (estudiante_id) REFERENCES estudiante(id) ON DELETE CASCADE,
    FOREIGN KEY (materia_id)    REFERENCES materia(id),
    FOREIGN KEY (estado_id)     REFERENCES estado_inscripcion(id)
);

CREATE INDEX IF NOT EXISTS idx_inscripcion_estudiante ON inscripcion(estudiante_id);
CREATE INDEX IF NOT EXISTS idx_inscripcion_materia    ON inscripcion(materia_id);
CREATE INDEX IF NOT EXISTS idx_inscripcion_estado     ON inscripcion(estado_id);

CREATE TABLE IF NOT EXISTS log_acceso (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER NOT NULL,
    fecha_hora TEXT    NOT NULL DEFAULT (datetime('now')),
    portal     TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuario_academico(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_log_acceso_usuario ON log_acceso(usuario_id);
