package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import dao.TablasDAOImpl;
import java.util.List;

/**
 * Controlador para manejar la interfaz de usuario relacionada con las tablas.
 * Este controlador obtiene la lista de tablas desde el DAO y la establece en el ComboBox.
 */
public class ControladorTablas {

    @FXML
    private ComboBox<String> comboBoxTablas; // El ComboBox en el FXML

    private TablasDAOImpl tablasDAOImpl;  // DAO para obtener las tablas

    private List<String> tablas; // Variable para almacenar la lista de tablas

    /**
     * Inicializa el controlador. Este método se ejecuta cuando se carga el FXML.
     */
    @FXML
    public void initialize() {
        tablasDAOImpl = new TablasDAOImpl(); // Inicializamos el DAO

        // Obtener la lista de tablas desde el DAO
        tablas = tablasDAOImpl.getTablas();

        // Llenar el ComboBox con los nombres de las tablas
        if (tablas != null && !tablas.isEmpty()) {
            comboBoxTablas.getItems().addAll(tablas);
        } else {
            comboBoxTablas.getItems().add("No se encontraron tablas");
        }
    }

    /**
     * Método público para obtener las tablas.
     *
     * @return La lista de nombres de las tablas.
     */
    public List<String> getTablas() {
        return tablas;
    }
}
