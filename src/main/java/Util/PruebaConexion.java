package Util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de prueba para verificar la conexión a MySQL utilizando Hibernate.
 *
 * **Objetivo:**
 * - Validar la configuración del archivo `hibernate.cfg.xml`.
 * - Asegurarse de que la conexión a la base de datos sea exitosa.
 * - Probar la apertura y cierre de sesiones de Hibernate.
 *
 * **Requisitos:**
 * - Archivo `hibernate.cfg.xml` configurado correctamente.
 * - Dependencias de MySQL y Hibernate en el proyecto.
 */
public class PruebaConexion {

    private static final Logger logger = LoggerFactory.getLogger(PruebaConexion.class);

    public static void main(String[] args) {
        // Inicializar el SessionFactory
        SessionFactory sessionFactory = null;
        try {
            logger.info("Iniciando prueba de conexión con Hibernate...");

            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml") // Archivo de configuración
                    .buildSessionFactory();

            // Abrir una sesión
            try (Session session = sessionFactory.openSession()) {
                logger.info("Conexión exitosa con la base de datos.");
            } catch (Exception e) {
                logger.error("Error al abrir la sesión con la base de datos: {}", e.getMessage());
            }

        } catch (Exception e) {
            logger.error("Error al inicializar el SessionFactory: {}", e.getMessage());
        } finally {
            // Cerrar el SessionFactory
            if (sessionFactory != null) {
                sessionFactory.close();
                logger.info("SessionFactory cerrado correctamente.");
            }
        }
    }
}

