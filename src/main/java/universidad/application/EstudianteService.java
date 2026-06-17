package universidad.application;

import universidad.domain.Estudiante;
import universidad.domain.NegocioException;
import universidad.domain.repository.IEstudianteRepository;

import java.util.List;

public class EstudianteService {

    private final IEstudianteRepository repo;
    private final InscripcionService    inscripciones;

    public EstudianteService(IEstudianteRepository repo, InscripcionService inscripciones) {
        this.repo          = repo;
        this.inscripciones = inscripciones;
    }

    public Long crear(String nombre, String legajo, String email,
                      String carrera, int anioIngreso) {
        Estudiante e = new Estudiante(nombre, legajo, email, carrera, anioIngreso);
        return repo.guardar(e, e.getCarrera(), e.getAnioIngreso());
    }

    public List<Estudiante> listar() {
        return repo.listar();
    }

    public Estudiante buscarPorLegajo(String legajo) {
        return repo.buscarPorLegajo(legajo);
    }

    public void inscribir(String legajo, String materia) {
        Estudiante e = repo.buscarPorLegajo(legajo);
        if (e == null) {
            throw new NegocioException("El alumno con legajo '" + legajo + "' no existe.");
        }
        inscripciones.inscribir(legajo, materia);
    }
}
