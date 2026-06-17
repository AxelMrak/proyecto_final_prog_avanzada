package universidad.infrastructure.persistence;

import universidad.domain.Docente;
import universidad.domain.NegocioException;
import universidad.domain.Rol;
import universidad.domain.UsuarioAcademico;
import universidad.domain.repository.IDocenteRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocenteRepositoryJdbc implements IDocenteRepository {

    private static final String SQL_INSERT_USUARIO =
        "INSERT INTO usuario_academico (nombre, legajo, email, rol_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_INSERT_DOCENTE =
        "INSERT INTO docente (id, departamento, categoria_id) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL =
        "SELECT u.id, u.nombre, u.legajo, u.email, d.departamento, d.categoria_id " +
        "FROM usuario_academico u JOIN docente d ON d.id = u.id " +
        "WHERE u.rol_id = ? ORDER BY u.id";
    private static final String SQL_SELECT_BY_LEGAJO =
        "SELECT u.id, u.nombre, u.legajo, u.email, d.departamento, d.categoria_id " +
        "FROM usuario_academico u JOIN docente d ON d.id = u.id " +
        "WHERE u.legajo = ? AND u.rol_id = ?";

    @Override
    public Long guardar(UsuarioAcademico usuario, String departamento, int categoriaId) {
        try (Connection conn = ConnectionFactory.obtener()) {
            conn.setAutoCommit(false);
            try {
                long id = insertarUsuario(conn, usuario, Rol.DOCENTE);
                insertarDetalle(conn, (int) id, departamento, categoriaId);
                conn.commit();
                return id;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo guardar el docente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Docente> listar() {
        List<Docente> resultado = new ArrayList<>();
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL)) {
            ps.setInt(1, Rol.DOCENTE.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo listar docentes", e);
        }
        return resultado;
    }

    @Override
    public Docente buscarPorLegajo(String legajo) {
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_LEGAJO)) {
            ps.setString(1, legajo);
            ps.setInt(2, Rol.DOCENTE.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo buscar el docente", e);
        }
        return null;
    }

    private long insertarUsuario(Connection conn, UsuarioAcademico u, Rol rol) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_USUARIO, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getLegajo());
            ps.setString(3, u.getEmail());
            ps.setInt(4, rol.getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }
        throw new SQLException("No se genero id para el usuario");
    }

    private void insertarDetalle(Connection conn, int id, String departamento, int categoriaId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_DOCENTE)) {
            ps.setInt(1, id);
            ps.setString(2, departamento);
            ps.setInt(3, categoriaId);
            ps.executeUpdate();
        }
    }

    private Docente mapear(ResultSet rs) throws SQLException {
        int categoriaId = rs.getInt("categoria_id");
        return new Docente(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("legajo"),
            rs.getString("email"),
            rs.getString("departamento"),
            universidad.domain.CategoriaDocente.desdeId(categoriaId).getNombre()
        );
    }
}
