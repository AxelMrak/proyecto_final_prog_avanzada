package universidad.application;

import universidad.domain.Estudiante;
import universidad.domain.Materia;
import universidad.domain.NegocioException;
import universidad.domain.repository.IEstudianteRepository;
import universidad.domain.repository.IInscripcionRepository;
import universidad.domain.repository.IMateriaRepository;

public class InscripcionService {

    private final IEstudianteRepository  repoEstudiantes;
    private final IMateriaRepository     repoMaterias;
    private final IInscripcionRepository repoInscripciones;

    public InscripcionService(IEstudianteRepository repoEstudiantes,
                              IMateriaRepository repoMaterias,
                              IInscripcionRepository repoInscripciones) {
        this.repoEstudiantes   = repoEstudiantes;
        this.repoMaterias      = repoMaterias;
        this.repoInscripciones = repoInscripciones;
    }

    public void inscribir(String legajo, String materiaCodigoONombre) {
        Estudiante e = repoEstudiantes.buscarPorLegajo(legajo);
        if (e == null) {
            throw new NegocioException("El alumno con legajo '" + legajo + "' no existe.");
        }

        Materia m = repoMaterias.buscarPorCodigoONombre(materiaCodigoONombre)
                .orElseThrow(() -> new NegocioException(
                    "La materia '" + materiaCodigoONombre + "' no existe."
                ));

        if (repoInscripciones.existe(e.getIdOrThrow(), m.getId())) {
            throw new NegocioException(
                "El alumno '" + legajo + "' ya esta inscripto en '" + materiaCodigoONombre + "'."
            );
        }

        repoInscripciones.guardar(e.getIdOrThrow(), m.getId());
    }
}
