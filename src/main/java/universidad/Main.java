package universidad;

import universidad.application.AdministrativoService;
import universidad.application.DocenteService;
import universidad.application.EstudianteService;
import universidad.application.InscripcionService;
import universidad.infrastructure.migration.Migrator;
import universidad.infrastructure.persistence.AdministrativoRepositoryJdbc;
import universidad.infrastructure.persistence.DocenteRepositoryJdbc;
import universidad.infrastructure.persistence.EstudianteRepositoryJdbc;
import universidad.infrastructure.persistence.InscripcionRepositoryJdbc;
import universidad.infrastructure.persistence.MateriaRepositoryJdbc;
import universidad.ui.menu.MenuAdministrativos;
import universidad.ui.menu.MenuAlumnos;
import universidad.ui.menu.MenuDocentes;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Migrator().migrar();

        var repoEstudiantes   = new EstudianteRepositoryJdbc();
        var repoDocentes      = new DocenteRepositoryJdbc();
        var repoAdministrativos = new AdministrativoRepositoryJdbc();
        var repoMaterias      = new MateriaRepositoryJdbc();
        var repoInscripciones = new InscripcionRepositoryJdbc();

        var inscripciones     = new InscripcionService(repoEstudiantes, repoMaterias, repoInscripciones);
        var estudianteService = new EstudianteService(repoEstudiantes, inscripciones);
        var docenteService    = new DocenteService(repoDocentes, repoEstudiantes, repoMaterias, repoInscripciones);
        var adminService      = new AdministrativoService(repoAdministrativos);

        Scanner scanner = new Scanner(System.in);
        var input = new InputReader(scanner);

        var menuAlumnos         = new MenuAlumnos(estudianteService, repoMaterias, input);
        var menuDocentes        = new MenuDocentes(docenteService, estudianteService, repoMaterias, input);
        var menuAdministrativos = new MenuAdministrativos(adminService, input);

        int opcion;
        do {
            System.out.println("\n=== Sistema de Gestion Universitaria ===");
            System.out.println("1. Alumnos");
            System.out.println("2. Docentes");
            System.out.println("3. Administrativos");
            System.out.println("0. Salir");
            opcion = input.leerEnteroEnRango("Ingrese opcion: ", "Opcion", 0, 3);
            switch (opcion) {
                case 1 -> menuAlumnos.mostrar();
                case 2 -> menuDocentes.mostrar();
                case 3 -> menuAdministrativos.mostrar();
                case 0 -> System.out.println("Saliendo...");
            }
        } while (opcion != 0);

        scanner.close();
    }
}
