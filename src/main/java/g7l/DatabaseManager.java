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

    public static void main(String[] args) throws SQLException, URISyntaxException, IOException {

        DatabaseManager dbManager = new DatabaseManager();

        createDatabase();

        dbManager.verTablaResultados();

        dbManager.verTablaPronosticos();

        Connection conn = getConnection();

        dbManager.closeConnection(conn);
    }

    //Método para crear base de datos
    public static void createDatabase() throws SQLException, URISyntaxException, IOException {

        //Leer ruta de init.sql
        URL url = DatabaseManager.class.getClassLoader().getResource("init.sql");
        if (url == null) {
            throw new IllegalStateException("No se pudo encontrar el archivo .sql!");
        }

        Path path = Paths.get(url.toURI());

        //Leer el contenido de init.sql
        String sql = new String(Files.readAllBytes(path));

        //Eliminar la base de datos si ya existía
        File file = new File(DB);
        if (file.exists()) {
            file.delete();
        }

        //Crear conexión a la base de datos
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB);

        //Ejecutar consultas SQL para crear tablas y cargar los datos
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }

        //Cerrar conexión
        conn.close();
    }
    //Método para conectarse a la url de la DB
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB);
    }

    //Método para cerrar la conexión
    public void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    //Método para leer la tabla de resultados
    public void verTablaResultados() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM resultados")) {

            System.out.println("Tabla de resultados:\n");

            while (rs.next()) {
                int id_fase = rs.getInt("id_fase");
                int id_ronda = rs.getInt("id_ronda");
                String equipo_1 = rs.getString("equipo_1");
                int goles_equipo_1 = rs.getInt("goles_equipo_1");
                String equipo_2 = rs.getString("equipo_2");
                int goles_equipo_2 = rs.getInt("goles_equipo_2");

                System.out.println("{id_fase=" + id_fase + ", id_ronda=" + id_ronda +
                        ", equipo_1=" + equipo_1 + ", goles_equipo_1=" + goles_equipo_1 +
                        ", goles_equipo_2=" + goles_equipo_2 + ", equipo_2=" + equipo_2 + "}\n");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la tabla de resultados: " + e.getMessage());
        }
    }

    //Método para leer la tabla de pronósticos
    public void verTablaPronosticos() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Pronosticos")) {

            System.out.println("Tabla de pronósticos:\n");

            while (rs.next()) {
                int id_fase = rs.getInt("id_fase");
                int id_ronda = rs.getInt("id_ronda");
                String participante = rs.getString("participante");
                String equipo_1 = rs.getString("equipo_1");
                String equipo_2 = rs.getString("equipo_2");
                String resultado = rs.getString("resultado");
                System.out.println("{id_fase=" + id_fase + ", id_ronda=" + id_ronda +
                        ", participante=" + participante + ", equipo_1=" + equipo_1 +
                        ", equipo_2=" + equipo_2 + ", resultado=" + resultado + "}\n");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
