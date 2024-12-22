package controlador;

import modelo.CompraLimpieza;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controlador DAO para manejar las operaciones de la entidad CompraLimpieza usando Hibernate.
 *
 * Este controlador gestiona operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en la base de datos,
 * utilizando sesiones Hibernate para interactuar con la tabla CompraLimpieza.
 *
 * **Responsabilidades:**
 * - Crear nuevas entradas en la tabla CompraLimpieza.
 * - Leer datos existentes.
 * - Actualizar registros.
 * - Eliminar registros.
 *
 * **Requisitos:**
 * - Configuración de Hibernate en el archivo `hibernate.cfg.xml`.
 * - La entidad `CompraLimpieza` debe estar correctamente mapeada con anotaciones JPA.
 *
 * @author [Tu Nombre]
 * @version 1.0
 * @since 2024
 */
public class ControladorCompraLimpieza {

    private static final Logger logger = LoggerFactory.getLogger(ControladorCompraLimpieza.class);
    private final SessionFactory sessionFactory;

    /**
     * Constructor que inicializa el SessionFactory para manejar sesiones de Hibernate.
     */
    public ControladorCompraLimpieza() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(CompraLimpieza.class)
                .buildSessionFactory();
    }

    /**
     * Agrega un nuevo registro a la tabla CompraLimpieza.
     *
     * @param compraLimpieza Objeto CompraLimpieza con los datos a insertar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean agregarCompra(CompraLimpieza compraLimpieza) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(compraLimpieza);
            transaction.commit();
            logger.info("CompraLimpieza agregada exitosamente: {}", compraLimpieza);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al agregar CompraLimpieza: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los registros de la tabla CompraLimpieza.
     *
     * @return Lista de objetos CompraLimpieza.
     */
    @SuppressWarnings("unchecked")
    public List<CompraLimpieza> obtenerTodasLasCompras() {
        try (Session session = sessionFactory.openSession()) {
            List<CompraLimpieza> compras = session.createQuery("FROM CompraLimpieza").list();
            logger.info("Se recuperaron {} compras de CompraLimpieza.", compras.size());
            return compras;
        } catch (Exception e) {
            logger.error("Error al obtener todas las compras: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza un registro existente en la tabla CompraLimpieza.
     *
     * @param compraLimpieza Objeto CompraLimpieza con los datos actualizados.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean actualizarCompra(CompraLimpieza compraLimpieza) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(compraLimpieza);
            transaction.commit();
            logger.info("CompraLimpieza actualizada exitosamente: {}", compraLimpieza);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al actualizar CompraLimpieza: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un registro de la tabla CompraLimpieza.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean eliminarCompra(int idUnico) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CompraLimpieza compra = session.get(CompraLimpieza.class, idUnico);
            if (compra != null) {
                session.delete(compra);
                transaction.commit();
                logger.info("CompraLimpieza con IdUnico={} eliminada exitosamente.", idUnico);
                return true;
            } else {
                logger.warn("No se encontró CompraLimpieza con IdUnico={}.", idUnico);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al eliminar CompraLimpieza con IdUnico={}: {}", idUnico, e.getMessage());
            return false;
        }
    }

    /**
     * Cierra el SessionFactory para liberar recursos.
     */
    public void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
            logger.info("SessionFactory cerrado correctamente.");
        }
    }
}
