package modelo;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que representa la entidad "CompraLimpieza" en la base de datos.
 *
 * Esta clase almacena la información de los productos relacionados con compras de limpieza.
 * Incluye validaciones, generación automática de números únicos para fotos, y un logger para registrar eventos importantes.
 *
 * **Características:**
 * - Almacena datos como nombre, descripción, cantidad, foto asociada, y estado de la compra.
 * - Si "foto" es true, genera automáticamente un número único para "NumeroUnicoFoto".
 *
 * @author [Tu Nombre]
 * @version 1.1
 * @since 2024
 */
@Entity
@Table(name = "CompraLimpieza")
public class CompraLimpieza {

    private static final Logger logger = LoggerFactory.getLogger(CompraLimpieza.class);

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
     * Constructor con parámetros para inicializar los atributos de la clase.
     *
     * @param nombreProducto Nombre del producto comprado.
     * @param descripcion Descripción del producto.
     * @param foto Indica si el producto tiene una foto asociada.
     * @param numeroUnicoFoto Número único asociado a la foto del producto.
     * @param cantidad Cantidad de productos comprados.
     * @param realizado Indica si la compra ha sido realizada.
     * @throws Exception Si ocurre un error al generar el número único para fotos.
     */
    public CompraLimpieza(String nombreProducto, String descripcion, boolean foto, Integer numeroUnicoFoto, int cantidad, boolean realizado) throws Exception {
        // Validaciones
        if (nombreProducto == null || nombreProducto.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser vacío o nulo.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción del producto no puede ser vacía o nula.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        // Generar automáticamente un número único para fotos si es necesario
        if (foto) {
            if (numeroUnicoFoto == null || numeroUnicoFoto <= 0) {
                numeroUnicoFoto = getLastNumeroUnicoFoto() + 1;
            }
        } else {
            numeroUnicoFoto = null; // Si no hay foto, no se necesita un número único
        }

        // Asignar valores
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.foto = foto;
        this.numeroUnicoFoto = numeroUnicoFoto;
        this.cantidad = cantidad;
        this.realizado = realizado;

        logger.info("CompraLimpieza creada exitosamente: {}", this);
    }

    /**
     * Constructor vacío requerido por JPA.
     */
    public CompraLimpieza() {
    }

    // Métodos auxiliares

    /**
     * Obtiene el último número único generado en la tabla "CompraLimpieza".
     *
     * @return El último valor de NumeroUnicoFoto o 0 si no existen registros.
     * @throws Exception Si ocurre un error al interactuar con la base de datos.
     */
    private int getLastNumeroUnicoFoto() throws Exception {
        int lastNumero = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(NumeroUnicoFoto) AS LastNumero FROM CompraLimpieza")) {

            if (resultSet.next()) {
                lastNumero = resultSet.getInt("LastNumero");
            }
        }

        return lastNumero;
    }

    // Getters y Setters

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
        if (nombreProducto == null || nombreProducto.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser vacío o nulo.");
        }
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

    public void setFoto(boolean foto) throws Exception {
        this.foto = foto;

        if (foto) {
            // Generar automáticamente un número único si no está asignado
            if (this.numeroUnicoFoto == null || this.numeroUnicoFoto <= 0) {
                this.numeroUnicoFoto = getLastNumeroUnicoFoto() + 1;
            }
        } else {
            // Limpiar el número único si no hay foto
            this.numeroUnicoFoto = null;
        }
    }

    public Integer getNumeroUnicoFoto() {
        return numeroUnicoFoto;
    }

    public void setNumeroUnicoFoto(Integer numeroUnicoFoto) {
        this.numeroUnicoFoto = numeroUnicoFoto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
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
        return "CompraLimpieza{" +
                "idUnico=" + idUnico +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", foto=" + foto +
                ", numeroUnicoFoto=" + numeroUnicoFoto +
                ", cantidad=" + cantidad +
                ", realizado=" + realizado +
                '}';
    }
}
