package modelo;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;



@Entity
@Table(name="CompraVarios")//Define la tabla correspondiente en la base de datos
public class ComprarVarios {
    //Logger para registrar eventos importantes
    private static final Logger logger =LoggerFactory.getLogger(ComprarVarios.class);

    //Atributos mapeados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdUnico",nullable = false)
    private int IdUnico;

    @Column(name="NombreProducto",nullable = false, length = 255)
    private String NombreProducto;

    @Column(name="Descripcion",nullable = false, length = 455)
    private String Descripcion;

    @Column(name = "Foto", nullable = false)
    private boolean Foto;

    @Column(name="NumeroUnicoFoto",nullable = false)
    private int NumeroUnicoFoto;

    @Column(name="Cantidad",nullable = false)
    private int Cantidad;

    @Column(name = "Realizado",nullable = false)
    private boolean Realizado;




}
