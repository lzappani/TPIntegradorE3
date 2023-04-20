package g7l;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {

    private static final String DB = "mydatabase.db";

    // Método para crear base de datos
    public static void createDatabase(String init) throws SQLException, URISyntaxException, IOException {
        // Leer ruta de init.sql
        URL url = DatabaseManager.class.getClassLoader().getResource(init);
        if (url == null) {
            throw new IllegalStateException("No se pudo encontrar el archivo .sql!");
        }
        Path path = Paths.get(url.toURI());
        //Leer el contenido de init.sql
        String sql = new String(Files.readAllBytes(path));
        //Eliminar la base de datos si ya existía
        File file = new File(DB);
        if (!file.exists()) {
            //Crea base de datos y ejecuta comandos SQL para crear tablas y cargar los datos
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            }
        }
    }

    // Método para conectarse a la url de la DB
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB);
    }

    public static void printTable(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        System.out.println("Tabla: " + metadata.getTableName(1));
        int columnCount = metadata.getColumnCount();
        for (int j = 1; j <= columnCount; j++) {
            System.out.printf("%-20s", metadata.getColumnName(j));
        }
        System.out.println();
        while (rs.next()) {
            for (int k = 1; k <= columnCount; k++) {
                System.out.printf("%-20s", rs.getString(k));
            }
            System.out.println();
        }
        System.out.println();
    }
}
