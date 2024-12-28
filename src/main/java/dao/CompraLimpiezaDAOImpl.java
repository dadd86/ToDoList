package dao;

import modelo.CompraLimpieza;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementación de la interfaz `CompraLimpiezaDAO` utilizando Hibernate.
 *
 * **Responsabilidades:**
 * - Implementar las operaciones CRUD definidas en la interfaz `CompraLimpiezaDAO`.
 * - Utilizar Hibernate para interactuar con la base de datos.
 *
 * @version 1.0
 * @since 2024
 */
public class CompraLimpiezaDAOImpl implements CompraLimpiezaDAO {

    private static final Logger logger = LoggerFactory.getLogger(CompraLimpiezaDAOImpl.class);
    private final SessionFactory sessionFactory;

    /**
     * Constructor que inicializa el `SessionFactory` para manejar sesiones de Hibernate.
     */
    public CompraLimpiezaDAOImpl() {
        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(CompraLimpieza.class).buildSessionFactory();
        logger.info("CompraLimpiezaDAOImpl inicializado.");
    }

    /**
     * Agrega un nuevo registro de CompraLimpieza a la base de datos.
     *
     * @param compra Objeto CompraLimpieza a agregar.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    @Override
    public boolean agregarCompra(CompraLimpieza compra) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(compra);
            transaction.commit();
            logger.info("CompraLimpieza agregada exitosamente: {}", compra);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al agregar CompraLimpieza: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los registros de CompraLimpieza desde la base de datos.
     *
     * @return Lista de objetos CompraLimpieza, o `null` si ocurre un error.
     */
    @Override
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
     * Actualiza un registro existente de CompraLimpieza en la base de datos.
     *
     * @param compra Objeto CompraLimpieza con los datos actualizados.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    @Override
    public boolean actualizarCompra(CompraLimpieza compra) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(compra);
            transaction.commit();
            logger.info("CompraLimpieza actualizada exitosamente: {}", compra);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al actualizar CompraLimpieza: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un registro de CompraLimpieza en la base de datos.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    @Override
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
                logger.error("CompraLimpieza con IdUnico={} no encontrada.", idUnico);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al eliminar CompraLimpieza con IdUnico={}: {}", idUnico, e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el último número único de foto registrado en la base de datos.
     *
     * @return El último número único de foto, o `0` si no existen registros.
     */
    @Override
    public int obtenerUltimoNumeroFoto() {
        int ultimoNumero = 0;
        try (Session session = sessionFactory.openSession()) {
            Integer maxNumero = (Integer) session.createQuery("SELECT MAX(c.numeroUnicoFoto) FROM CompraLimpieza c").uniqueResult();
            ultimoNumero = maxNumero != null ? maxNumero : 0;
            logger.info("Último número único de foto obtenido: {}", ultimoNumero);
        } catch (Exception e) {
            logger.error("Error al obtener el último número único de foto: {}", e.getMessage());
        }
        return ultimoNumero;
    }

    /**
     * Cierra el `SessionFactory` para liberar recursos y evitar fugas de memoria.
     */
    @Override
    public void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
            logger.info("SessionFactory cerrado correctamente.");
        }
    }
}
