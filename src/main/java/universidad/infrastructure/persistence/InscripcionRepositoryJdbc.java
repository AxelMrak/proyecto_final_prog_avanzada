package universidad.infrastructure.persistence;

import universidad.domain.NegocioException;
import universidad.domain.repository.IInscripcionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class InscripcionRepositoryJdbc implements IInscripcionRepository {

    private static final String SQL_INSERT =
        "INSERT INTO inscripcion (estudiante_id, materia_id, fecha, estado_id) " +
        "VALUES (?, ?, datetime('now'), 1)";
    private static final String SQL_EXISTE =
        "SELECT 1 FROM inscripcion WHERE estudiante_id = ? AND materia_id = ?";
    private static final String SQL_UPDATE_NOTA =
        "UPDATE inscripcion SET nota_final = ? WHERE estudiante_id = ? AND materia_id = ?";
    private static final String SQL_SELECT_NOTA =
        "SELECT nota_final FROM inscripcion WHERE estudiante_id = ? AND materia_id = ?";

    @Override
    public Long guardar(int estudianteId, int materiaId) {
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, estudianteId);
            ps.setInt(2, materiaId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo guardar la inscripcion: " + e.getMessage(), e);
        }
        throw new NegocioException("No se genero id para la inscripcion");
    }

    @Override
    public boolean existe(int estudianteId, int materiaId) {
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTE)) {
            ps.setInt(1, estudianteId);
            ps.setInt(2, materiaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo consultar la inscripcion", e);
        }
    }

    @Override
    public boolean actualizarNotaFinal(int estudianteId, int materiaId, double nota) {
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_NOTA)) {
            ps.setDouble(1, nota);
            ps.setInt(2, estudianteId);
            ps.setInt(3, materiaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new NegocioException("No se pudo actualizar la nota: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Double> obtenerNotaFinal(int estudianteId, int materiaId) {
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_NOTA)) {
            ps.setInt(1, estudianteId);
            ps.setInt(2, materiaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double nota = rs.getDouble("nota_final");
                    if (rs.wasNull()) return Optional.empty();
                    return Optional.of(nota);
                }
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo obtener la nota", e);
        }
        return Optional.empty();
    }
}
