package dao;

import modelo.CompraLimpieza;

import java.util.List;

/**
 * Interfaz DAO para manejar las operaciones CRUD de la entidad CompraLimpieza.
 *
 * Define las operaciones básicas que deben ser implementadas para interactuar con la base de datos.
 *
 * **Responsabilidades:**
 * - Crear un nuevo registro de CompraLimpieza.
 * - Leer registros existentes.
 * - Actualizar registros existentes.
 * - Eliminar registros existentes.
 *
 * @author [Tu Nombre]
 * @version 1.0
 * @since 2024
 */
public interface CompraLimpiezaDAO {

    /**
     * Agrega un nuevo registro a la tabla CompraLimpieza.
     *
     * @param compraLimpieza Objeto CompraLimpieza con los datos a insertar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean agregarCompra(CompraLimpieza compraLimpieza);

    /**
     * Obtiene todos los registros de la tabla CompraLimpieza.
     *
     * @return Lista de objetos CompraLimpieza.
     */
    List<CompraLimpieza> obtenerTodasLasCompras();

    /**
     * Actualiza un registro existente en la tabla CompraLimpieza.
     *
     * @param compraLimpieza Objeto CompraLimpieza con los datos actualizados.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean actualizarCompra(CompraLimpieza compraLimpieza);

    /**
     * Elimina un registro de la tabla CompraLimpieza.
     *
     * @param idUnico Identificador único del registro a eliminar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean eliminarCompra(int idUnico);

    /**
     * Cierra el recurso SessionFactory.
     */
    void cerrar();
}
