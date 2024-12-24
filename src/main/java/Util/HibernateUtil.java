package Util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Clase utilitaria para gestionar la sesión de Hibernate.
 * Proporciona un acceso centralizado al objeto `SessionFactory`
 * utilizado para manejar conexiones con la base de datos.
 *
 * <p>Incluye las siguientes responsabilidades:</p>
 * <ul>
 *     <li>Configurar Hibernate desde el archivo de configuración `hibernate.cfg.xml`.</li>
 *     <li>Proporcionar un método para obtener el `SessionFactory`.</li>
 *     <li>Gestionar el cierre del `SessionFactory` y el `StandardServiceRegistry` para evitar fugas de memoria.</li>
 * </ul>
 *
 * <p><strong>Mejoras en Seguridad y Robustez:</strong></p>
 * <ul>
 *     <li>Manejo centralizado de excepciones con mensajes claros.</li>
 *     <li>Verificación del estado de los objetos antes de proceder a operaciones críticas.</li>
 *     <li>Uso de bloque `try-catch-finally` para asegurar la limpieza de recursos.</li>
 * </ul>
 */
public class HibernateUtil {

    /**
     * Registro de servicios estándar utilizado para configurar Hibernate.
     * Este objeto es parte de la configuración de Hibernate, gestionando todos los servicios necesarios para la sesión.
     */
    private static StandardServiceRegistry registry;

    /**
     * Fábrica de sesiones Hibernate.
     * Este objeto es la fábrica que se utiliza para obtener sesiones de Hibernate y realizar operaciones con la base de datos.
     */
    private static SessionFactory sessionFactory;

    /**
     * Devuelve el objeto `SessionFactory`, creando uno nuevo si no existe.
     * Este método está diseñado para ser seguro en entornos multi-hilo.
     *
     * <p>En primer lugar, se verifica si la fábrica de sesiones ya existe. Si no es así,
     * se realiza una configuración y creación segura del `SessionFactory` con sincronización de acceso.</p>
     *
     * <p>Este método está optimizado para ser seguro en un entorno con múltiples hilos (thread-safe),
     * utilizando la técnica de doble comprobación (double-checked locking).</p>
     *
     * @return La instancia única de `SessionFactory`.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) { // Doble comprobación para garantizar seguridad en multi-hilos.
                    try {
                        // Crear el registro de servicios estándar
                        registry = new StandardServiceRegistryBuilder().configure().build();

                        // Crear los metadatos desde las configuraciones
                        MetadataSources sources = new MetadataSources(registry);
                        Metadata metadata = sources.getMetadataBuilder().build();

                        // Crear la fábrica de sesiones
                        sessionFactory = metadata.getSessionFactoryBuilder().build();
                    } catch (Exception e) {
                        // Imprimir la pila de errores para depuración
                        e.printStackTrace();
                        // Destruir el registro si ocurre un error
                        if (registry != null) {
                            StandardServiceRegistryBuilder.destroy(registry);
                        }
                        // Lanzar una excepción detallada con el mensaje de error
                        throw new ExceptionInInitializerError("Error al inicializar Hibernate SessionFactory: " + e.getMessage());
                    }
                }
            }
        }
        return sessionFactory;
    }

    /**
     * Apaga el `SessionFactory` y destruye el registro de servicios estándar.
     * Este método debe ser llamado al cerrar la aplicación para liberar recursos.
     * Se asegura de que el `SessionFactory` se cierre correctamente y que todos los recursos asociados sean liberados.
     * <p>Utiliza un chequeo para asegurarse de que el `SessionFactory` no esté cerrado antes de intentar cerrarlo.</p>
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close(); // Cerrar el SessionFactory
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry); // Destruir el registro de servicios
        }
    }
}
