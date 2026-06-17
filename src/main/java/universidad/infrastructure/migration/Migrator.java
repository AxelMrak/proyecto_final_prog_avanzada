package universidad.infrastructure.migration;

import universidad.infrastructure.persistence.ConnectionFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Migrator {

    private static final String MIGRATION_PATH = "/db/migration/";

    public void migrar() {
        try (Connection conn = ConnectionFactory.obtener()) {
            for (String archivo : listarArchivos()) {
                ejecutarArchivo(conn, archivo);
            }
        } catch (SQLException | IOException | URISyntaxException e) {
            throw new RuntimeException("Fallo la migracion", e);
        }
    }

    private List<String> listarArchivos() throws IOException, URISyntaxException {
        var dir = getClass().getResource(MIGRATION_PATH);
        if (dir == null) {
            throw new IllegalStateException("No se encontro " + MIGRATION_PATH);
        }
        List<String> archivos = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir.toURI()), "*.sql")) {
            for (Path path : stream) {
                archivos.add(path.getFileName().toString());
            }
        }
        Collections.sort(archivos);
        return archivos;
    }

    private void ejecutarArchivo(Connection conn, String nombre) {
        String recurso = MIGRATION_PATH + nombre;
        try (InputStream in = getClass().getResourceAsStream(recurso);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             Statement stmt = conn.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                String trimmed = linea.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("--")) continue;
                sql.append(linea).append('\n');
            }
            stmt.executeUpdate(sql.toString());
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Fallo la migracion " + nombre, e);
        }
    }
}
