package universidad.domain.repository;

import universidad.domain.Administrativo;
import universidad.domain.UsuarioAcademico;

import java.util.List;

public interface IAdministrativoRepository {

    Long guardar(UsuarioAcademico usuario, String sector);

    List<Administrativo> listar();

    Administrativo buscarPorLegajo(String legajo);
}
