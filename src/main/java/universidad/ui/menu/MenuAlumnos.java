package universidad.ui.menu;

import universidad.InputReader;
import universidad.application.EstudianteService;
import universidad.domain.Estudiante;
import universidad.domain.Materia;
import universidad.domain.NegocioException;
import universidad.domain.repository.IMateriaRepository;

import java.util.List;
import java.util.stream.IntStream;

public class MenuAlumnos {

    private final EstudianteService    serviceEstudiantes;
    private final IMateriaRepository   repoMaterias;
    private final InputReader          input;

    public MenuAlumnos(EstudianteService serviceEstudiantes,
                       IMateriaRepository repoMaterias,
                       InputReader input) {
        this.serviceEstudiantes = serviceEstudiantes;
        this.repoMaterias       = repoMaterias;
        this.input              = input;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- Alumnos ---");
            System.out.println("1. Listar");
            System.out.println("2. Agregar");
            System.out.println("3. Inscribir a materia");
            System.out.println("0. Volver");
            opcion = input.leerEnteroEnRango("Opcion: ", "Opcion", 0, 3);
            try {
                switch (opcion) {
                    case 1 -> listar();
                    case 2 -> agregar();
                    case 3 -> inscribir();
                }
            } catch (NegocioException e) {
                System.out.println("  Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void listar() {
        List<Estudiante> lista = serviceEstudiantes.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay alumnos cargados.");
            return;
        }
        System.out.printf("%-10s %-25s %-10s%n", "Legajo", "Nombre", "Carrera");
        lista.forEach(e -> System.out.printf(
            "%-10s %-25s %-10s%n", e.getLegajo(), e.getNombre(), e.getCarrera()
        ));
    }

    private void agregar() {
        String nombre  = input.leerTexto  ("Nombre: ",    "El nombre");
        String legajo  = input.leerTexto  ("Legajo: ",    "El legajo");
        String email   = input.leerEmail  ("Email: ");
        String carrera = input.leerTexto  ("Carrera: ",   "La carrera");
        int    anio    = input.leerEnteroEnRango("Anio de ingreso (1900-2099): ", "El anio", 1900, 2099);
        Long id = serviceEstudiantes.crear(nombre, legajo, email, carrera, anio);
        System.out.println("Alumno creado con id " + id + ".");
    }

    private void inscribir() {
        List<Estudiante> alumnos = serviceEstudiantes.listar();
        if (alumnos.isEmpty()) {
            System.out.println("No hay alumnos cargados para inscribir.");
            return;
        }
        List<String> legajos = alumnos.stream().map(Estudiante::getLegajo).toList();
        List<String> etiquetas = IntStream.range(0, alumnos.size())
                .mapToObj(i -> alumnos.get(i).getLegajo() + " - " + alumnos.get(i).getNombre())
                .toList();

        String legajo = input.seleccionarDeLista(
            "Alumnos disponibles:",
            "Numero o legajo: ",
            etiquetas,
            legajos::get
        );

        List<Materia> materias = repoMaterias.listar();
        if (materias.isEmpty()) {
            System.out.println("No hay materias cargadas.");
            return;
        }
        List<String> codigos = materias.stream().map(Materia::getCodigo).toList();
        List<String> etiquetasMateria = IntStream.range(0, materias.size())
                .mapToObj(i -> materias.get(i).getCodigo() + " - " + materias.get(i).getNombre())
                .toList();

        String materia = input.seleccionarDeLista(
            "Materias disponibles:",
            "Numero o codigo: ",
            etiquetasMateria,
            codigos::get
        );

        serviceEstudiantes.inscribir(legajo, materia);
        System.out.println("Inscripcion registrada.");
    }
}
