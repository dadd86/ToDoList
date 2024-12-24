package dao;

import Util.HibernateUtil;
import excepciones.CompraComidaNotFoundException;
import modelo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementación de la interfaz CompraComidaDAO utilizando Hibernate.
 * <p>Esta clase gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la entidad CompraComida
 * y maneja las transacciones de manera segura y eficiente.</p>
 *
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *     <li>Realizar operaciones CRUD sobre la entidad CompraComida.</li>
 *     <li>Gestionar transacciones de Hibernate de manera segura.</li>
 *     <li>Registrar eventos significativos y errores mediante logs para facilitar el diagnóstico.</li>
 * </ul>
 *
 * <p><strong>Mejoras en Seguridad y Robustez:</strong></p>
 * <ul>
 *     <li>Validación estricta de los parámetros antes de realizar operaciones en la base de datos.</li>
 *     <li>Uso de transacciones para asegurar la integridad de los datos.</li>
 *     <li>Manejo centralizado de excepciones con mensajes claros para facilitar la resolución de problemas.</li>
 * </ul>
 *
 * <p><strong>Requisitos:</strong></p>
 * <ul>
 *     <li>Archivo de configuración `hibernate.cfg.xml` correctamente configurado.</li>
 *     <li>Entidad `CompraComida` definida con anotaciones de Hibernate.</li>
 * </ul>
 *
 * @author Diego Diaz
 * @version 1.5
 * @since 2024
 */
public class CompraComidaDAOImpl implements CompraComidaDAO {

    private static final Logger logger = LoggerFactory.getLogger(CompraComidaDAOImpl.class);
    private final SessionFactory sessionFactory;

    /**
     * Constructor que inicializa el `SessionFactory` para manejar sesiones de Hibernate.
     *
     * <p><strong>Validaciones:</strong></p>
     * <ul>
     *     <li>Si el archivo de configuración `hibernate.cfg.xml` es inválido, se lanza una excepción.</li>
     * </ul>
     */
    public CompraComidaDAOImpl() {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();  // Utiliza el HibernateUtil para la sesión.
            logger.info("SessionFactory inicializado correctamente.");
        } catch (Exception e) {
            logger.error("Error al inicializar el SessionFactory: {}", e.getMessage());
            throw new RuntimeException("Error al inicializar Hibernate", e);
        }
    }

    /**
     * Agrega un nuevo registro de CompraComida.
     *
     * <p><strong>Validaciones:</strong></p>
     * <ul>
     *     <li>Los datos de `CompraComida` son validados antes de realizar la inserción.</li>
     * </ul>
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si ocurre un error durante la operación, se registra el error y se revierte la transacción.</li>
     * </ul>
     *
     * @param compraComida Objeto `CompraComida` con los datos a insertar.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    @Override
    public boolean agregarCompra(CompraComida compraComida) {
        validarCompraComida(compraComida);  // Validación previa
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(compraComida);
            transaction.commit();
            logger.info("CompraComida agregada exitosamente: {}", compraComida);
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al agregar CompraComida: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los registros de CompraComida desde la base de datos.
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si ocurre un error, se registra el error y se retorna `null`.</li>
     * </ul>
     *
     * @return Lista de objetos `CompraComida`, o `null` si ocurre un error.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<CompraComida> obtenerTodasLasCompras() {
        try (Session session = sessionFactory.openSession()) {
            List<CompraComida> compras = session.createQuery("from CompraComida", CompraComida.class).list();
            logger.info("Se recuperaron {} compras de CompraComida.", compras.size());
            return compras;
        } catch (Exception e) {
            logger.error("Error al obtener todas las compras: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza un registro existente de CompraComida en la base de datos.
     *
     * <p><strong>Validaciones:</strong></p>
     * <ul>
     *     <li>El objeto `CompraComida` debe ser no nulo y tener un identificador válido.</li>
     * </ul>
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si ocurre un error, se registra y se revierte la transacción.</li>
     * </ul>
     *
     * @param compraComida Objeto `CompraComida` con los datos actualizados.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    @Override
    public boolean actualizarCompra(CompraComida compraComida) {
        validarCompraComida(compraComida);  // Validación previa
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(compraComida);
            transaction.commit();
            logger.info("CompraComida actualizada exitosamente: {}", compraComida);
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar CompraComida: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un registro existente de CompraComida en la base de datos.
     *
     * <p><strong>Validaciones:</strong></p>
     * <ul>
     *     <li>El identificador debe ser mayor que cero.</li>
     * </ul>
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si el registro no existe, lanza una excepción específica.</li>
     *     <li>Si ocurre otro error, se revierte la transacción y se registra el error.</li>
     * </ul>
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    @Override
    public boolean eliminarCompra(int idUnico) {
        if (idUnico <= 0) {
            throw new IllegalArgumentException("El identificador único debe ser mayor que cero.");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CompraComida compra = session.get(CompraComida.class, idUnico);
            if (compra == null) {
                throw new CompraComidaNotFoundException("No se encontró CompraComida con IdUnico=" + idUnico);
            }
            session.delete(compra);
            transaction.commit();
            logger.info("CompraComida con IdUnico={} eliminada exitosamente.", idUnico);
            return true;
        } catch (CompraComidaNotFoundException e) {
            logger.error("Error: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al eliminar CompraComida: {}", e.getMessage());
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
     * Cierra el `SessionFactory` para liberar recursos.
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si ocurre un error al cerrar el recurso, este es registrado.</li>
     * </ul>
     */
    @Override
    public void cerrar() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
                logger.info("SessionFactory cerrado correctamente.");
            } catch (Exception e) {
                logger.error("Error al cerrar SessionFactory: {}", e.getMessage());
            }
        }
    }

    /**
     * Valida los datos del objeto CompraComida antes de interactuar con la base de datos.
     *
     * <p><strong>Validaciones:</strong></p>
     * <ul>
     *     <li>El nombre del producto no puede ser nulo o vacío.</li>
     *     <li>La descripción no puede ser nula o vacía.</li>
     *     <li>La cantidad debe ser mayor que cero.</li>
     * </ul>
     *
     * @param compraComida Objeto a validar.
     * @throws IllegalArgumentException Si alguno de los campos no cumple con las restricciones.
     */
    private void validarCompraComida(CompraComida compraComida) {
        if (compraComida == null) {
            throw new IllegalArgumentException("El objeto CompraComida no puede ser nulo.");
        }
        if (compraComida.getNombreProducto() == null || compraComida.getNombreProducto().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío.");
        }
        if (compraComida.getDescripcion() == null || compraComida.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía.");
        }
        if (compraComida.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
    }
}
