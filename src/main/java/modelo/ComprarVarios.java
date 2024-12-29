package modelo;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que representa la entidad "CompraVarios" en la base de datos.
 * Esta clase almacena información sobre los productos relacionados con compras variadas.
 *
 * <p><strong>Características:</strong></p>
 * <ul>
 *     <li>Almacena datos como nombre, descripción, cantidad, foto asociada, y estado de la compra.</li>
 *     <li>Si "Foto" es true, genera automáticamente un número único para "NumeroUnicoFoto".</li>
 * </ul>
 *
 * <p><strong>Restricciones:</strong></p>
 * <ul>
 *     <li>El nombre del producto no puede ser nulo ni vacío.</li>
 *     <li>La descripción no puede ser nula ni vacía.</li>
 *     <li>La cantidad debe ser mayor que 0.</li>
 *     <li>Si `Foto` es `true`, `NumeroUnicoFoto` debe ser un número único positivo.</li>
 * </ul>
 *
 * <p><strong>Mejoras en Seguridad y Robustez:</strong></p>
 * <ul>
 *     <li>Se validan todos los parámetros de entrada para evitar datos inconsistentes en la base de datos.</li>
 *     <li>Generación automática de números únicos de foto, si es necesario.</li>
 *     <li>Se manejan excepciones de forma clara, proporcionando mensajes útiles para la depuración.</li>
 * </ul>
 *
 * @author Diego Diaz
 * @version 1.1
 * @since 2024
 */
@Entity
@Table(name = "CompraVarios")
public class ComprarVarios {

    // Logger para registrar eventos importantes en la clase
    private static final Logger logger = LoggerFactory.getLogger(ComprarVarios.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUnico", nullable = false, updatable = false)
    private int idUnico;

    @Column(name = "NombreProducto", nullable = false, length = 255)
    private String nombreProducto;

    @Column(name = "Descripcion", nullable = false, length = 455)
    private String descripcion;

    @Column(name = "Foto", nullable = false)
    private boolean foto;

    @Column(name = "NumeroUnicoFoto")
    private Integer numeroUnicoFoto;

    @Column(name = "Cantidad", nullable = false)
    private int cantidad;

    @Column(name = "Realizado", nullable = false)
    private boolean realizado;

    /**
     * Nombre del supermercado donde se realizó la compra.
     */
    @Column(name = "SuperMercado", nullable = false, length = 255)
    private String supermercado;

    /**
     * Constructor con parámetros para inicializar los atributos de la clase.
     * Realiza validaciones estrictas antes de asignar los valores a los campos.
     *
     * <p><strong>Excepciones:</strong></p>
     * <ul>
     *     <li>Lanza una excepción si alguno de los valores proporcionados no es válido.</li>
     * </ul>
     *
     * @param nombreProducto Nombre del producto comprado.
     * @param descripcion Descripción del producto.
     * @param foto Indica si el producto tiene una foto asociada.
     * @param numeroUnicoFoto Número único asociado a la foto del producto.
     * @param cantidad Cantidad de productos comprados.
     * @param realizado Indica si la compra ha sido realizada.
     * @param supermercado Nombre del supermercado donde se realizó la compra.
     * @throws Exception Si ocurre un error al generar el número único para fotos.
     */
    public ComprarVarios(String nombreProducto, String descripcion, boolean foto, Integer numeroUnicoFoto, int cantidad, boolean realizado, String supermercado) {
        validarNombreProducto(nombreProducto);
        validarDescripcion(descripcion);
        validarCantidad(cantidad);
        validarSupermercado(supermercado);

        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.foto = foto;

        // Si la foto está activa, asignamos un número único de foto, si no se proporciona uno.
        if (foto) {
            if (numeroUnicoFoto == null || numeroUnicoFoto <= 0) {
                this.numeroUnicoFoto = 1; // Este valor puede ser ajustado en el DAO
                logger.warn("Número único de foto no proporcionado. Se asigna temporalmente el valor: 1");
            } else {
                this.numeroUnicoFoto = numeroUnicoFoto;
            }
        } else {
            this.numeroUnicoFoto = null; // Si no tiene foto, el número único de foto es nulo
        }

        this.cantidad = cantidad;
        this.realizado = realizado;
        this.supermercado = supermercado;
        logger.info("CompraVarios creada exitosamente: {}", this);
    }

    /**
     * Constructor vacío requerido para el uso con Hibernate.
     */
    public ComprarVarios() {
    }

    // ========================
    // Getters y Setters
    // ========================

    public int getIdUnico() {
        return idUnico;
    }

    public void setIdUnico(int idUnico) {
        this.idUnico = idUnico;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        validarNombreProducto(nombreProducto);
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción del producto no puede ser vacía o nula.");
        }
        this.descripcion = descripcion;
    }

    public boolean isFoto() {
        return foto;
    }

    public void setFoto(boolean foto) {
        this.foto = foto;
        if (!foto) {
            this.numeroUnicoFoto = null;
        }
    }

    public Integer getNumeroUnicoFoto() {
        return numeroUnicoFoto;
    }

    public void setNumeroUnicoFoto(Integer numeroUnicoFoto) {
        if (numeroUnicoFoto != null && numeroUnicoFoto <= 0) {
            logger.error("Validación fallida: El número único de la foto debe ser mayor que 0.");
            throw new IllegalArgumentException("El número único de la foto debe ser mayor que 0.");
        }
        this.numeroUnicoFoto = numeroUnicoFoto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        validarCantidad(cantidad);
        this.cantidad = cantidad;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(String supermercado) {
        validarSupermercado(supermercado);
        this.supermercado = supermercado;
    }

    @Override
    public String toString() {
        return "ComprarVarios{" +
                "idUnico=" + idUnico +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", foto=" + foto +
                ", numeroUnicoFoto=" + numeroUnicoFoto +
                ", cantidad=" + cantidad +
                ", realizado=" + realizado +
                ", supermercado='" + supermercado + '\'' +
                '}';
    }

    // ========================
    // Métodos privados de validación
    // ========================

    /**
     * Valida el nombre del producto.
     *
     * <p><strong>Excepciones:</strong></p>
     * <ul>
     *     <li>Si el nombre es nulo o vacío, lanza una excepción.</li>
     * </ul>
     *
     * @param nombreProducto Nombre a validar.
     * @throws IllegalArgumentException Si el nombre es nulo o vacío.
     */
    private void validarNombreProducto(String nombreProducto) {
        if (nombreProducto == null || nombreProducto.isBlank()) {
            logger.error("El nombre del producto no puede ser vacío o nulo.");
            throw new IllegalArgumentException("El nombre del producto no puede ser vacío o nulo.");
        }
    }

    /**
     * Valida la descripción del producto.
     *
     * <p><strong>Excepciones:</strong></p>
     * <ul>
     *     <li>Si la descripción es nula o vacía, lanza una excepción.</li>
     * </ul>
     *
     * @param descripcion Descripción a validar.
     * @throws IllegalArgumentException Si la descripción es nula o vacía.
     */
    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            logger.error("La descripción no puede ser vacía o nula.");
            throw new IllegalArgumentException("La descripción no puede ser vacía o nula.");
        }
    }

    /**
     * Valida la cantidad del producto.
     *
     * <p><strong>Excepciones:</strong></p>
     * <ul>
     *     <li>Si la cantidad no es mayor a 0, lanza una excepción.</li>
     * </ul>
     *
     * @param cantidad Cantidad a validar.
     * @throws IllegalArgumentException Si la cantidad no es mayor a 0.
     */
    private void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            logger.error("La cantidad debe ser mayor que 0.");
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
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
