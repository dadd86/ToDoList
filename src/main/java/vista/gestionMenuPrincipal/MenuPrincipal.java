package vista.gestionMenuPrincipal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
     * Verifica si el SceneManager está configurado correctamente.
     *
     * @throws IllegalStateException Si el SceneManager no está configurado.
     */
    private void validarSceneManager() {
        if (sceneManager == null) {
            logger.error("El SceneManager no está configurado. No se puede cambiar la vista.");
            throw new IllegalStateException("El SceneManager no está configurado.");
        }
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
            validarSceneManager(); // Verificar que SceneManager está configurado correctamente
            // Cambiar la vista para agregar elementos a las tareas
            sceneManager.cambiarVista("/vistas/AgregarElementos.fxml", "Agregar Elementos");
            logger.info("Ventana de Agregar Elementos abierta correctamente.");
        } catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de Gestión de Excursiones.", e);
            mostrarAlertaError("Error", "Ocurrió un error inesperado.");
        }
    }
    /**
     * Maneja la acción del botón "Volver al Menú Principal".
     */
    @FXML
    private void VolverMenu() {
        logger.info("Intentando volver al menú principal.");
        if (sceneManager == null) {
            logger.error("El SceneManager no está configurado.");
            mostrarAlertaError("Error", "El gestor de escenas no está configurado. No se puede volver al menú principal.");
            return;
        }
        try {
            sceneManager.cambiarVista("/vistas/application.fxml", "Menú Principal", "/styles.css");
            logger.info("Vista del menú principal abierta correctamente.");
        } catch (Exception e) {
            logger.error("Error al volver al menú principal.", e);
            mostrarAlertaError("Error", "No se pudo volver al menú principal.");
        }
    }
    /**
     * Muestra una alerta de error genérica al usuario.
     *
     * @param header  Título de la alerta.
     * @param content Detalle del error.
     */
    private void mostrarAlertaError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
