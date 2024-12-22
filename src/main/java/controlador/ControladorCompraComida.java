package controlador;

import dao.CompraComidaDAO;
import dao.CompraComidaDAOImpl;
import modelo.CompraComida;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controlador para manejar las operaciones relacionadas con la entidad CompraComida.
 *
 * **Responsabilidades:**
 * - Validar datos antes de enviarlos al DAO.
 * - Coordinar la interacción entre la vista y el modelo (DAO).
 * - Realizar operaciones CRUD en la base de datos mediante la capa DAO.
 *
 * **Características:**
 * - Integra logging para monitorear operaciones y errores.
 * - Garantiza que las validaciones y reglas de negocio se cumplan antes de interactuar con el DAO.
 *
 * **Requisitos:**
 * - Configuración del DAO `CompraComidaDAO`.
 * - Uso de la clase modelo `CompraComida`.
 *
 * **Buenas prácticas aplicadas:**
 * - Validación de entrada.
 * - Gestión de excepciones detallada.
 * - Cierre de recursos del DAO para evitar fugas de memoria.
 *
 * @author Diego Diaz
 * @version 1.2
 * @since 2024
 */
public class ControladorCompraComida {

    private static final Logger logger = LoggerFactory.getLogger(ControladorCompraComida.class);
    private final CompraComidaDAO compraComidaDAO;

    /**
     * Constructor que inicializa el DAO de CompraComida.
     * Asegura que los recursos de DAO estén configurados correctamente.
     */
    public ControladorCompraComida() {
        this.compraComidaDAO = new CompraComidaDAOImpl();
        logger.info("ControladorCompraComida inicializado.");
    }

    /**
     * Agrega un nuevo registro de CompraComida a la base de datos.
     *
     * @param nombreProducto Nombre del producto comprado (no puede ser nulo o vacío).
     * @param descripcion Descripción del producto (no puede ser nula o vacía).
     * @param foto Indica si el producto tiene una foto asociada.
     * @param cantidad Cantidad de productos comprados (debe ser mayor que cero).
     * @param realizado Indica si la compra ha sido realizada.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean agregarCompra(String nombreProducto, String descripcion, boolean foto, int cantidad, boolean realizado) {
        try {
            // Validaciones de entrada
            validarNombreProducto(nombreProducto);
            validarDescripcion(descripcion);
            validarCantidad(cantidad);

            // Obtener el último número de foto si es necesario
            Integer numeroUnicoFoto = null;
            if (foto) {
                numeroUnicoFoto = compraComidaDAO.obtenerUltimoNumeroFoto() + 1;
                logger.info("Generado número único para foto: {}", numeroUnicoFoto);
            }

            // Crear instancia del modelo
            CompraComida compra = new CompraComida(nombreProducto, descripcion, foto, numeroUnicoFoto, cantidad, realizado);

            // Llamar al DAO para guardar el registro
            boolean resultado = compraComidaDAO.agregarCompra(compra);

            if (resultado) {
                logger.info("CompraComida agregada exitosamente: {}", compra);
            }
            return resultado;

        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al agregar CompraComida: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error al agregar CompraComida.", e);
            return false;
        }
    }

    /**
     * Obtiene todos los registros de CompraComida desde la base de datos.
     *
     * @return Lista de objetos CompraComida, o null si ocurre un error.
     */
    public List<CompraComida> obtenerTodasLasCompras() {
        try {
            List<CompraComida> compras = compraComidaDAO.obtenerTodasLasCompras();
            if (compras != null) {
                logger.info("Se recuperaron {} compras de CompraComida.", compras.size());
            }
            return compras;
        } catch (Exception e) {
            logger.error("Error al obtener todas las compras: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza un registro existente de CompraComida en la base de datos.
     *
     * @param compra Objeto CompraComida con los datos actualizados (no puede ser nulo).
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean actualizarCompra(CompraComida compra) {
        try {
            if (compra == null || compra.getIdUnico() <= 0) {
                logger.error("El objeto CompraComida es nulo o tiene un identificador inválido.");
                throw new IllegalArgumentException("El objeto CompraComida es nulo o tiene un identificador inválido.");
            }

            boolean resultado = compraComidaDAO.actualizarCompra(compra);
            if (resultado) {
                logger.info("CompraComida actualizada exitosamente: {}", compra);
            }
            return resultado;

        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al actualizar CompraComida: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error al actualizar CompraComida.", e);
            return false;
        }
    }

    /**
     * Elimina un registro de CompraComida en la base de datos.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean eliminarCompra(int idUnico) {
        try {
            if (idUnico <= 0) {
                logger.error("El identificador único debe ser mayor que cero.");
                throw new IllegalArgumentException("El identificador único debe ser mayor que cero.");
            }

            boolean resultado = compraComidaDAO.eliminarCompra(idUnico);
            if (resultado) {
                logger.info("CompraComida con IdUnico={} eliminada exitosamente.", idUnico);
            }
            return resultado;

        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al eliminar CompraComida: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error al eliminar CompraComida con IdUnico={}.", idUnico, e);
            return false;
        }
    }

    /**
     * Cierra los recursos utilizados por el DAO para liberar memoria y evitar fugas de recursos.
     */
    public void cerrar() {
        try {
            compraComidaDAO.cerrar();
            logger.info("Recursos del DAO cerrados correctamente.");
        } catch (Exception e) {
            logger.error("Error al cerrar los recursos del DAO.", e);
        }
    }

    // Métodos privados de validación

    private void validarNombreProducto(String nombreProducto) {
        if (nombreProducto == null || nombreProducto.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío.");
        }
    }

    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía.");
        }
    }

    private void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
    }
}
