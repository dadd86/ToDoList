package controlador;

import modelo.ComprarVarios;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controlador DAO para manejar las operaciones de la entidad CompraVarios usando Hibernate.
 *
 * Este controlador gestiona operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en la base de datos,
 * utilizando sesiones Hibernate para interactuar con la tabla CompraVarios.
 *
 * **Responsabilidades:**
 * - Crear nuevas entradas en la tabla CompraVarios.
 * - Leer datos existentes.
 * - Actualizar registros.
 * - Eliminar registros.
 *
 * **Requisitos:**
 * - Configuración de Hibernate en el archivo `hibernate.cfg.xml`.
 * - La entidad `ComprarVarios` debe estar correctamente mapeada con anotaciones JPA.
 *
 * @author [Tu Nombre]
 * @version 1.0
 * @since 2024
 */
public class ControladorCompraVarios {

    private static final Logger logger = LoggerFactory.getLogger(ControladorCompraVarios.class);
    private final SessionFactory sessionFactory;

    /**
     * Constructor que inicializa el SessionFactory para manejar sesiones de Hibernate.
     */
    public ControladorCompraVarios() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(ComprarVarios.class).buildSessionFactory();
    }

    /**
     * Agrega un nuevo registro a la tabla CompraVarios.
     *
     * @param compraVarios Objeto CompraVarios con los datos a insertar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean agregarCompra(ComprarVarios compraVarios) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(compraVarios);
            transaction.commit();
            logger.info("CompraVarios agregado exitosamente: {}", compraVarios);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al agregar CompraVarios: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los registros de la tabla CompraVarios.
     *
     * @return Lista de objetos CompraVarios.
     */
    @SuppressWarnings("unchecked")
    public List<ComprarVarios> obtenerTodasLasCompras() {
        try (Session session = sessionFactory.openSession()) {
            List<ComprarVarios> compras = session.createQuery("FROM ComprarVarios").list();
            logger.info("Se recuperaron {} compras de CompraVarios.", compras.size());
            return compras;
        } catch (Exception e) {
            logger.error("Error al obtener todas las compras: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza un registro existente en la tabla CompraVarios.
     *
     * @param compraVarios Objeto CompraVarios con los datos actualizados.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean actualizarCompra(ComprarVarios compraVarios) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(compraVarios);
            transaction.commit();
            logger.info("CompraVarios actualizada exitosamente: {}", compraVarios);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al actualizar CompraVarios: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un registro de la tabla CompraVarios.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean eliminarCompra(int idUnico) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            ComprarVarios compra = session.get(ComprarVarios.class, idUnico);
            if (compra != null) {
                session.delete(compra);
                transaction.commit();
                logger.info("CompraVarios con IdUnico={} eliminada exitosamente.", idUnico);
                return true;
            } else {
                logger.warn("No se encontró CompraVarios con IdUnico={}.", idUnico);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al eliminar CompraVarios con IdUnico={}: {}", idUnico, e.getMessage());
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
