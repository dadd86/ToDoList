package dao;

import dao.CompraLimpiezaDAO;
import modelo.CompraLimpieza;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementación de la interfaz CompraLimpiezaDAO utilizando Hibernate.
 *
 * Maneja las operaciones CRUD de la entidad CompraLimpieza con el uso de sesiones Hibernate.
 *
 * **Responsabilidades:**
 * - Implementar las operaciones definidas en CompraLimpiezaDAO.
 * - Manejar transacciones de manera segura.
 *
 * **Requisitos:**
 * - Configuración de Hibernate en el archivo `hibernate.cfg.xml`.
 * - La entidad `CompraLimpieza` debe estar correctamente mapeada con anotaciones JPA.
 *
 * @author [Tu Nombre]
 * @version 1.0
 * @since 2024
 */
public class CompraLimpiezaDAOImpl implements CompraLimpiezaDAO {

    private static final Logger logger = LoggerFactory.getLogger(CompraLimpiezaDAOImpl.class);
    private final SessionFactory sessionFactory;

    /**
     * Constructor que inicializa el SessionFactory para manejar sesiones de Hibernate.
     */
    public CompraLimpiezaDAOImpl() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(CompraLimpieza.class)
                .buildSessionFactory();
    }

    @Override
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

    @Override
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
                logger.warn("No se encontró CompraLimpieza con IdUnico={}.", idUnico);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al eliminar CompraLimpieza con IdUnico={}: {}", idUnico, e.getMessage());
            return false;
        }
    }

    @Override
    public void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
            logger.info("SessionFactory cerrado correctamente.");
        }
    }
}
