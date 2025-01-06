package oneDrive;

import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.IAuthenticationResult;

import okhttp3.Request;

import java.io.*;
import java.util.Collections;

public class OneDriveConnection {


    /**
     * Obtiene el token de acceso y lo guarda en un archivo para su reutilización.
     * @return El token de acceso.
     * @throws Exception Si ocurre algún error durante la autenticación.
     */
    public String getAccessToken() throws Exception {
        ConfidentialClientApplication app = ConfidentialClientApplication.builder(
                        CLIENT_ID, ClientCredentialFactory.createFromSecret(CLIENT_SECRET))
                .authority(AUTHORITY)
                .build();

        IAuthenticationResult result = app.acquireToken(ClientCredentialParameters.builder(
                Collections.singleton(SCOPE)).build()).join();

        String accessToken = result.accessToken();

        try (FileWriter writer = new FileWriter(TOKEN_FILE_PATH)) {
            writer.write(accessToken);
            System.out.println("Access Token guardado en: " + TOKEN_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error al guardar el token en el archivo: " + e.getMessage());
            throw e;
        }

        return accessToken;
    }

    /**
     * Sube un archivo a OneDrive usando Microsoft Graph.
     * @param accessToken El token de acceso obtenido.
     * @param fileName El nombre del archivo que se va a subir.
     * @throws Exception Si ocurre algún error durante la subida.
     */
    public void uploadFileToOneDrive(String accessToken, String fileName) throws Exception {
        String filePath = FILES_FOLDER_PATH + fileName;

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("El archivo no existe: " + filePath);
        }

        // Crear instancia de GraphServiceClient
        GraphServiceClient<Request> graphClient = getGraphClient(accessToken);

        try (InputStream fileStream = new FileInputStream(file)) {
            DriveItem uploadedFile = graphClient
                    .me()
                    .drive()
                    .root()
                    .itemWithPath(file.getName())
                    .content()
                    .buildRequest()
                    .put(fileStream);

            System.out.println("Archivo subido exitosamente a OneDrive con ID: " + uploadedFile.id);
        } catch (Exception e) {
            System.err.println("Error al subir el archivo: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Crea e inicializa el cliente de Microsoft Graph.
     * @param accessToken El token de acceso obtenido.
     * @return Una instancia de GraphServiceClient lista para usarse.
     */
    private GraphServiceClient<Request> getGraphClient(String accessToken) {
        return GraphServiceClient.builder()
                .authenticationProvider(request -> {
                    // Agrega el encabezado Authorization a cada solicitud
                    request.addHeader("Authorization", "Bearer " + accessToken);
                })
                .buildClient();
    }

    public static void main(String[] args) {
        try {
            OneDriveConnection oneDriveConnection = new OneDriveConnection();

            // Obtener token de acceso
            String accessToken = oneDriveConnection.getAccessToken();

            // Subir un archivo a OneDrive
            oneDriveConnection.uploadFileToOneDrive(accessToken, "example.jpg");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
