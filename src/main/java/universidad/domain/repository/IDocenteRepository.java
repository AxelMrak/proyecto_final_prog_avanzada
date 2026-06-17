package universidad.domain.repository;

import universidad.domain.Docente;
import universidad.domain.UsuarioAcademico;

import java.util.List;

public interface IDocenteRepository {

    Long guardar(UsuarioAcademico usuario, String departamento, int categoriaId);

    List<Docente> listar();

    Docente buscarPorLegajo(String legajo);
}
