package universidad.infrastructure.persistence;

import universidad.domain.Materia;
import universidad.domain.NegocioException;
import universidad.domain.repository.IMateriaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MateriaRepositoryJdbc implements IMateriaRepository {

    private static final String SQL_BUSCAR =
        "SELECT id, nombre, codigo, anio FROM materia " +
        "WHERE LOWER(codigo) = LOWER(?) OR LOWER(nombre) = LOWER(?)";
    private static final String SQL_LISTAR =
        "SELECT id, nombre, codigo, anio FROM materia ORDER BY anio, nombre";

    @Override
    public Optional<Materia> buscarPorCodigoONombre(String codigoONombre) {
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_BUSCAR)) {
            ps.setString(1, codigoONombre);
            ps.setString(2, codigoONombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo buscar la materia", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Materia> listar() {
        List<Materia> resultado = new ArrayList<>();
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultado.add(mapear(rs));
        } catch (SQLException e) {
            throw new NegocioException("No se pudo listar materias", e);
        }
        return resultado;
    }

    private Materia mapear(ResultSet rs) throws SQLException {
        return new Materia(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("codigo"),
            rs.getInt("anio")
        );
    }
}
