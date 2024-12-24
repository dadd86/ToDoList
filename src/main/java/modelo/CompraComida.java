package modelo;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modelo que representa la entidad "CompraComida" para ser gestionada con Hibernate.
 *
 * **Propósito:**
 * Gestionar los datos de compras relacionadas con comida, validando la consistencia y permitiendo
 * el acceso a través de getters y setters.
 *
 * **Características:**
 * - Almacena datos sobre nombre, descripción, cantidad, estado de compra, y si está asociada a una foto.
 * - Proporciona validaciones estrictas para asegurar la integridad de los datos.
 * - Incluye un logger para registrar eventos importantes.
 *
 * **Restricciones:**
 * - El nombre del producto no puede ser nulo ni vacío.
 * - La descripción no puede ser nula ni vacía.
 * - La cantidad debe ser mayor a 0.
 * - Si `foto` es `true`, el número único de la foto (`numeroUnicoFoto`) debe ser un valor positivo.
 *
 * **Relación con Hibernate:**
 * Este modelo será gestionado por Hibernate en la capa DAO para realizar operaciones CRUD.
 *
 * @author Diego
 * @version 1.3
 * @since 2024
 */
@Entity
@Table(name = "CompraComida")
public class CompraComida {

    private static final Logger logger = LoggerFactory.getLogger(CompraComida.class);

    /**
     * Identificador único del registro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUnico", nullable = false, updatable = false)
    private int idUnico;

    /**
     * Nombre del producto comprado.
     */
    @Column(name = "NombreProducto", nullable = false, length = 255)
    private String nombreProducto;

    /**
     * Descripción del producto.
     */
    @Column(name = "Descripcion", nullable = false, length = 455)
    private String descripcion;

    /**
     * Indica si la compra tiene una foto asociada.
     */
    @Column(name = "Foto", nullable = false)
    private boolean foto;

    /**
     * Número único asociado a la foto del producto.
     * Si `foto` es `true`, este campo debe tener un valor positivo.
     */
    @Column(name = "NumeroUnicoFoto")
    private Integer numeroUnicoFoto;

    /**
     * Cantidad de productos comprados.
     */
    @Column(name = "Cantidad", nullable = false)
    private int cantidad;

    /**
     * Indica si la compra ha sido realizada.
     */
    @Column(name = "Realizado", nullable = false)
    private boolean realizado;

    // ========================
    // Constructores
    // ========================

    /**
     * Constructor con parámetros para inicializar el modelo.
     *
     * **Validaciones:**
     * - Asegura que los datos ingresados cumplan con las restricciones del modelo.
     *
     * @param nombreProducto Nombre del producto comprado.
     * @param descripcion Descripción detallada del producto.
     * @param foto Indica si el producto tiene una foto asociada.
     * @param numeroUnicoFoto Número único de la foto (se asignará automáticamente si es nulo).
     * @param cantidad Cantidad del producto comprado (debe ser mayor a 0).
     * @param realizado Indica si la compra ya ha sido realizada.
     * @throws IllegalArgumentException Si alguno de los valores no cumple con las restricciones.
     */
    public CompraComida(String nombreProducto, String descripcion, boolean foto, Integer numeroUnicoFoto, int cantidad, boolean realizado) {
        validarNombreProducto(nombreProducto);
        validarDescripcion(descripcion);
        validarCantidad(cantidad);

        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.foto = foto;

        if (foto) {
            if (numeroUnicoFoto == null || numeroUnicoFoto <= 0) {
                this.numeroUnicoFoto = 1; // Este valor puede ser ajustado en el DAO
                logger.warn("Número único de foto no proporcionado. Se asigna temporalmente el valor: 1");
            } else {
                this.numeroUnicoFoto = numeroUnicoFoto;
            }
        } else {
            this.numeroUnicoFoto = null;
        }

        this.cantidad = cantidad;
        this.realizado = realizado;

        logger.info("CompraComida creada exitosamente: {}", this);
    }

    /**
     * Constructor vacío requerido para el uso con Hibernate.
     */
    public CompraComida() {
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
        validarDescripcion(descripcion);
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

    @Override
    public String toString() {
        return "CompraComida{" +
                "idUnico=" + idUnico +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", foto=" + foto +
                ", numeroUnicoFoto=" + numeroUnicoFoto +
                ", cantidad=" + cantidad +
                ", realizado=" + realizado +
                '}';
    }

    // ========================
    // Métodos privados de validación
    // ========================

    /**
     * Valida el nombre del producto.
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
     * @param cantidad Cantidad a validar.
     * @throws IllegalArgumentException Si la cantidad no es mayor a 0.
     */
    private void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            logger.error("La cantidad debe ser mayor que 0.");
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
        }
    }
}
