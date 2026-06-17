package universidad.application;

import universidad.domain.CategoriaDocente;
import universidad.domain.Docente;
import universidad.domain.Materia;
import universidad.domain.NegocioException;
import universidad.domain.Nota;
import universidad.domain.repository.IDocenteRepository;
import universidad.domain.repository.IEstudianteRepository;
import universidad.domain.repository.IInscripcionRepository;
import universidad.domain.repository.IMateriaRepository;

import java.util.List;

public class DocenteService {

    private final IDocenteRepository     repoDocentes;
    private final IEstudianteRepository  repoEstudiantes;
    private final IMateriaRepository     repoMaterias;
    private final IInscripcionRepository repoInscripciones;

    public DocenteService(IDocenteRepository repoDocentes,
                          IEstudianteRepository repoEstudiantes,
                          IMateriaRepository repoMaterias,
                          IInscripcionRepository repoInscripciones) {
        this.repoDocentes      = repoDocentes;
        this.repoEstudiantes   = repoEstudiantes;
        this.repoMaterias      = repoMaterias;
        this.repoInscripciones = repoInscripciones;
    }

    public Long crear(String nombre, String legajo, String email,
                      String departamento, CategoriaDocente categoria) {
        Docente d = new Docente(nombre, legajo, email, departamento, categoria.getNombre());
        return repoDocentes.guardar(d, d.getDepartamento(), categoria.getId());
    }

    public List<Docente> listar() {
        return repoDocentes.listar();
    }

    public void cargarNota(String estudianteLegajo, String materiaCodigoONombre, double notaValor) {
        Nota nota = new Nota(notaValor);

        var estudiante = repoEstudiantes.buscarPorLegajo(estudianteLegajo);
        if (estudiante == null) {
            throw new NegocioException(
                "El alumno con legajo '" + estudianteLegajo + "' no existe o no es estudiante."
            );
        }

        Materia materia = repoMaterias.buscarPorCodigoONombre(materiaCodigoONombre)
                .orElseThrow(() -> new NegocioException(
                    "La materia '" + materiaCodigoONombre + "' no existe."
                ));

        if (!repoInscripciones.existe(estudiante.getIdOrThrow(), materia.getId())) {
            throw new NegocioException(
                "El alumno '" + estudianteLegajo
                + "' no esta inscripto en la materia '" + materiaCodigoONombre + "'."
            );
        }

        boolean ok = repoInscripciones.actualizarNotaFinal(
            estudiante.getIdOrThrow(),
            materia.getId(),
            nota.getValor()
        );
        if (!ok) {
            throw new NegocioException("No se pudo guardar la nota. Intente nuevamente.");
        }
    }
}
