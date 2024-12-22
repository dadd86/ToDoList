package dao;

import excepciones.CompraComidaNotFoundException;
import modelo.CompraComida;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementación de la interfaz CompraComidaDAO utilizando Hibernate.
 *
 * **Responsabilidades:**
 * - Realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en la entidad CompraComida.
 * - Manejar transacciones de manera segura y eficiente utilizando Hibernate.
 * - Registrar operaciones y errores en logs para facilitar la depuración.
 *
 * **Características:**
 * - Uso de `try-with-resources` para cerrar recursos de forma automática.
 * - Validación estricta de datos de entrada.
 * - Robustez frente a excepciones, con mensajes detallados y consistentes.
 *
 * **Requisitos:**
 * - Archivo de configuración `hibernate.cfg.xml` correctamente configurado.
 * - Entidad `CompraComida` definida con anotaciones Hibernate.
 *
 * @author Diego Diaz
 * @version 1.3
 * @since 2024
 */
public class CompraComidaDAOImpl implements CompraComidaDAO {

    private static final Logger logger = LoggerFactory.getLogger(CompraComidaDAOImpl.class);
    private final SessionFactory sessionFactory;

    /**
     * Constructor que inicializa el SessionFactory para manejar sesiones de Hibernate.
     *
     * **Errores Manejados:**
     * - Si el archivo de configuración no es válido, se lanza una excepción que detiene el proceso.
     */
    public CompraComidaDAOImpl() {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(CompraComida.class)
                    .buildSessionFactory();
            logger.info("SessionFactory inicializado correctamente.");
        } catch (Exception e) {
            logger.error("Error al inicializar el SessionFactory: {}", e.getMessage());
            throw new RuntimeException("Error al inicializar Hibernate", e);
        }
    }

    /**
     * Agrega un nuevo registro a la tabla `CompraComida`.
     *
     * **Validación de entrada:**
     * - Los datos son validados antes de enviarse a la base de datos.
     *
     * **Gestión de excepciones:**
     * - Si ocurre un error durante la operación, se registra en los logs y la transacción es revertida.
     *
     * @param compraComida Objeto CompraComida con los datos a insertar.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    @Override
    public boolean agregarCompra(CompraComida compraComida) {
        validarCompraComida(compraComida);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(compraComida);
            transaction.commit();
            logger.info("CompraComida agregada exitosamente: {}", compraComida);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al agregar CompraComida: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los registros de la tabla `CompraComida`.
     *
     * **Gestión de excepciones:**
     * - Registra cualquier error ocurrido durante la operación.
     *
     * @return Lista de objetos CompraComida, o `null` si ocurre un error.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<CompraComida> obtenerTodasLasCompras() {
        try (Session session = sessionFactory.openSession()) {
            List<CompraComida> compras = session.createQuery("FROM CompraComida", CompraComida.class).list();
            logger.info("Se recuperaron {} compras de CompraComida.", compras.size());
            return compras;
        } catch (Exception e) {
            logger.error("Error al obtener todas las compras: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza un registro existente en la tabla `CompraComida`.
     *
     * **Validación de entrada:**
     * - Se valida que el objeto no sea nulo y tenga un ID válido.
     *
     * **Gestión de excepciones:**
     * - Registra errores y revierte la transacción si ocurre un problema.
     *
     * @param compraComida Objeto CompraComida con los datos actualizados.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    @Override
    public boolean actualizarCompra(CompraComida compraComida) {
        validarCompraComida(compraComida);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(compraComida);
            transaction.commit();
            logger.info("CompraComida actualizada exitosamente: {}", compraComida);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al actualizar CompraComida: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un registro de la tabla `CompraComida`.
     *
     * **Validación de entrada:**
     * - Se asegura que el ID sea mayor que cero.
     *
     * **Gestión de excepciones:**
     * - Si el registro no existe, se lanza una excepción específica.
     * - Registra errores y revierte la transacción en caso de problemas.
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
            if (transaction != null) transaction.rollback();
            logger.error("Error al eliminar CompraComida: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el último número único de foto almacenado en la tabla `CompraComida`.
     *
     * **Gestión de excepciones:**
     * - Si ocurre un error, se registra en los logs.
     *
     * @return El valor máximo de `numeroUnicoFoto` o `0` si no hay registros.
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
     * **Gestión de excepciones:**
     * - Si ocurre un error durante el cierre, este se registra.
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
     * Valida los datos de un objeto CompraComida antes de interactuar con la base de datos.
     *
     * **Validación:**
     * - Los campos obligatorios no deben ser nulos o vacíos.
     * - La cantidad debe ser mayor que 0.
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
