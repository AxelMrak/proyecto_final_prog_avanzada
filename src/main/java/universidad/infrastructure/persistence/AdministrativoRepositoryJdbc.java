package universidad.infrastructure.persistence;

import universidad.domain.Administrativo;
import universidad.domain.NegocioException;
import universidad.domain.Rol;
import universidad.domain.UsuarioAcademico;
import universidad.domain.repository.IAdministrativoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministrativoRepositoryJdbc implements IAdministrativoRepository {

    private static final String SQL_INSERT_USUARIO =
        "INSERT INTO usuario_academico (nombre, legajo, email, rol_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_INSERT_DETALLE =
        "INSERT INTO administrativo (id, sector) VALUES (?, ?)";
    private static final String SQL_SELECT_ALL =
        "SELECT u.id, u.nombre, u.legajo, u.email, a.sector " +
        "FROM usuario_academico u JOIN administrativo a ON a.id = u.id " +
        "WHERE u.rol_id = ? ORDER BY u.id";
    private static final String SQL_SELECT_BY_LEGAJO =
        "SELECT u.id, u.nombre, u.legajo, u.email, a.sector " +
        "FROM usuario_academico u JOIN administrativo a ON a.id = u.id " +
        "WHERE u.legajo = ? AND u.rol_id = ?";

    @Override
    public Long guardar(UsuarioAcademico usuario, String sector) {
        try (Connection conn = ConnectionFactory.obtener()) {
            conn.setAutoCommit(false);
            try {
                long id = insertarUsuario(conn, usuario);
                insertarDetalle(conn, (int) id, sector);
                conn.commit();
                return id;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo guardar el administrativo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Administrativo> listar() {
        List<Administrativo> resultado = new ArrayList<>();
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL)) {
            ps.setInt(1, Rol.ADMINISTRATIVO.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo listar administrativos", e);
        }
        return resultado;
    }

    @Override
    public Administrativo buscarPorLegajo(String legajo) {
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_LEGAJO)) {
            ps.setString(1, legajo);
            ps.setInt(2, Rol.ADMINISTRATIVO.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo buscar el administrativo", e);
        }
        return null;
    }

    private long insertarUsuario(Connection conn, UsuarioAcademico u) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_USUARIO, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getLegajo());
            ps.setString(3, u.getEmail());
            ps.setInt(4, Rol.ADMINISTRATIVO.getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }
        throw new SQLException("No se genero id para el usuario");
    }

    private void insertarDetalle(Connection conn, int id, String sector) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_DETALLE)) {
            ps.setInt(1, id);
            ps.setString(2, sector);
            ps.executeUpdate();
        }
    }

    private Administrativo mapear(ResultSet rs) throws SQLException {
        return new Administrativo(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("legajo"),
            rs.getString("email"),
            rs.getString("sector")
        );
    }
}
