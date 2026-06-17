package universidad.domain.repository;

import universidad.domain.Materia;

import java.util.List;
import java.util.Optional;

public interface IMateriaRepository {

    Optional<Materia> buscarPorCodigoONombre(String codigoONombre);

    List<Materia> listar();
}
