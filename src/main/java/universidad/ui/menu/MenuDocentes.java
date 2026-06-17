package universidad.ui.menu;

import universidad.InputReader;
import universidad.application.DocenteService;
import universidad.application.EstudianteService;
import universidad.domain.CategoriaDocente;
import universidad.domain.Docente;
import universidad.domain.Estudiante;
import universidad.domain.Materia;
import universidad.domain.NegocioException;
import universidad.domain.repository.IMateriaRepository;

import java.util.List;
import java.util.stream.IntStream;

public class MenuDocentes {

    private final DocenteService     serviceDocentes;
    private final EstudianteService  serviceEstudiantes;
    private final IMateriaRepository repoMaterias;
    private final InputReader        input;

    public MenuDocentes(DocenteService serviceDocentes,
                        EstudianteService serviceEstudiantes,
                        IMateriaRepository repoMaterias,
                        InputReader input) {
        this.serviceDocentes    = serviceDocentes;
        this.serviceEstudiantes = serviceEstudiantes;
        this.repoMaterias       = repoMaterias;
        this.input              = input;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- Docentes ---");
            System.out.println("1. Listar");
            System.out.println("2. Agregar");
            System.out.println("3. Cargar nota");
            System.out.println("0. Volver");
            opcion = input.leerEnteroEnRango("Opcion: ", "Opcion", 0, 3);
            try {
                switch (opcion) {
                    case 1 -> listar();
                    case 2 -> agregar();
                    case 3 -> cargarNota();
                }
            } catch (NegocioException e) {
                System.out.println("  Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void listar() {
        List<Docente> lista = serviceDocentes.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay docentes cargados.");
            return;
        }
        System.out.printf("%-10s %-25s %-20s%n", "Legajo", "Nombre", "Departamento");
        lista.forEach(d -> System.out.printf(
            "%-10s %-25s %-20s%n", d.getLegajo(), d.getNombre(), d.getDepartamento()
        ));
    }

    private void agregar() {
        String nombre       = input.leerTexto("Nombre: ",       "El nombre");
        String legajo       = input.leerTexto("Legajo: ",       "El legajo");
        String email        = input.leerEmail("Email: ");
        String departamento = input.leerTexto("Departamento: ", "El departamento");

        System.out.println("Categorias: 1-Titular  2-Adjunto  3-JTP  4-Ayudante");
        int cat = input.leerEnteroEnRango("Categoria (1-4): ", "La categoria", 1, 4);
        CategoriaDocente categoria = CategoriaDocente.desdeId(cat);

        Long id = serviceDocentes.crear(nombre, legajo, email, departamento, categoria);
        System.out.println("Docente creado con id " + id + ".");
    }

    private void cargarNota() {
        List<Estudiante> alumnos = serviceEstudiantes.listar();
        if (alumnos.isEmpty()) {
            System.out.println("No hay alumnos cargados.");
            return;
        }
        List<String> legajos = alumnos.stream().map(Estudiante::getLegajo).toList();
        List<String> etiquetasAlumnos = IntStream.range(0, alumnos.size())
                .mapToObj(i -> alumnos.get(i).getLegajo() + " - " + alumnos.get(i).getNombre())
                .toList();

        String legajo = input.seleccionarDeLista(
            "Alumnos disponibles:",
            "Numero o legajo: ",
            etiquetasAlumnos,
            legajos::get
        );

        List<Materia> materias = repoMaterias.listar();
        if (materias.isEmpty()) {
            System.out.println("No hay materias cargadas.");
            return;
        }
        List<String> codigos = materias.stream().map(Materia::getCodigo).toList();
        List<String> etiquetasMaterias = IntStream.range(0, materias.size())
                .mapToObj(i -> materias.get(i).getCodigo() + " - " + materias.get(i).getNombre())
                .toList();

        String materia = input.seleccionarDeLista(
            "Materias disponibles:",
            "Numero o codigo: ",
            etiquetasMaterias,
            codigos::get
        );

        double nota = input.leerDoubleEnRango("Nota (0-10): ", "La nota", 0, 10);
        serviceDocentes.cargarNota(legajo, materia, nota);
        System.out.println("Nota cargada correctamente.");
    }
}
