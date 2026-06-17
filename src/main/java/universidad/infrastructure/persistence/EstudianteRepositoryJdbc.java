package universidad.infrastructure.persistence;

import universidad.domain.Estudiante;
import universidad.domain.NegocioException;
import universidad.domain.Rol;
import universidad.domain.UsuarioAcademico;
import universidad.domain.repository.IEstudianteRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteRepositoryJdbc implements IEstudianteRepository {

    private static final String SQL_INSERT_USUARIO =
        "INSERT INTO usuario_academico (nombre, legajo, email, rol_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_INSERT_ESTUDIANTE =
        "INSERT INTO estudiante (id, carrera, anio_ingreso) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL =
        "SELECT u.id, u.nombre, u.legajo, u.email, e.carrera, e.anio_ingreso " +
        "FROM usuario_academico u JOIN estudiante e ON e.id = u.id " +
        "WHERE u.rol_id = ? ORDER BY u.id";
    private static final String SQL_SELECT_BY_LEGAJO =
        "SELECT u.id, u.nombre, u.legajo, u.email, e.carrera, e.anio_ingreso " +
        "FROM usuario_academico u JOIN estudiante e ON e.id = u.id " +
        "WHERE u.legajo = ? AND u.rol_id = ?";

    @Override
    public Long guardar(UsuarioAcademico usuario, String carrera, int anioIngreso) {
        try (Connection conn = ConnectionFactory.obtener()) {
            conn.setAutoCommit(false);
            try {
                long id = insertarUsuario(conn, usuario, Rol.ESTUDIANTE);
                insertarDetalle(conn, (int) id, carrera, anioIngreso);
                conn.commit();
                return id;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo guardar el estudiante: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Estudiante> listar() {
        List<Estudiante> resultado = new ArrayList<>();
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL)) {
            ps.setInt(1, Rol.ESTUDIANTE.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo listar estudiantes", e);
        }
        return resultado;
    }

    @Override
    public Estudiante buscarPorLegajo(String legajo) {
        try (Connection conn = ConnectionFactory.obtener();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_LEGAJO)) {
            ps.setString(1, legajo);
            ps.setInt(2, Rol.ESTUDIANTE.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo buscar el estudiante", e);
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

    private void insertarDetalle(Connection conn, int id, String carrera, int anio) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_ESTUDIANTE)) {
            ps.setInt(1, id);
            ps.setString(2, carrera);
            ps.setInt(3, anio);
            ps.executeUpdate();
        }
    }

    private Estudiante mapear(ResultSet rs) throws SQLException {
        return new Estudiante(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("legajo"),
            rs.getString("email"),
            rs.getString("carrera"),
            rs.getInt("anio_ingreso")
        );
    }
}
