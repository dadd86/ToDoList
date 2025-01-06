package vista.gestionAgregarElementos;

import controlador.ControladorTablas;
import controlador.ControladorCompraComida;
import controlador.ControladorCompraLimpieza;
import controlador.ControladorCompraVarios;
import vista.gestionMenuPrincipal.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Controlador para gestionar el formulario de agregar elementos.
 * Permite seleccionar una tabla de la base de datos desde un ComboBox y agregar productos a distintas tablas (CompraComida, CompraLimpieza, CompraVarios).
 *
 * **Responsabilidades:**
 * - Llenar el ComboBox con los nombres de las tablas disponibles en la base de datos.
 * - Cambiar el comportamiento del formulario según la tabla seleccionada en el ComboBox.
 * - Validar y agregar los datos del formulario al sistema.
 * - Registrar los eventos con un logger para depuración.
 *
 * **Mejoras en Seguridad y Robustez:**
 * - Validación estricta de entrada para evitar datos erróneos.
 * - Uso de excepciones con mensajes claros para el manejo de errores.
 * - Uso de un Logger para registrar las operaciones y errores de manera detallada.
 *
 * @version 1.1
 * @since 2024
 */
public class AgregarElementos {

    private static final Logger logger = LoggerFactory.getLogger(AgregarElementos.class);

    // Referencias a los componentes de la interfaz FXML
    @FXML
    private ComboBox<String> comboBoxTablas;
    @FXML
    private TextField NombreProducto;
    @FXML
    private TextField Descripcion;
    @FXML
    private CheckBox Foto;
    @FXML
    private TextField Cantidad;
    @FXML
    private CheckBox Realizada;
    @FXML
    private TextField SuperMercado;
    @FXML
    private Button btnAgregarElemento;

    // Instancias de los controladores para cada tipo de tabla
    private ControladorTablas controladorTablas;
    private ControladorCompraComida controladorCompraComida;
    private ControladorCompraLimpieza controladorCompraLimpieza;
    private ControladorCompraVarios controladorCompraVarios;
    private SceneManager sceneManager;


    /**
     * Constructor que inicializa los controladores específicos y el DAO para interactuar con la base de datos.
     */
    public AgregarElementos() {
        controladorTablas = new ControladorTablas();
        controladorCompraComida = new ControladorCompraComida();
        controladorCompraLimpieza = new ControladorCompraLimpieza();
        controladorCompraVarios = new ControladorCompraVarios();
    }

    /**
     * Método llamado al cargar la vista FXML. Configura el ComboBox y la acción para la selección de tablas.
     * Llama al método cargarTablas para llenar el ComboBox con las tablas de la base de datos.
     */
    @FXML
    public void initialize() {
        cargarTablas();  // Cargar las tablas disponibles

        // Configurar el ComboBox para escuchar cambios de selección y llamar a cargarFormulario con la tabla seleccionada
        comboBoxTablas.setOnAction(event -> {
            String tablaSeleccionada = comboBoxTablas.getValue();
            cargarFormulario(tablaSeleccionada);
        });
    }

    /**
     * Carga las tablas obtenidas desde la base de datos al ComboBox.
     * Utiliza el controladorTablas para obtener la lista de tablas.
     */
    private void cargarTablas() {
        List<String> tablas = controladorTablas.getTablas();  // Obtener las tablas desde el controlador
        if (tablas != null && !tablas.isEmpty()) {
            comboBoxTablas.getItems().addAll(tablas);
        } else {
            comboBoxTablas.getItems().add("No hay tablas disponibles.");
        }
    }


    /**
     * Cambia el comportamiento del formulario según la tabla seleccionada en el ComboBox.
     * Llama al controlador adecuado para la tabla seleccionada.
     *
     * @param tablaSeleccionada El nombre de la tabla seleccionada.
     */
    private void cargarFormulario(String tablaSeleccionada) {
        switch (tablaSeleccionada) {
            case "CompraComida":
                btnAgregarElemento.setOnAction(event -> agregarCompraComida());
                break;
            case "CompraLimpieza":
                btnAgregarElemento.setOnAction(event -> agregarCompraLimpieza());
                break;
            case "CompraVarios":
                btnAgregarElemento.setOnAction(event -> agregarCompraVarios());
                break;
            default:
                logger.warn("Tabla no reconocida: {}", tablaSeleccionada);
        }
    }

