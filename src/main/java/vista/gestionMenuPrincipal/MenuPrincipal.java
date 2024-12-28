package vista.gestionMenuPrincipal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import excepciones.SceneManagerException;

public class MenuPrincipal {

    private static final Logger logger = LoggerFactory.getLogger(MenuPrincipal.class);
    private SceneManager sceneManager;  // Escena Manager para manejar las transiciones entre vistas

    // Definir los botones que estarán en el FXML
    @FXML
    private Button btnNuevaTarea;
    @FXML
    private Button btnTareaPendiente;
    @FXML
    private Button btnTareaFinalizada;
    @FXML
    private Button btnAgregarElementos;

    /**
     * Método que se llama para establecer el SceneManager en el controlador.
     *
     * @param sceneManager El SceneManager que se utilizará para cambiar de vistas.
     */
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /**
     * Acción para el botón "Nueva Tarea".
     * Este método cambiará a la vista de creación de nueva tarea.
     */
    @FXML
    private void NuevaTarea() {
        try {
            // Cambiar la vista a la ventana de nueva tarea
            sceneManager.cambiarVista("/vista/gestionMenuPrincipal/NuevaTarea.fxml", "Nueva Tarea");
        } catch (SceneManagerException e) {
            logger.error("Error al cambiar la vista a Nueva Tarea.", e);
        }
    }

    /**
     * Acción para el botón "Tareas Pendientes".
     * Este método cambiará a la vista de tareas pendientes.
     */
    @FXML
    private void TareaPendiente() {
        try {
            // Cambiar la vista a tareas pendientes
            sceneManager.cambiarVista("/vista/gestionMenuPrincipal/TareasPendientes.fxml", "Tareas Pendientes");
        } catch (SceneManagerException e) {
            logger.error("Error al cambiar la vista a Tareas Pendientes.", e);
        }
    }

    /**
     * Acción para el botón "Tareas Finalizadas".
     * Este método cambiará a la vista de tareas finalizadas.
     */
    @FXML
    private void TareaFinalizadas() {
        try {
            // Cambiar la vista a tareas finalizadas
            sceneManager.cambiarVista("/vista/gestionMenuPrincipal/TareasFinalizadas.fxml", "Tareas Finalizadas");
        } catch (SceneManagerException e) {
            logger.error("Error al cambiar la vista a Tareas Finalizadas.", e);
        }
    }

    /**
     * Acción para el botón "Agregar Elementos".
     * Este método cambiará a la vista de agregar elementos a la tarea.
     */
    @FXML
    private void AgregarElementos() {
        try {
            // Cambiar la vista para agregar elementos a las tareas
            sceneManager.cambiarVista("/vista/gestionMenuPrincipal/AgregarElementos.fxml", "Agregar Elementos");
        } catch (SceneManagerException e) {
            logger.error("Error al cambiar la vista a Agregar Elementos.", e);
        }
    }
}
