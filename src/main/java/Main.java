import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase principal para probar la conexión a la base de datos MySQL utilizando Hibernate.
 *
 * **Objetivos:**
 * - Validar la configuración de Hibernate.
 * - Comprobar que la conexión con la base de datos es exitosa.
 * - Registrar cualquier error o éxito en los logs.
 *
 * **Requisitos:**
 * - Archivo `hibernate.cfg.xml` correctamente configurado.
 * - Dependencias de Hibernate y MySQL en el proyecto.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Inicialización del SessionFactory
        SessionFactory sessionFactory = null;
        try {
            logger.info("Iniciando prueba de conexión a MySQL con Hibernate...");

            // Crear el SessionFactory a partir de la configuración
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml") // Archivo de configuración de Hibernate
                    .buildSessionFactory();

            // Probar apertura de sesión
            try (Session session = sessionFactory.openSession()) {
                logger.info("Conexión exitosa con la base de datos.");
            } catch (Exception e) {
                logger.error("Error al abrir la sesión con la base de datos: {}", e.getMessage());
            }

        } catch (Exception e) {
            logger.error("Error al inicializar Hibernate o conectar con la base de datos: {}", e.getMessage());
        } finally {
            // Cerrar el SessionFactory para liberar recursos
            if (sessionFactory != null) {
                sessionFactory.close();
                logger.info("SessionFactory cerrado correctamente.");
            }
        }
    }
}