    /**
     * Método para agregar un producto de tipo CompraComida a la base de datos.
     * Validación de los campos antes de intentar agregar el producto.
     */
    private void agregarCompraComida() {
        try {
            if (validarCampos()) {
                boolean fotoSeleccionada = isCheckBoxSelected(Foto);  // Verificar si Foto está seleccionada
                boolean realizadaSeleccionada = isCheckBoxSelected(Realizada);  // Verificar si Realizada está seleccionada

                controladorCompraComida.agregarCompra(NombreProducto.getText(), Descripcion.getText(), fotoSeleccionada, Integer.parseInt(Cantidad.getText()), realizadaSeleccionada, SuperMercado.getText());
                logger.info("Compra de comida agregada: {}", NombreProducto.getText());
            }
        } catch (Exception e) {
            logger.error("Error al agregar CompraComida: {}", e.getMessage(), e);
        }
    }

    /**
     * Método para agregar un producto de tipo CompraLimpieza a la base de datos.
     * Validación de los campos antes de intentar agregar el producto.
     */
    private void agregarCompraLimpieza() {
        try {
            if (validarCampos()) {
                boolean fotoSeleccionada = isCheckBoxSelected(Foto);  // Verificar si Foto está seleccionada
                boolean realizadaSeleccionada = isCheckBoxSelected(Realizada);  // Verificar si Realizada está seleccionada

                controladorCompraLimpieza.agregarCompra(NombreProducto.getText(), Descripcion.getText(), fotoSeleccionada, Integer.parseInt(Cantidad.getText()), realizadaSeleccionada, SuperMercado.getText());
                logger.info("Compra de limpieza agregada: {}", NombreProducto.getText());
            }
        } catch (Exception e) {
            logger.error("Error al agregar CompraLimpieza: {}", e.getMessage(), e);
        }
    }

    /**
     * Método para agregar un producto de tipo CompraVarios a la base de datos.
     * Validación de los campos antes de intentar agregar el producto.
     */
    private void agregarCompraVarios() {
        try {
            if (validarCampos()) {
                boolean fotoSeleccionada = isCheckBoxSelected(Foto);  // Verificar si Foto está seleccionada
                boolean realizadaSeleccionada = isCheckBoxSelected(Realizada);  // Verificar si Realizada está seleccionada

                controladorCompraVarios.agregarCompra(NombreProducto.getText(), Descripcion.getText(), fotoSeleccionada, Integer.parseInt(Cantidad.getText()), realizadaSeleccionada, SuperMercado.getText());
                logger.info("Compra de varios agregada: {}", NombreProducto.getText());
            }
        } catch (Exception e) {
            logger.error("Error al agregar CompraVarios: {}", e.getMessage(), e);
        }
    }

    /**
     * Método de validación de campos para asegurar que la entrada sea correcta.
     * Valida que los campos NombreProducto, Descripcion, y Cantidad no estén vacíos.
     *
     * @return `true` si todos los campos son válidos, `false` en caso contrario.
     */
    private boolean validarCampos() {
        if (NombreProducto.getText().isEmpty() || Cantidad.getText().isEmpty() || Descripcion.getText().isEmpty()) {
            logger.error("Los campos NombreProducto, Cantidad y Descripción no pueden estar vacíos.");
            return false;
        }
        try {
            int cantidad = Integer.parseInt(Cantidad.getText());
            if (cantidad <= 0) {
                logger.error("La cantidad debe ser mayor que cero.");
                return false;
            }
        } catch (NumberFormatException e) {
            logger.error("La cantidad debe ser un número entero.");
            return false;
        }
        return true;
    }

    /**
     * Método genérico para obtener el estado (seleccionado/no seleccionado) de cualquier CheckBox.
     *
     * @param checkBox El CheckBox cuya selección se va a comprobar.
     * @return `true` si el CheckBox está seleccionado, `false` si no lo está.
     */
    private boolean isCheckBoxSelected(CheckBox checkBox) {
        return checkBox.isSelected();  // Devuelve true si está marcado, false si no lo está
    }
    /**
     * Maneja la acción del botón "Volver al Menú Principal".
     */
    @FXML
    private void volverAlMenu() {
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
