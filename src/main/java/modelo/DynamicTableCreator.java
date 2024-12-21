package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Clase que permite la creación dinámica de tablas en una base de datos MySQL.
 *
 * Esta clase está diseñada para que un usuario pueda especificar el nombre de una tabla
 * y crearla con una estructura predefinida. La estructura incluye campos como IdUnico,
 * NombreProducto, Descripcion, Foto, entre otros.
 *
 * **Características:**
 * - Valida el nombre de la tabla para evitar inyecciones SQL.
 * - Crea la tabla con restricciones específicas para garantizar la integridad de los datos.
 * - Utiliza conexiones seguras y gestionadas para la interacción con la base de datos.
 *
 * **Requisitos:**
 * - Una base de datos MySQL accesible con las credenciales proporcionadas.
 * - Permisos para ejecutar comandos CREATE TABLE.
 *
 * @author [Tu Nombre]
 * @version 1.1
 * @since 2024
 */
public class DynamicTableCreator {

    // URL de la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Tienda"; // Cambiar "Tienda" por el nombre real de tu base de datos.

    // Credenciales de la base de datos
    private static final String DB_USER = "root"; // Usuario de la base de datos.
    private static final String DB_PASSWORD = "password"; // Contraseña de la base de datos.

    /**
     * Método para crear una tabla con el nombre especificado por el usuario.
     *
     * Este método valida el nombre de la tabla proporcionado para evitar
     * inyecciones SQL y crea la tabla con la estructura definida.
     *
     * @param tableName El nombre de la tabla a crear.
     * @throws IllegalArgumentException Si el nombre de la tabla no es válido.
     * @throws Exception Si ocurre un error al interactuar con la base de datos.
     */
    public void createTable(String tableName) throws Exception {
        // Validar el nombre de la tabla para evitar inyecciones SQL
        if (!isValidTableName(tableName)) {
            throw new IllegalArgumentException("Nombre de tabla no válido. Solo se permiten caracteres alfanuméricos y guiones bajos.");
        }

        // Crear la conexión y ejecutar el comando SQL
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            // Crear la tabla con la estructura especificada
            String createTableSQL = String.format("""
                CREATE TABLE %s (
                    IdUnico INT AUTO_INCREMENT PRIMARY KEY,
                    NombreProducto VARCHAR(255) NOT NULL,
                    Descripcion VARCHAR(455) NOT NULL,
                    Foto BOOLEAN NOT NULL DEFAULT FALSE,
                    NumeroUnicoFoto INT NULL,
                    Cantidad INT NOT NULL,
                    Realizado BOOLEAN NOT NULL DEFAULT FALSE,
                    CONSTRAINT chk_Foto CHECK (Foto = 0 OR NumeroUnicoFoto IS NOT NULL)
                );
                """, tableName);

            statement.executeUpdate(createTableSQL);
            System.out.println("Tabla '" + tableName + "' creada exitosamente.");
        } catch (Exception e) {
            // Registrar y volver a lanzar la excepción
            System.err.println("Error al crear la tabla '" + tableName + "': " + e.getMessage());
            throw e;
        }
    }

    /**
     * Valida el nombre de la tabla para garantizar que no contenga caracteres peligrosos.
     *
     * @param tableName El nombre de la tabla a validar.
     * @return true si el nombre es válido, false en caso contrario.
     */
    private boolean isValidTableName(String tableName) {
        // Permitir solo caracteres alfanuméricos y guiones bajos
        return tableName != null && tableName.matches("[a-zA-Z0-9_]+");
    }
}
