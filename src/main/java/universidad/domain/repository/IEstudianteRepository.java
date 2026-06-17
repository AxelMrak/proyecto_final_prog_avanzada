package universidad.domain.repository;

import universidad.domain.Estudiante;
import universidad.domain.UsuarioAcademico;

import java.util.List;

public interface IEstudianteRepository {

    Long guardar(UsuarioAcademico usuario, String carrera, int anioIngreso);

    List<Estudiante> listar();

    Estudiante buscarPorLegajo(String legajo);
}
