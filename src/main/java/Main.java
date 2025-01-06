import oneDrive.OneDriveConnection;
import Util.PruebaConexion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Clase principal para ejecutar la aplicación JavaFX y gestionar la integración con OneDrive.
 */
public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Método principal que arranca la aplicación.
     *
     * @param primaryStage El escenario principal donde se cargará la interfaz.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Verificar conexión a la base de datos en un hilo separado
            new Thread(() -> {
                try {
                    PruebaConexion.main(new String[]{}); // Verificar conexión
                } catch (Exception e) {
                    logger.error("Error al conectar a la base de datos: {}", e.getMessage());
                    showErrorDialog("Error de conexión", "No se pudo conectar a la base de datos.");
                }
            }).start();

            // Configurar la ventana principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/MenuPrincipal.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menu Principal");
            primaryStage.show();

            // Manejar la integración con OneDrive
            manejarIntegracionOneDrive();

        } catch (IOException e) {
            logger.error("Error al cargar la vista: {}", e.getMessage());
            showErrorDialog("Error de carga", "Hubo un problema al cargar la vista.");
        }
    }

    /**
     * Maneja la integración con Microsoft OneDrive.
     */
    private void manejarIntegracionOneDrive() {
        try {
            OneDriveConnection oneDriveConnection = new OneDriveConnection();
            String accessToken = oneDriveConnection.getAccessToken();
            oneDriveConnection.uploadFileToOneDrive(accessToken, "example.txt");
        } catch (Exception e) {
            logger.error("Error en la integración con OneDrive: {}", e.getMessage());
        }
    }

    /**
     * Muestra un cuadro de diálogo con un mensaje de error.
     *
     * @param title   Título del cuadro de diálogo.
     * @param message Mensaje de error.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
