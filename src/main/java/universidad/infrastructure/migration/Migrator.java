package universidad.infrastructure.migration;

import universidad.infrastructure.persistence.ConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
        } catch (SQLException | IOException | java.net.URISyntaxException e) {
            throw new RuntimeException("Fallo la migracion", e);
        }
    }

    private List<String> listarArchivos() throws IOException, java.net.URISyntaxException {
        var dir = getClass().getResource(MIGRATION_PATH);
        if (dir == null) {
            throw new IllegalStateException("No se encontro " + MIGRATION_PATH);
        }
        List<String> archivos = new ArrayList<>();
        for (URL url : Collections.list(getClass().getClassLoader().getResources(MIGRATION_PATH.substring(1)))) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                try (var stream = java.nio.file.Files.newDirectoryStream(
                        java.nio.file.Paths.get(url.toURI()), "*.sql")) {
                    stream.forEach(p -> archivos.add(p.getFileName().toString()));
                }
            } else if ("jar".equals(protocol)) {
                String fullPath = url.getPath();
                int separator = fullPath.indexOf("!/");
                String jarPath = fullPath.substring(0, separator);
                String entryPath = fullPath.substring(separator + 2);
                try (java.util.jar.JarFile jar = new java.util.jar.JarFile(
                        java.nio.file.Paths.get(new java.net.URI(jarPath)).toFile())) {
                    java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        String name = entries.nextElement().getName();
                        if (name.startsWith(entryPath) && name.endsWith(".sql") && !name.equals(entryPath)) {
                            archivos.add(name.substring(entryPath.length()));
                        }
                    }
                }
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
