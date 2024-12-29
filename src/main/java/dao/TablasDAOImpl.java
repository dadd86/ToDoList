package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase DAO que proporciona métodos para interactuar con la base de datos y obtener los nombres de las tablas.
 * Esta clase utiliza Hibernate para establecer una conexión con la base de datos y ejecutar consultas SQL nativas.
 *
 * **Responsabilidades:**
 * - Configurar y gestionar la sesión de Hibernate.
 * - Ejecutar consultas SQL para obtener los nombres de las tablas.
 * - Gestionar el ciclo de vida de la sesión para evitar fugas de recursos.
 */
public class TablasDAOImpl implements TablasDAO {

    private static final Logger logger = LoggerFactory.getLogger(TablasDAOImpl.class);  // Logger para la clase
    private SessionFactory sessionFactory;

    /**
     * Constructor que inicializa la sesión de Hibernate.
     * Configura la fábrica de sesiones para interactuar con la base de datos utilizando Hibernate.
     *
     * Este constructor utiliza un archivo de configuración de Hibernate (hibernate.cfg.xml) para
     * establecer los parámetros necesarios para la conexión a la base de datos.
     */
    public TablasDAOImpl() {
        try {
            // Configuración de Hibernate, cargando la configuración desde hibernate.cfg.xml
            this.sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            // Mejor manejo de excepciones
            logger.error("Error al configurar la sesión de Hibernate: {}", e.getMessage(), e);
            throw new RuntimeException("Error al configurar la sesión de Hibernate", e);  // Propagar el error
        }
    }

    /**
     * Método que obtiene los nombres de todas las tablas en la base de datos.
     *
     * Este método ejecuta una consulta SQL nativa a la base de datos para recuperar los nombres
     * de las tablas en el esquema público. La consulta está adaptada para MySQL.
     *
     * @return Lista de nombres de las tablas en la base de datos.
     * @throws IllegalStateException Si ocurre un error durante la ejecución de la consulta o si la sesión es nula.
     */
    @Override
    public List<String> getTablas() {
        // Usamos 'try-with-resources' para asegurar que la sesión se cierre automáticamente
        try (Session session = sessionFactory.openSession()) {
            if (session == null) {
                throw new IllegalStateException("La sesión de Hibernate no se pudo abrir.");
            }

            // Consulta SQL nativa para obtener los nombres de las tablas en MySQL
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'your_database_name'"; // MySQL

            // Crear la consulta nativa
            Query<String> query = session.createNativeQuery(sql, String.class);

            // Ejecutar la consulta y obtener los resultados
            return query.getResultList();
        } catch (Exception e) {
            // Registrar la excepción con más detalles
            logger.error("Error al obtener los nombres de las tablas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener los nombres de las tablas", e);  // Propagar el error
        }
    }

    /**
     * Método principal para probar el acceso a las tablas y mostrar los resultados.
     * Este método solo debe usarse con fines de prueba y depuración.
     *
     * @param args Los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        try {
            // Crear la instancia de TablasDAOImpl
            TablasDAOImpl tablasDAOImpl = new TablasDAOImpl();

            // Obtener los nombres de las tablas
            List<String> tablas = tablasDAOImpl.getTablas();

            // Imprimir los resultados
            if (tablas != null && !tablas.isEmpty()) {
                for (String tabla : tablas) {
                    System.out.println("Tabla: " + tabla);
                }
            } else {
                System.out.println("No se pudieron obtener los nombres de las tablas.");
            }
        } catch (Exception e) {
            // Manejo de cualquier error que pueda ocurrir durante la prueba
            System.err.println("Error en la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
