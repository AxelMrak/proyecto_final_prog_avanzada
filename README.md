# Sistema de Gestion Universitaria

Aplicacion de consola en Java 17 con SQLite -- Proyecto final de **Programacion Avanzada**, Universidad del Aconcagua.

---

## Informe del proyecto (PDF)

**Leer el informe completo: [informes/INFORME.pdf](informes/INFORME.pdf)**

El informe en LaTeX, sus archivos auxiliares y el PDF compilado viven en la carpeta [`informes/`](informes/). Para recompilarlo:

```bash
cd informes
pdflatex INFORME.tex
pdflatex INFORME.tex   # segunda pasada para el indice
```

---

## Sobre el sistema

Sistema de linea de comandos para administrar alumnos, docentes,
administrativos, materias e inscripciones de una universidad. Permite dar
de alta usuarios, inscribir alumnos en materias y que los docentes carguen
notas finales.

- **Stack:** Java 17, Maven 3, SQLite (JDBC)
- **Arquitectura:** 4 capas (domain / application / infrastructure / ui)
- **Base de datos:** esquema normalizado con migraciones automaticas
- **Validacion:** por campo, con re-prompt en caso de error

## Funcionalidades

| Modulo           | Operaciones                         |
|------------------|-------------------------------------|
| Alumnos          | Listar, agregar, inscribir a materia |
| Docentes         | Listar, agregar, cargar nota         |
| Administrativos  | Listar, agregar                      |

Al inscribir o cargar nota, el sistema muestra listas numeradas de alumnos
y materias para elegir sin necesidad de memorizar legajos o codigos.

## Como ejecutar

```bash
# 1. Compilar y empaquetar (requiere JDK 17+ y Maven 3.6+)
mvn clean package

# 2. Ejecutar
java -jar target/LSP_Universidad-1.0-SNAPSHOT.jar
```

La primera ejecucion crea automaticamente la base de datos SQLite y la
puebla con datos semilla.

## Tests

```bash
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "target/classes:target/test-classes:$(cat cp.txt)" \
    universidad.TestValidacionesYNota
```

25 tests de integracion cubren validadores, constructores, carga de nota y
persistencia.

## Estructura del proyecto

```
├── pom.xml
├── README.md
├── informes/
│   ├── INFORME.tex
│   └── INFORME.pdf
├── Recursos/
│   ├── demo.mov / .mp4
│   ├── demo-codigo-y-estructura.mov / .mp4
│   └── ... (capturas de interaccion)
├── Diagrams/
│   ├── diagramaa.png
│   └── mermaid-diagram-*.png
└── src/
    ├── main/
    │   ├── java/universidad/
    │   │   ├── Main.java
    │   │   ├── InputReader.java
    │   │   ├── Validador.java
    │   │   ├── domain/              # Entidades, value objects, interfaces
    │   │   │   ├── UsuarioAcademico.java
    │   │   │   ├── Estudiante.java
    │   │   │   ├── Docente.java
    │   │   │   ├── Administrativo.java
    │   │   │   ├── Materia.java
    │   │   │   ├── Inscripcion.java
    │   │   │   ├── Nota.java
    │   │   │   ├── NegocioException.java
    │   │   │   ├── Rol.java
    │   │   │   ├── CategoriaDocente.java
    │   │   │   ├── EstadoInscripcion.java
    │   │   │   └── repository/
    │   │   ├── application/         # Servicios de caso de uso
    │   │   │   ├── EstudianteService.java
    │   │   │   ├── DocenteService.java
    │   │   │   ├── AdministrativoService.java
    │   │   │   └── InscripcionService.java
    │   │   ├── infrastructure/      # JDBC, migraciones
    │   │   │   ├── migration/
    │   │   │   │   └── Migrator.java
    │   │   │   └── persistence/
    │   │   │       ├── ConnectionFactory.java
    │   │   │       └── ...RepositoryJdbc.java (5 archivos)
    │   │   └── ui/menu/             # Menus de consola
    │   │       ├── MenuAlumnos.java
    │   │       ├── MenuDocentes.java
    │   │       └── MenuAdministrativos.java
    │   └── resources/db/migration/
    │       ├── V1__schema.sql       # DDL normalizado
    │       └── V2__seed.sql         # Datos semilla
    └── test/java/universidad/
        └── TestValidacionesYNota.java
```

## Repositorio

[https://github.com/AxelMrak/proyecto_final_prog_avanzada](https://github.com/AxelMrak/proyecto_final_prog_avanzada)

## Integrantes

- Axel Sarmiento
- Julian Molina
- Andres Riveros

**Catedra:** Programacion Avanzada  ·  **Facultad:** Ciencias Sociales y Administrativas  ·  **Universidad del Aconcagua**
