package universidad;

import universidad.application.DocenteService;
import universidad.application.EstudianteService;
import universidad.application.InscripcionService;
import universidad.domain.Estudiante;
import universidad.domain.NegocioException;
import universidad.infrastructure.migration.Migrator;
import universidad.infrastructure.persistence.DocenteRepositoryJdbc;
import universidad.infrastructure.persistence.EstudianteRepositoryJdbc;
import universidad.infrastructure.persistence.InscripcionRepositoryJdbc;
import universidad.infrastructure.persistence.MateriaRepositoryJdbc;

import java.nio.file.Files;
import java.nio.file.Path;

public class TestValidacionesYNota {

    private static int total = 0;
    private static int passed = 0;

    private static void check(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            passed++;
            System.out.println("  PASS  " + nombre);
        } else {
            System.out.println("  FAIL  " + nombre);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Test: Validaciones + Carga de Nota en SQLite ===\n");

        Path db = Path.of("resources/sistema_universitario.db");
        Files.deleteIfExists(db);
        new Migrator().migrar();

        var repoEstudiantes   = new EstudianteRepositoryJdbc();
        var repoDocentes      = new DocenteRepositoryJdbc();
        var repoMaterias      = new MateriaRepositoryJdbc();
        var repoInscripciones = new InscripcionRepositoryJdbc();

        var materiaService    = repoMaterias;
        var inscripciones     = new InscripcionService(repoEstudiantes, repoMaterias, repoInscripciones);
        var estudianteService = new EstudianteService(repoEstudiantes, inscripciones);
        var docenteService    = new DocenteService(repoDocentes, repoEstudiantes, repoMaterias, repoInscripciones);

        System.out.println("[1] Validadores puros");
        check("validarEmail(null)   rechaza",  Validador.validarEmail(null) != null);
        check("validarEmail(\"\")     rechaza",  Validador.validarEmail("") != null);
        check("validarEmail(\"foo\")  rechaza",  Validador.validarEmail("foo") != null);
        check("validarEmail(\"a@b.co\") acepta", Validador.validarEmail("a@b.co") == null);
        check("validarNoVacio(null) rechaza",   Validador.validarNoVacio(null, "X") != null);
        check("validarNoVacio(\"  \") rechaza", Validador.validarNoVacio("   ", "X") != null);
        check("validarNoVacio(\"ok\") acepta",  Validador.validarNoVacio("ok", "X") == null);
        check("validarAnioIngreso(1899) rechaza", Validador.validarAnioIngreso(1899) != null);
        check("validarAnioIngreso(2100) rechaza", Validador.validarAnioIngreso(2100) != null);
        check("validarAnioIngreso(2024) acepta",  Validador.validarAnioIngreso(2024) == null);
        check("validarNota(-1) rechaza",     Validador.validarNota(-1) != null);
        check("validarNota(11) rechaza",     Validador.validarNota(11) != null);
        check("validarNota(7.5) acepta",    Validador.validarNota(7.5) == null);

        System.out.println("\n[2] Constructores del modelo lanzan excepcion");
        check("Estudiante nombre vacio -> excepcion",     lanza(() -> new Estudiante("", "X1", "x@y.com", "C", 2020)));
        check("Estudiante legajo vacio -> excepcion",     lanza(() -> new Estudiante("Ana", "", "x@y.com", "C", 2020)));
        check("Estudiante email invalido -> excepcion",   lanza(() -> new Estudiante("Ana", "X1", "no-es-email", "C", 2020)));
        check("Estudiante anio fuera de rango -> excepcion", lanza(() -> new Estudiante("Ana", "X1", "a@b.co", "C", 1500)));
        check("Estudiante valido -> sin excepcion",         !lanza(() -> new Estudiante("Ana", "X1", "a@b.co", "C", 2020)));

        System.out.println("\n[3] DocenteService.cargarNota: validaciones contra SQLite");
        try {
            docenteService.cargarNota("NOEXISTE", "INF-101", 8.0);
            check("Legajo inexistente -> error", false);
        } catch (NegocioException e) {
            check("Legajo inexistente -> error", e.getMessage().toLowerCase().contains("no existe"));
        }

        try {
            docenteService.cargarNota("EST001", "MATERIA-FANTA", 8.0);
            check("Materia inexistente -> error", false);
        } catch (NegocioException e) {
            check("Materia inexistente -> error", e.getMessage().toLowerCase().contains("no existe"));
        }

        try {
            docenteService.cargarNota("EST002", "INF-101", 8.0);
            check("Alumno no inscripto en materia -> error", false);
        } catch (NegocioException e) {
            check("Alumno no inscripto en materia -> error", e.getMessage().toLowerCase().contains("inscripto"));
        }

        try {
            docenteService.cargarNota("EST001", "INF-101", 8.5);
            check("Carga exitosa de nota 8.5", true);
        } catch (NegocioException e) {
            check("Carga exitosa de nota 8.5", false);
            System.out.println("        error: " + e.getMessage());
        }

        System.out.println("\n[4] Persistencia real: nueva instancia lee la nota");
        var notaLeida = repoInscripciones.obtenerNotaFinal(
            repoEstudiantes.buscarPorLegajo("EST001").getIdOrThrow(),
            repoMaterias.buscarPorCodigoONombre("INF-101").orElseThrow().getId()
        );
        check("Nota persistida == 8.5", notaLeida.isPresent() && Math.abs(notaLeida.get() - 8.5) < 1e-6);

        System.out.println("\n[5] Servicios listan correctamente");
        check("Estudiantes cargados desde DB (>=2)", estudianteService.listar().size() >= 2);
        check("Materias cargadas desde DB    (>=1)", materiaService.listar().size() >= 1);

        System.out.println("\n=== Resultado: " + passed + "/" + total + " tests OK ===");
        if (passed != total) System.exit(1);
    }

    private static boolean lanza(Runnable r) {
        try { r.run(); return false; } catch (RuntimeException e) { return true; }
    }
}
