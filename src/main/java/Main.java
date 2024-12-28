import vista.gestionMenuPrincipal.*;
import Util.PruebaConexion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import excepciones.*;

import java.io.IOException;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public interface SceneManagerAware {
        void setSceneManager(SceneManager sceneManager);
    }


    /**
     * Método principal que arranca la aplicación.
     * <p>
     * Este método inicializa la interfaz de usuario, configura el `SceneManager`
     * y gestiona la conexión con Hibernate para verificar que el sistema esté
     * correctamente configurado antes de continuar con el arranque de la aplicación.
     * </p>
     *
     * @param primaryStage El escenario principal donde se cargará la interfaz.
     * @throws Exception Si ocurre algún error durante el proceso de inicialización.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Llamar a la clase de prueba para verificar la conexión a la base de datos
            PruebaConexion.main(new String[]{}); // Cambio aquí: pasar arreglo vacío
            double fixedWidth = 800.0;
            double fixedHeight =600.0;
            // Crear el SceneManager, pasando el primaryStage y la configuración de la ventana
            SceneManager sceneManager = new SceneManager(primaryStage, "/styles.css", fixedWidth, fixedHeight, false);
            // Cargar el archivo FXML para la vista principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/MenuPrincipal.fxml"));
            Parent root = loader.load();

            // Configurar el controlador para poder acceder al SceneManager
            Object controlador = loader.getController();
            if (controlador instanceof SceneManagerAware) {
                ((SceneManagerAware) controlador).setSceneManager(sceneManager);
            }


            // Crear la escena y aplicarle la hoja de estilo
            Scene scene = new Scene(root, fixedWidth, fixedHeight);
            // Establecer la escena en el primaryStage y mostrar la ventana principal
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menu Principal");
            primaryStage.setResizable(true);
            primaryStage.show();

        } catch (IOException e) {
            logger.error("Error al cargar la vista: {}", e.getMessage());
            showErrorDialog("Error de carga", "Hubo un problema al cargar la vista.");
        } catch (Exception e) {
            logger.error("Error en la ejecución de la aplicación: {}", e.getMessage());
            showErrorDialog("Error de inicio", "No se pudo iniciar la aplicación. Verifique la configuración.");
        }
    }


    /**
     * Muestra un cuadro de diálogo con un mensaje de error para el usuario.
     *
     * @param title   Título del cuadro de diálogo.
     * @param message Mensaje de error a mostrar al usuario.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Método principal para ejecutar la aplicación.
     * Este método es el punto de entrada de la aplicación JavaFX.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}


//            ESTA LINA ES PARA HACER LA PRUEBA DE CONEXION CON LA BASE DATOS
//            // Llamar a la clase de prueba para verificar la conexión a la base de datos
//            PruebaConexion.main(args);