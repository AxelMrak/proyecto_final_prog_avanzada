package universidad.domain.repository;

import java.util.Optional;

public interface IInscripcionRepository {

    Long guardar(int estudianteId, int materiaId);

    boolean existe(int estudianteId, int materiaId);

    boolean actualizarNotaFinal(int estudianteId, int materiaId, double nota);

    Optional<Double> obtenerNotaFinal(int estudianteId, int materiaId);
}
