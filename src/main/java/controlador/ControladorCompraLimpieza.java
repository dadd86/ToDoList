package controlador;

import modelo.CompraLimpieza;
import dao.CompraLimpiezaDAO;
import dao.CompraLimpiezaDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controlador DAO para manejar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) de la entidad CompraLimpieza utilizando Hibernate.
 *
 * **Responsabilidades:**
 * - Crear nuevas entradas en la tabla `CompraLimpieza`.
 * - Leer los registros existentes de compras de limpieza.
 * - Actualizar registros de compra en la base de datos.
 * - Eliminar registros de compra de la base de datos.
 *
 * **Mejoras en Seguridad y Robustez:**
 * - Validaciones de entrada estrictas para prevenir la inserción de datos incorrectos.
 * - Manejo centralizado de excepciones con mensajes claros.
 * - Uso de logs para registrar eventos significativos y errores.
 *
 * **Requisitos:**
 * - Configuración adecuada de Hibernate y entidades mapeadas con anotaciones JPA.
 * @author Diego Diaz
 * @version 1.1
 * @since 2024
 */
public class ControladorCompraLimpieza {

    private static final Logger logger = LoggerFactory.getLogger(ControladorCompraLimpieza.class);
    private final CompraLimpiezaDAO compraLimpiezaDAO;

    /**
     * Constructor que inicializa el DAO para realizar operaciones CRUD sobre la entidad CompraLimpieza.
     * Inicializa el logger para registrar eventos y errores.
     */
    public ControladorCompraLimpieza() {
        this.compraLimpiezaDAO = new CompraLimpiezaDAOImpl();  // Inicia el DAO
        logger.info("ControladorCompraLimpieza inicializado correctamente.");
    }

    /**
     * Agrega un nuevo registro de CompraLimpieza a la base de datos.
     *
     * **Validaciones:**
     * - Asegura que los parámetros de entrada no sean nulos o vacíos.
     *
     * **Gestión de Excepciones:**
     * - Si ocurre un error, se captura y se registra mediante el logger.
     *
     * @param nombreProducto Nombre del producto (no puede ser nulo ni vacío).
     * @param descripcion Descripción del producto (no puede ser nula ni vacía).
     * @param foto Indica si el producto tiene una foto asociada.
     * @param cantidad Cantidad de productos comprados (debe ser mayor que 0).
     * @param realizado Indica si la compra ha sido realizada.
     * @param supermercado Nombre del supermercado donde se realizó la compra.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    public boolean agregarCompra(String nombreProducto, String descripcion, boolean foto, int cantidad, boolean realizado, String supermercado) {
        try {
            // Validaciones de entrada
            validarNombreProducto(nombreProducto);
            validarDescripcion(descripcion);
            validarCantidad(cantidad);
            validarSupermercado(supermercado);

            // Generar el número único de foto si es necesario
            Integer numeroUnicoFoto = null;
            if (foto) {
                numeroUnicoFoto = compraLimpiezaDAO.obtenerUltimoNumeroFoto() + 1;  // Obtener el último número y sumarle 1
                logger.info("Número único generado para la foto: {}", numeroUnicoFoto);
            }

            // Crear el objeto de compra
            CompraLimpieza compra = new CompraLimpieza(nombreProducto, descripcion, foto, numeroUnicoFoto, cantidad, realizado, supermercado);

            // Llamar al DAO para agregar la compra
            boolean resultado = compraLimpiezaDAO.agregarCompra(compra);

            if (resultado) {
                logger.info("CompraLimpieza agregada exitosamente: {}", compra);
            }
            return resultado;

        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al agregar CompraLimpieza: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error inesperado al agregar CompraLimpieza.", e);
            return false;
        }
    }

    /**
     * Obtiene todos los registros de CompraLimpieza desde la base de datos.
     *
     * **Gestión de Excepciones:**
     * - Si ocurre un error, se captura y se registra mediante el logger.
     *
     * @return Lista de objetos `CompraLimpieza`, o `null` si ocurre un error.
     */
    public List<CompraLimpieza> obtenerTodasLasCompras() {
        try {
            List<CompraLimpieza> compras = compraLimpiezaDAO.obtenerTodasLasCompras();
            if (compras != null) {
                logger.info("Se recuperaron {} compras de CompraLimpieza.", compras.size());
            }
            return compras;
        } catch (Exception e) {
            logger.error("Error al obtener todas las compras: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza un registro existente de CompraLimpieza en la base de datos.
     *
     * **Validaciones:**
     * - El objeto `CompraLimpieza` no puede ser nulo y debe tener un identificador válido.
     *
     * **Gestión de Excepciones:**
     * - Si ocurre un error, se captura y se registra mediante el logger.
     *
     * @param compra Objeto `CompraLimpieza` con los datos actualizados (no puede ser nulo).
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    public boolean actualizarCompra(CompraLimpieza compra) {
        try {
            if (compra == null || compra.getIdUnico() <= 0) {
                logger.error("El objeto CompraLimpieza es nulo o tiene un identificador inválido.");
                throw new IllegalArgumentException("El objeto CompraLimpieza es nulo o tiene un identificador inválido.");
            }

            boolean resultado = compraLimpiezaDAO.actualizarCompra(compra);
            if (resultado) {
                logger.info("CompraLimpieza actualizada exitosamente: {}", compra);
            }
            return resultado;

        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al actualizar CompraLimpieza: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error al actualizar CompraLimpieza.", e);
            return false;
        }
    }

    /**
     * Elimina un registro de CompraLimpieza en la base de datos.
     *
     * **Validaciones:**
     * - El identificador único debe ser mayor que cero.
     *
     * **Gestión de Excepciones:**
     * - Si ocurre un error, se captura y se registra mediante el logger.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    public boolean eliminarCompra(int idUnico) {
        try {
            if (idUnico <= 0) {
                logger.error("El identificador único debe ser mayor que cero.");
                throw new IllegalArgumentException("El identificador único debe ser mayor que cero.");
            }

            boolean resultado = compraLimpiezaDAO.eliminarCompra(idUnico);
            if (resultado) {
                logger.info("CompraLimpieza con IdUnico={} eliminada exitosamente.", idUnico);
            }
            return resultado;

        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al eliminar CompraLimpieza: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error al eliminar CompraLimpieza con IdUnico={}.", idUnico, e);
            return false;
        }
    }

    /**
     * Cierra los recursos utilizados por el DAO para liberar memoria y evitar fugas de recursos.
     */
    public void cerrar() {
        try {
            compraLimpiezaDAO.cerrar();
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
