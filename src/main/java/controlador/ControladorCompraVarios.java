package controlador;

import modelo.CompraComida;
import modelo.ComprarVarios;
import dao.ComprarVariosDAO;
import dao.ComprarVariosDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controlador DAO para manejar las operaciones de la entidad CompraVarios usando Hibernate.
 *
 * <p>Este controlador gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en la base de datos
 * utilizando sesiones Hibernate para interactuar con la tabla CompraVarios.</p>
 *
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *     <li>Crear nuevas entradas en la tabla CompraVarios.</li>
 *     <li>Leer datos existentes.</li>
 *     <li>Actualizar registros.</li>
 *     <li>Eliminar registros.</li>
 * </ul>
 *
 * <p><strong>Requisitos:</strong></p>
 * <ul>
 *     <li>Configuración de Hibernate en el archivo `hibernate.cfg.xml`.</li>
 *     <li>La entidad `ComprarVarios` debe estar correctamente mapeada con anotaciones JPA.</li>
 * </ul>
 *
 * <p><strong>Mejoras en Seguridad y Robustez:</strong></p>
 * <ul>
 *     <li>Se validan los parámetros de entrada para garantizar que no se introduzcan valores no válidos.</li>
 *     <li>Uso de transacciones para garantizar la coherencia de las operaciones en la base de datos.</li>
 *     <li>Se manejan excepciones de forma centralizada para registrar los errores y mantener la estabilidad.</li>
 * </ul>
 *
 * @author Diego Diaz
 * @version 1.0
 * @since 2024
 */
public class ControladorCompraVarios {

    // Logger para registrar eventos y errores de forma centralizada
    private static final Logger logger = LoggerFactory.getLogger(ControladorCompraVarios.class);
    private final ComprarVariosDAO comprarVariosDAO;

    /**
     * Constructor que inicializa el controlador y la instancia del DAO.
     *
     * <p><strong>Mejora:</strong></p>
     * <ul>
     *     <li>Inicia el DAO para manejar operaciones en la base de datos.</li>
     *     <li>Se asegura de que se pueda gestionar cualquier tipo de excepción al inicializar el controlador.</li>
     * </ul>
     */
    public ControladorCompraVarios() {
        this.comprarVariosDAO = new ComprarVariosDAOImpl();
        logger.info("ControladorCompraVarios inicializado correctamente.");
    }

