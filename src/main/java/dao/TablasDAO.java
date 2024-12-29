package dao;

import java.util.List;

/**
 * Interfaz DAO para obtener los nombres de las tablas de la base de datos.
 */
public interface TablasDAO {
    /**
     * Método para obtener los nombres de todas las tablas en la base de datos.
     *
     * @return Lista de nombres de las tablas.
     */
    List<String> getTablas();
}
