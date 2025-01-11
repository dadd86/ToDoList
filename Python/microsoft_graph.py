import os
import logging
import requests
from msal import ConfidentialClientApplication
from dotenv import load_dotenv
from tenacity import retry, stop_after_attempt, wait_fixed
from urllib.parse import quote

# Configuración de logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]  # Permite loguear en la consola
)

# Ruta al archivo de configuración .env
dotenv_path = "C:/Users/Admin/OneDrive/cursos/Programas/Java/ToDoList/ToDoList/Certificados/Credenciales.env"
load_dotenv(dotenv_path=dotenv_path)

class MicrosoftGraphAuth:
    """
    Clase para manejar la autenticación con Microsoft Graph API.
    """

    def __init__(self):
        """
        Inicializa los parámetros de autenticación, validando la existencia de las variables de entorno necesarias.
        
        Variables:
            client_id (str): ID del cliente (aplicación).
            client_secret (str): Secreto del cliente.
            tenant_id (str): ID del inquilino en Azure.
            authority (str): URL de autoridad para la autenticación.
            scope (list): Alcance para el acceso a la API de Microsoft Graph.
        """
        self.client_id = os.getenv("CLIENT_ID")
        self.client_secret = os.getenv("CLIENT_SECRET")
        self.tenant_id = os.getenv("TENANT_ID")
        self.authority = f"https://login.microsoftonline.com/{self.tenant_id}"
        self.scope = ["https://graph.microsoft.com/.default"]

        # Validar que todas las variables de entorno estén definidas
        if not all([self.client_id, self.client_secret, self.tenant_id]):
            logging.critical("Faltan variables de entorno: CLIENT_ID, CLIENT_SECRET o TENANT_ID.")
            raise EnvironmentError("Faltan variables de entorno necesarias. Verifique el archivo .env.")
        
        logging.info("Variables de entorno cargadas correctamente.")

    def get_access_token(self) -> str:
        """
        Obtiene un token de acceso utilizando MSAL.

        Retorna:
            str: Token de acceso válido.

        Lanza:
            Exception: Si no se puede obtener el token.
        """
        try:
            app = ConfidentialClientApplication(
                self.client_id, authority=self.authority, client_credential=self.client_secret
            )
            result = app.acquire_token_for_client(scopes=self.scope)

            if "access_token" in result:
                logging.info("Token de acceso obtenido correctamente.")
                return result["access_token"]
            else:
                raise Exception(f"Error al obtener el token: {result}")
        except Exception as e:
            logging.error(f"Error al obtener el token: {e}")
            raise


class OneDriveFileManager:
    BASE_PATH = "cursos/Programas/Java/ToDoList/ToDoList/Fotos/"  # Ruta relativa en OneDrive

    def __init__(self, access_token: str):
        self.access_token = access_token
        self.headers = {"Authorization": f"Bearer {self.access_token}"}

    def file_exists(self, file_name: str) -> bool:
        """
        Verifica si un archivo existe en la carpeta predeterminada de OneDrive.

        Parámetros:
            file_name (str): Nombre del archivo.

        Retorna:
            bool: True si el archivo existe, False en caso contrario.
        """
        encoded_path = quote(f"{self.BASE_PATH}/{file_name}")  # Codificar caracteres especiales
        url = f"https://graph.microsoft.com/v1.0/me/drive/root:/{encoded_path}"

        try:
            response = requests.get(url, headers=self.headers)
            if response.status_code == 200:
                logging.info(f"El archivo '{file_name}' existe en OneDrive.")
                return True
            elif response.status_code == 404:
                logging.warning(f"El archivo '{file_name}' no existe en OneDrive.")
                return False
            else:
                response.raise_for_status()
        except requests.exceptions.RequestException as e:
            logging.error(f"Error al verificar la existencia del archivo '{file_name}': {e}")
            raise

    @retry(stop=stop_after_attempt(3), wait=wait_fixed(2))
    def open_file(self, file_name: str) -> str:
        """
        Abre y lee un archivo en OneDrive.

        Parámetros:
            file_name (str): Nombre del archivo.

        Retorna:
            str: Contenido del archivo.

        Lanza:
            FileNotFoundError: Si el archivo no existe.
            requests.exceptions.RequestException: Si ocurre un error al leer el archivo.
        """
        # Validar si el archivo existe
        if not self.file_exists(file_name):
            raise FileNotFoundError(f"El archivo '{file_name}' no existe en la carpeta predeterminada de OneDrive.")

        # Leer el archivo
        encoded_path = quote(f"{self.BASE_PATH}/{file_name}")
        url = f"https://graph.microsoft.com/v1.0/me/drive/root:/{encoded_path}:/content"

        try:
            response = requests.get(url, headers=self.headers)
            response.raise_for_status()
            logging.info(f"Archivo '{file_name}' leído exitosamente desde OneDrive.")
            return response.text
        except requests.exceptions.RequestException as e:
            logging.error(f"Error al leer el archivo '{file_name}' desde OneDrive: {e}")
            raise