    /**
     * Agrega un nuevo registro de CompraComida a la base de datos.
     *
     * <p><strong>Validaciones de Entrada:</strong></p>
     * <ul>
     *     <li>Se valida que el nombre, la descripción y la cantidad sean correctos.</li>
     * </ul>
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si ocurre una excepción durante la operación, se captura y se registra el error.</li>
     * </ul>
     *
     * @param nombreProducto Nombre del producto comprado (no puede ser nulo o vacío).
     * @param descripcion Descripción del producto (no puede ser nula o vacía).
     * @param foto Indica si el producto tiene una foto asociada.
     * @param cantidad Cantidad de productos comprados (debe ser mayor que cero).
     * @param realizado Indica si la compra ha sido realizada.
     * @param supermercado Nombre del supermercado donde se realizó la compra.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean agregarCompra(String nombreProducto, String descripcion, boolean foto, int cantidad, boolean realizado, String supermercado) {
        try {
            // Validaciones de entrada
            validarNombreProducto(nombreProducto);
            validarDescripcion(descripcion);
            validarCantidad(cantidad);
            validarSupermercado(supermercado);

            // Obtener el último número de foto si es necesario
            Integer numeroUnicoFoto = null;
            if (foto) {
                numeroUnicoFoto = comprarVariosDAO.obtenerUltimoNumeroFoto() + 1;
                logger.info("Generado número único para foto: {}", numeroUnicoFoto);
            }

            // Crear instancia del modelo
            ComprarVarios compra = new ComprarVarios(nombreProducto, descripcion, foto, numeroUnicoFoto, cantidad, realizado, supermercado);

            // Llamar al DAO para guardar el registro
            boolean resultado = comprarVariosDAO.agregarCompra(compra);

            if (resultado) {
                logger.info("CompraComida agregada exitosamente: {}", compra);
            }
            return resultado;

        } catch (IllegalArgumentException e) {
            // Captura errores de validación de los parámetros de entrada
            logger.error("Error de validación al agregar CompraComida: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            // Captura cualquier otro tipo de error durante la operación
            logger.error("Error al agregar CompraComida.", e);
            return false;
        }
    }

    /**
     * Obtiene todos los registros de CompraComida desde la base de datos.
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si ocurre un error al consultar los registros, se captura y se registra el error.</li>
     * </ul>
     *
     * @return Lista de objetos CompraComida, o null si ocurre un error.
     */
    public List<ComprarVarios> obtenerTodasLasCompras() {
        try {
            List<ComprarVarios> compras = comprarVariosDAO.obtenerTodasLasCompras();
            if (compras != null) {
                logger.info("Se recuperaron {} compras de CompraComida.", compras.size());
            }
            return compras;
        } catch (Exception e) {
            // Captura cualquier error durante la recuperación de los registros
            logger.error("Error al obtener todas las compras: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza un registro existente de CompraComida en la base de datos.
     *
     * <p><strong>Validaciones:</strong></p>
     * <ul>
     *     <li>Se valida que el objeto no sea nulo y que tenga un identificador válido.</li>
     * </ul>
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si ocurre un error, se captura y se revierte la transacción.</li>
     * </ul>
     *
     * @param compra Objeto CompraComida con los datos actualizados (no puede ser nulo).
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean actualizarCompra(ComprarVarios compra) {
        try {
            if (compra == null || compra.getIdUnico() <= 0) {
                logger.error("El objeto CompraComida es nulo o tiene un identificador inválido.");
                throw new IllegalArgumentException("El objeto CompraComida es nulo o tiene un identificador inválido.");
            }

            boolean resultado = comprarVariosDAO.actualizarCompra(compra);
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
     * <p><strong>Validaciones:</strong></p>
     * <ul>
     *     <li>Se valida que el identificador del registro sea mayor que cero.</li>
     * </ul>
     *
     * <p><strong>Gestión de Excepciones:</strong></p>
     * <ul>
     *     <li>Si el registro no existe, se lanza una excepción.</li>
     *     <li>Si ocurre un error, se captura y se revierte la transacción.</li>
     * </ul>
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

            boolean resultado = comprarVariosDAO.eliminarCompra(idUnico);
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
     *
     * <p><strong>Mejoras:</strong></p>
     * <ul>
     *     <li>Se garantiza que los recursos se cierren correctamente, evitando fugas de memoria.</li>
     * </ul>
     */
    public void cerrar() {
        try {
            comprarVariosDAO.cerrar();
            logger.info("Recursos del DAO cerrados correctamente.");
        } catch (Exception e) {
            logger.error("Error al cerrar los recursos del DAO.", e);
        }
    }

    // Métodos privados de validación

    /**
     * Valida el nombre del producto.
     *
     * @param nombreProducto Nombre del producto a validar.
     * @throws IllegalArgumentException Si el nombre es nulo o vacío.
     */
    private void validarNombreProducto(String nombreProducto) {
        if (nombreProducto == null || nombreProducto.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío.");
        }
    }

    /**
     * Valida la descripción del producto.
     *
     * @param descripcion Descripción a validar.
     * @throws IllegalArgumentException Si la descripción es nula o vacía.
     */
    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía.");
        }
    }

    /**
     * Valida la cantidad del producto.
     *
     * @param cantidad Cantidad a validar.
     * @throws IllegalArgumentException Si la cantidad no es mayor a 0.
     */
    private void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
    }

    /**
     * Valida el nombre del supermercado.
     *
     * @param supermercado Nombre del supermercado a validar.
     * @throws IllegalArgumentException Si el supermercado es nulo o vacío.
     */
    private void validarSupermercado(String supermercado) {
        if (supermercado == null || supermercado.isBlank()) {
            logger.error("El nombre del supermercado no puede ser vacío o nulo.");
            throw new IllegalArgumentException("El nombre del supermercado no puede ser vacío o nulo.");
        }
    }
}
