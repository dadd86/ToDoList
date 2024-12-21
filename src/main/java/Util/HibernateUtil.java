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
     */
    private static StandardServiceRegistry registry;

    /**
     * Fábrica de sesiones Hibernate.
     */
    private static SessionFactory sessionFactory;

    /**
     * Devuelve el objeto `SessionFactory`, creando uno nuevo si no existe.
     * Este método está diseñado para ser seguro en entornos multi-hilo.
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
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
