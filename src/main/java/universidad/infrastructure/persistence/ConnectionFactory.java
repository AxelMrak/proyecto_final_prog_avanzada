package universidad.infrastructure.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:sqlite:resources/sistema_universitario.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite no encontrado", e);
        }
    }

    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
