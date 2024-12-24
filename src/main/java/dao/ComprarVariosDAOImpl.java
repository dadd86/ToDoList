package dao;

import modelo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import Util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementación de la interfaz CompraVariosDAO utilizando Hibernate.
 *
 * Esta clase maneja las operaciones CRUD de la entidad ComprarVarios usando Hibernate.
 *
 * @author [Tu Nombre]
 * @version 1.0
 * @since 2024
 */
public class ComprarVariosDAOImpl implements ComprarVariosDAO {

    private static final Logger logger = LoggerFactory.getLogger(ComprarVariosDAOImpl.class);
    private final SessionFactory sessionFactory;

    /**
     * Constructor que inicializa el SessionFactory para manejar sesiones de Hibernate.
     *
     * Este constructor utiliza `HibernateUtil.getSessionFactory()` para obtener la instancia de la fábrica de sesiones.
     */
    public ComprarVariosDAOImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("ComprarVariosDAOImpl inicializado correctamente.");
    }

    /**
     * Agrega un nuevo registro de CompraVarios a la base de datos.
     *
     * @param compra Objeto CompraVarios a agregar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    @Override
    public boolean agregarCompra(ComprarVarios compra) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(compra);
            transaction.commit();
            logger.info("CompraVarios agregada exitosamente: {}", compra);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al agregar CompraVarios: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los registros de CompraVarios desde la base de datos.
     *
     * @return Lista de objetos CompraVarios, o null si ocurre un error.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ComprarVarios> obtenerTodasLasCompras() {
        try (Session session = sessionFactory.openSession()) {
            Query<ComprarVarios> query = session.createQuery("from ComprarVarios", ComprarVarios.class);
            List<ComprarVarios> compras = query.list();
            logger.info("Se recuperaron {} registros de CompraVarios.", compras.size());
            return compras;
        } catch (Exception e) {
            logger.error("Error al obtener todas las compras de CompraVarios: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza un registro existente de CompraVarios en la base de datos.
     *
     * @param compra Objeto CompraVarios con los datos actualizados.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    @Override
    public boolean actualizarCompra(ComprarVarios compra) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(compra);
            transaction.commit();
            logger.info("CompraVarios actualizada exitosamente: {}", compra);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al actualizar CompraVarios: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un registro de CompraVarios de la base de datos.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    @Override
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
                logger.error("No se encontró CompraVarios con IdUnico={}.", idUnico);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al eliminar CompraVarios con IdUnico={}: {}", idUnico, e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el último número único de foto registrado en la base de datos.
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si ocurre un error, se registra y retorna `0`.</li>
     * </ul>
     *
     * @return El valor máximo de `numeroUnicoFoto`, o `0` si no hay registros.
     */
    @Override
    public int obtenerUltimoNumeroFoto() {
        int ultimoNumero = 0;
        try (Session session = sessionFactory.openSession()) {
            Integer maxNumero = (Integer) session.createQuery("SELECT MAX(c.numeroUnicoFoto) FROM CompraComida c")
                    .uniqueResult();
            ultimoNumero = maxNumero != null ? maxNumero : 0;
            logger.info("Último número único de foto obtenido: {}", ultimoNumero);
        } catch (Exception e) {
            logger.error("Error al obtener el último número único de foto: {}", e.getMessage());
        }
        return ultimoNumero;
    }

    /**
     * Cierra los recursos utilizados por el DAO para liberar memoria y evitar fugas de recursos.
     *
     * Este método asegura que los recursos se liberen correctamente después de su uso.
     */
    @Override
    public void cerrar() {
        try {
            sessionFactory.close();
            logger.info("SessionFactory cerrado correctamente.");
        } catch (Exception e) {
            logger.error("Error al cerrar SessionFactory: {}", e.getMessage());
        }
    }
}
