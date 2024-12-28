package vista.gestionMenuPrincipal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import excepciones.SceneManagerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Clase que gestiona las transiciones entre vistas de la aplicación JavaFX y personaliza la ventana.
 *
 * **Objetivo:**
 * - Cambiar la vista de la aplicación cargando un archivo FXML.
 * - Aplicar un logo en una esquina de la ventana.
 * - Configurar la ventana con un tamaño fijo.
 * - Aplicar la hoja de estilo CSS centralizada.
 * - Gestionar errores con excepciones personalizadas.
 *
 * **Responsabilidades:**
 * - Cargar las vistas.
 * - Configurar el tamaño y estilo de las ventanas.
 * - Añadir imágenes de fondo, como un logo.
 * - Cambiar la vista con validación adecuada.
 */
public class SceneManager {

    private static final Logger logger = LoggerFactory.getLogger(SceneManager.class);

    private final Stage primaryStage; // Stage principal de la aplicación.
    private final String defaultStylesheet; // Hoja de estilo CSS predeterminada.
    private Object controlador; // Controlador asociado a la vista cargada.
    private final double fixedWidth; // Ancho fijo de las ventanas.
    private final double fixedHeight; // Alto fijo de las ventanas.
    private final boolean resizable; // Indica si las ventanas son redimensionables.

    /**
     * Constructor de SceneManager.
     *
     * Este constructor inicializa las propiedades del gestor de vistas, incluyendo las dimensiones
     * de la ventana, la hoja de estilo predeterminada y el comportamiento de redimensionado.
     *
     * @param primaryStage      Stage principal de la aplicación.
     * @param defaultStylesheet Ruta del archivo CSS predeterminado (puede ser null).
     * @param fixedWidth        Ancho fijo en píxeles para las ventanas.
     * @param fixedHeight       Alto fijo en píxeles para las ventanas.
     * @param resizable         Indica si la ventana es redimensionable.
     * @throws IllegalArgumentException Si los parámetros proporcionados son inválidos.
     */
    public SceneManager(Stage primaryStage, String defaultStylesheet, double fixedWidth, double fixedHeight, boolean resizable) {
        if (primaryStage == null) {
            throw new IllegalArgumentException("El Stage principal no puede ser null.");
        }
        if (fixedWidth <= 0 || fixedHeight <= 0) {
            throw new IllegalArgumentException("Las dimensiones deben ser mayores que 0.");
        }

        this.primaryStage = primaryStage;
        this.defaultStylesheet = defaultStylesheet;
        this.fixedWidth = fixedWidth;
        this.fixedHeight = fixedHeight;
        this.resizable = resizable;
    }

    /**
     * Cambia la vista actual cargando un archivo FXML con un título y aplicando la hoja de estilo predeterminada.
     *
     * @param fxmlPath Ruta del archivo FXML que se va a cargar.
     * @param title Título de la ventana.
     * @throws SceneManagerException Si ocurre un error durante el cambio de vista.
     */
    public void cambiarVista(String fxmlPath, String title) throws SceneManagerException {
        cambiarVista(fxmlPath, title, defaultStylesheet);
    }

    /**
     * Cambia la vista actual cargando un archivo FXML, aplicando un archivo CSS y configurando un controlador.
     *
     * @param fxmlPath Ruta del archivo FXML.
     * @param title Título de la ventana.
     * @param stylesheet Ruta del archivo CSS para la vista (puede ser null para omitir).
     * @throws SceneManagerException Si ocurre un error durante el cambio de vista.
     */
    public void cambiarVista(String fxmlPath, String title, String stylesheet) throws SceneManagerException {
        validarParametros(fxmlPath, title);

        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            this.controlador = loader.getController();

            // Configurar el controlador si implementa setSceneManager
            try {
                var setSceneManagerMethod = controlador.getClass().getMethod("setSceneManager", SceneManager.class);
                setSceneManagerMethod.invoke(controlador, this);
            } catch (NoSuchMethodException e) {
                logger.warn("El método setSceneManager no existe en el controlador.");
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("Error al invocar el método setSceneManager.", e);
            }

            // Agregar el logo a la esquina
            ImageView logoImageView = cargarLogo();

            // Usar AnchorPane para posicionar el logo en la esquina
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().add(root); // Añadir contenido FXML
            anchorPane.getChildren().add(logoImageView); // Añadir el logo

            // Posicionar el logo en la esquina superior derecha
            AnchorPane.setTopAnchor(logoImageView, 10.0);
            AnchorPane.setRightAnchor(logoImageView, 10.0);

            // Crear la escena con el contenido y el logo
            Scene scene = new Scene(anchorPane, fixedWidth, fixedHeight);

            // Aplicar la hoja de estilo centralizada (styles.css) si se proporciona
            if (stylesheet != null && !stylesheet.trim().isEmpty()) {
                scene.getStylesheets().add(getClass().getResource(stylesheet).toExternalForm());
            }

            // Aquí aplicamos la hoja de estilo CSS centralizada para toda la aplicación
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.setResizable(resizable);
            primaryStage.show();

        } catch (IOException e) {
            throw new SceneManagerException("Error al cargar la vista desde: " + fxmlPath, e);
        }
    }

    /**
     * Carga la imagen del logo.
     *
     * @return Un objeto ImageView con la imagen del logo.
     */
    private ImageView cargarLogo() {
        ImageView logoImageView = new ImageView(new Image(getClass().getResource("/Imagenes/logo.jpg").toExternalForm()));
        logoImageView.setFitWidth(100);  // Ajustar tamaño del logo
        logoImageView.setFitHeight(100); // Ajustar tamaño del logo
        return logoImageView;
    }

    /**
     * Valida que los parámetros proporcionados no sean nulos o vacíos.
     *
     * @param fxmlPath Ruta del archivo FXML.
     * @param title Título de la ventana.
     * @throws IllegalArgumentException Si los parámetros son inválidos.
     */
    private void validarParametros(String fxmlPath, String title) {
        if (fxmlPath == null || fxmlPath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo FXML no puede ser nula o vacía.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la ventana no puede ser nulo o vacío.");
        }
    }

    /**
     * Devuelve el controlador asociado a la vista cargada.
     *
     * @return El objeto controlador de la vista cargada.
     * @throws IllegalStateException Si no se ha cargado ninguna vista.
     */
    public Object getControlador() {
        if (controlador == null) {
            throw new IllegalStateException("No se ha cargado ninguna vista.");
        }
        return controlador;
    }
}
