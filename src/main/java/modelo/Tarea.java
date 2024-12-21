package modelo;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que representa la entidad Tarea en la base de datos.
 *
 * Esta entidad mapea la tabla "Tarea" y establece relaciones con las tablas
 * CompraComida, CompraLimpieza y CompraVarios.
 * Se utiliza para registrar y gestionar las tareas asociadas a diferentes compras.
 *
 * <ul>
 *   <li>Cada Tarea puede asociarse a una CompraComida, CompraLimpieza o CompraVarios.</li>
 *   <li>Incluye claves foráneas y asegura integridad referencial con las restricciones definidas.</li>
 * </ul>
 *
 * @author [Tu Nombre]
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "Tarea")
public class Tarea {

    // Logger para registrar eventos relacionados con esta clase
    private static final Logger logger = LoggerFactory.getLogger(Tarea.class);

    /**
     * Identificador único de la tarea. Se genera automáticamente en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, updatable = false)
    private Integer id;

    /**
     * Relación Many-to-One con la tabla CompraComida.
     * Puede ser null si la tarea no está asociada a una compra de comida.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "IDCompraComida",
            referencedColumnName = "IdUnico",
            foreignKey = @ForeignKey(name = "fk_tarea_compra_comida"),
            nullable = true
    )
    private CompraComida compraComida;

    /**
     * Relación Many-to-One con la tabla CompraLimpieza.
     * Puede ser null si la tarea no está asociada a una compra de limpieza.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "IDCompraLimpieza",
            referencedColumnName = "IdUnico",
            foreignKey = @ForeignKey(name = "fk_tarea_compra_limpieza"),
            nullable = true
    )
    private CompraLimpieza compraLimpieza;

    /**
     * Relación Many-to-One con la tabla CompraVarios.
     * Puede ser null si la tarea no está asociada a una compra de varios.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "IDCompraVarios",
            referencedColumnName = "IdUnico",
            foreignKey = @ForeignKey(name = "fk_tarea_compra_varios"),
            nullable = true
    )
    private ComprarVarios compraVarios;

    /**
     * Obtiene el identificador único de la tarea.
     *
     * @return el identificador único.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador único de la tarea.
     *
     * @param id el identificador único.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la compra de comida asociada a esta tarea.
     *
     * @return la entidad CompraComida asociada.
     */
    public CompraComida getCompraComida() {
        return compraComida;
    }

    /**
     * Establece la compra de comida asociada a esta tarea.
     *
     * @param compraComida la entidad CompraComida a asociar.
     */
    public void setCompraComida(CompraComida compraComida) {
        this.compraComida = compraComida;
    }

    /**
     * Obtiene la compra de limpieza asociada a esta tarea.
     *
     * @return la entidad CompraLimpieza asociada.
     */
    public CompraLimpieza getCompraLimpieza() {
        return compraLimpieza;
    }

    /**
     * Establece la compra de limpieza asociada a esta tarea.
     *
     * @param compraLimpieza la entidad CompraLimpieza a asociar.
     */
    public void setCompraLimpieza(CompraLimpieza compraLimpieza) {
        this.compraLimpieza = compraLimpieza;
    }

    /**
     * Obtiene la compra de varios asociada a esta tarea.
     *
     * @return la entidad CompraVarios asociada.
     */
    public ComprarVarios getCompraVarios() {
        return compraVarios;
    }

    /**
     * Establece la compra de varios asociada a esta tarea.
     *
     * @param compraVarios la entidad CompraVarios a asociar.
     */
    public void setCompraVarios(ComprarVarios compraVarios) {
        this.compraVarios = compraVarios;
    }

    /**
     * Método de conveniencia para registrar eventos relacionados con esta tarea.
     *
     * @param mensaje el mensaje a registrar en el log.
     */
    public void registrarEvento(String mensaje) {
        logger.info("Evento en Tarea {}: {}", id, mensaje);
    }
}
