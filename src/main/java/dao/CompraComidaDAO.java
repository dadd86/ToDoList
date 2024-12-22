package dao;

import modelo.CompraComida;

import java.util.List;

/**
 * Interfaz DAO para manejar las operaciones CRUD de la entidad CompraComida.
 *
 * Define las operaciones básicas que deben ser implementadas para interactuar con la base de datos.
 *
 * **Responsabilidades:**
 * - Crear un nuevo registro de CompraComida.
 * - Leer registros existentes.
 * - Actualizar registros existentes.
 * - Eliminar registros existentes.
 *
 * @author [Tu Nombre]
 * @version 1.0
 * @since 2024
 */
public interface CompraComidaDAO {

    /**
     * Agrega un nuevo registro a la tabla CompraComida.
     *
     * @param compraComida Objeto CompraComida con los datos a insertar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean agregarCompra(CompraComida compraComida);

    /**
     * Obtiene todos los registros de la tabla CompraComida.
     *
     * @return Lista de objetos CompraComida.
     */
    List<CompraComida> obtenerTodasLasCompras();

    /**
     * Actualiza un registro existente en la tabla CompraComida.
     *
     * @param compraComida Objeto CompraComida con los datos actualizados.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean actualizarCompra(CompraComida compraComida);

    /**
     * Elimina un registro de la tabla CompraComida.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean eliminarCompra(int idUnico);
    /**
     * Obtiene el último valor de la columna NumeroUnicoFoto en la tabla CompraComida.
     *
     * @return El último número único utilizado para una foto o 0 si no existen registros.
     */
    int obtenerUltimoNumeroFoto();


    /**
     * Cierra los recursos utilizados por el DAO.
     */
    void cerrar();
}
