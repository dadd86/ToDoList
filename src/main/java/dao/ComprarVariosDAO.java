package dao;

import modelo.ComprarVarios;
import java.util.List;

/**
 * Interfaz para la implementación de operaciones CRUD de la entidad ComprarVarios.
 *
 * Esta interfaz define las operaciones que deben ser implementadas para manipular registros de
 * la tabla CompraVarios en la base de datos.
 *
 * @author Diego Diaz
 * @version 1.0
 * @since 2024
 */
public interface ComprarVariosDAO {

    /**
     * Agrega un nuevo registro de CompraVarios a la base de datos.
     *
     * @param compra Objeto CompraVarios a agregar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean agregarCompra(ComprarVarios compra);

    /**
     * Obtiene todos los registros de CompraVarios desde la base de datos.
     *
     * @return Lista de objetos CompraVarios, o null si ocurre un error.
     */
    List<ComprarVarios> obtenerTodasLasCompras();

    /**
     * Actualiza un registro existente de CompraVarios en la base de datos.
     *
     * @param compra Objeto CompraVarios con los datos actualizados.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean actualizarCompra(ComprarVarios compra);

    /**
     * Elimina un registro de CompraVarios de la base de datos.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean eliminarCompra(int idUnico);

    /**
     * Obtiene el último número único de foto registrado en la base de datos.
     *
     * @return El valor máximo de numeroUnicoFoto, o 0 si no hay registros.
     */
    int obtenerUltimoNumeroFoto();

    /**
     * Cierra los recursos utilizados por el DAO para liberar memoria y evitar fugas de recursos.
     */
    void cerrar();
}
