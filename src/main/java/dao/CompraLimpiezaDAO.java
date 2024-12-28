package dao;

import modelo.CompraLimpieza;

import java.util.List;

/**
 * Interfaz que define las operaciones CRUD para la entidad CompraLimpieza utilizando Hibernate.
 *
 * **Responsabilidades:**
 * - Definir los métodos necesarios para agregar, actualizar, eliminar y obtener registros de `CompraLimpieza`.
 *
 * **Requisitos:**
 * - Implementación de los métodos por la clase `CompraLimpiezaDAOImpl`.
 *
 * @version 1.0
 * @since 2024
 */
public interface CompraLimpiezaDAO {

    /**
     * Agrega un nuevo registro de CompraLimpieza a la base de datos.
     *
     * @param compra Objeto CompraLimpieza a agregar.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    boolean agregarCompra(CompraLimpieza compra);

    /**
     * Obtiene todos los registros de CompraLimpieza desde la base de datos.
     *
     * @return Lista de objetos CompraLimpieza, o `null` si ocurre un error.
     */
    List<CompraLimpieza> obtenerTodasLasCompras();

    /**
     * Actualiza un registro existente de CompraLimpieza en la base de datos.
     *
     * @param compra Objeto CompraLimpieza con los datos actualizados.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    boolean actualizarCompra(CompraLimpieza compra);

    /**
     * Elimina un registro de CompraLimpieza en la base de datos.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    boolean eliminarCompra(int idUnico);

    /**
     * Obtiene el último número único de foto registrado en la base de datos.
     *
     * @return El último número único de foto, o `0` si no existen registros.
     */
    int obtenerUltimoNumeroFoto();

    /**
     * Cierra los recursos utilizados por el DAO para evitar fugas de memoria.
     */
    void cerrar();
}
