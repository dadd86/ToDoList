import os
<<<<<<< HEAD
import re
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from google.auth.transport.requests import Request
from dotenv import load_dotenv

# ===========================================
# 🔹 Cargar variables de entorno desde `.env`
# ===========================================

dotenv_path = os.path.join(os.path.dirname(__file__), '..', 'certificados', 'Credenciales.env')
load_dotenv(dotenv_path=dotenv_path)

# ===========================================
# 🔹 Definir los alcances de la API de Google Drive
# ===========================================

SCOPES = [
    'https://www.googleapis.com/auth/drive',  # Acceso completo a Google Drive
    'https://www.googleapis.com/auth/drive.file',  # Acceso a archivos creados por la app
    'https://www.googleapis.com/auth/drive.metadata',  # Ver archivos y metadatos
    'https://www.googleapis.com/auth/drive.appdata'  # Gestionar archivos de la app
]

# ===========================================
# 🔹 Función para autenticar al usuario con Google Drive
# ===========================================

def authenticate():
    """
    Autentica al usuario con la API de Google Drive utilizando OAuth 2.0.

    Funcionalidades:
    ----------------
    - Carga un token guardado (`token.json`) para evitar repetir autenticación.
    - Si el token es inválido o expiró, se intenta refrescar automáticamente.
    - Si no hay credenciales válidas, inicia el flujo de autenticación en el navegador.

    Retorna:
    --------
    service : googleapiclient.discovery.Resource
        Objeto autenticado para interactuar con la API de Google Drive.

    Excepciones:
    ------------
    - FileNotFoundError: Si no se encuentra el archivo de credenciales.
    - google.auth.exceptions.RefreshError: Si el token guardado ha expirado y no puede ser renovado.
    - Exception: Para cualquier otro error inesperado.
    """
    # Obtener la ruta del archivo de credenciales desde el archivo `.env`
    creds_path = os.getenv("GOOGLE_CLIENT_ID")
    token_path = os.path.join(os.path.dirname(__file__), 'token.json')  # Token en la misma carpeta que el script

    # Validar que el archivo de credenciales existe
    if not creds_path or not os.path.exists(creds_path):
        raise FileNotFoundError(f"❌ El archivo de credenciales no se encontró en la ruta: {os.path.abspath(creds_path)}")

    creds = None

    try:
        # Intentar cargar credenciales guardadas en `token.json`
        if os.path.exists(token_path):
            creds = Credentials.from_authorized_user_file(token_path, SCOPES)

        # Si no hay credenciales válidas, iniciar el flujo de autenticación
        if not creds or not creds.valid:
            if creds and creds.expired and creds.refresh_token:
                try:
                    creds.refresh(Request())
                    print("🔄 Token actualizado correctamente.")
                except Exception as e:
                    print(f"⚠️ Error al refrescar el token: {e}. Iniciando nuevo flujo de autenticación.")
                    creds = None

            if not creds:  # Si las credenciales siguen siendo inválidas, solicitar nuevas
                flow = InstalledAppFlow.from_client_secrets_file(creds_path, SCOPES)
                creds = flow.run_local_server(port=0)

            # Guardar el nuevo token para futuras ejecuciones
            with open(token_path, 'w') as token_file:
                token_file.write(creds.to_json())

        # Construir el servicio de Google Drive
        service = build('drive', 'v3', credentials=creds)
        print("✅ Autenticación exitosa.")
        return service

    except FileNotFoundError as e:
        print(f"❌ Error: {e}")
        raise
    except Exception as e:
        print(f"❌ Error inesperado durante la autenticación: {e}")
        raise

# ===========================================
# 🔹 Función para asignar permisos a una carpeta
# ===========================================

def set_folder_permissions(service, folder_id, role, type, email_address=None):
    """
    Asigna permisos a una carpeta en Google Drive.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Objeto autenticado para interactuar con la API de Google Drive.
    folder_id : str
        ID de la carpeta a la que se asignarán los permisos.
    role : str
        Rol a asignar: 'reader' (lector) o 'writer' (escritor).
    type : str
        Tipo de entidad: 'user' (usuario), 'group' (grupo), 'domain' (dominio) o 'anyone' (cualquiera).
    email_address : str, opcional
        Dirección de correo electrónico del usuario o grupo al que se asignan los permisos.
        Obligatorio si `type` es 'user' o 'group'.

    Retorna:
    -------
    None

    Excepciones:
    ------------
    - googleapiclient.errors.HttpError: Si ocurre un error al interactuar con la API de Google Drive.
    - Exception: Para otros errores inesperados.
    """
    try:
        # Validar el ID de la carpeta
        if not folder_id or len(folder_id) < 25:
            raise ValueError("❌ El ID de la carpeta proporcionado no es válido.")

        # Crear cuerpo de la solicitud
        permission = {
            "role": role,  # 'reader' o 'writer'
            "type": type   # 'user', 'group', 'domain' o 'anyone'
        }
        if email_address:
            permission["emailAddress"] = email_address

        # Asignar permisos
        service.permissions().create(
            fileId=folder_id,
            body=permission,
            fields="id"
        ).execute()
        print(f"✅ Permiso '{role}' asignado correctamente a la carpeta ID: {folder_id}")

    except ValueError as ve:
        print(f"❌ Error de validación: {ve}")
        raise
    except Exception as e:
        print(f"❌ Error al asignar permisos: {e}")
        raise

# ===========================================
# 🔹 Función para extraer ID de un enlace de Google Drive
# ===========================================

def extract_drive_id(drive_url):
    """
    Extrae el ID de un archivo o carpeta desde un enlace de Google Drive.

    Parámetros:
    ----------
    drive_url : str
        URL compartida de Google Drive.

    Retorna:
    -------
    str
        El ID del archivo o carpeta.

    Excepciones:
    -----------
    - ValueError: Si la URL no contiene un ID válido.
    """
    match = re.search(r'[-\w]{25,}', drive_url)
    if match:
        return match.group(0)
    else:
        raise ValueError(f"❌ No se pudo extraer un ID válido de la URL: {drive_url}")
=======
import logging
from typing import List
from dotenv import load_dotenv

# Configuración del sistema de logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]  # Loguear en la consola
)

# Ruta al archivo .env
DOTENV_PATH = "C:/Users/Admin/OneDrive/cursos/Programas/Java/ToDoList/ToDoList/Certificados/Credenciales.env"

# Constantes: Variables de entorno requeridas
REQUIRED_ENV_VARS: List[str] = ["CLIENT_ID", "CLIENT_SECRET", "TENANT_ID"]

def load_environment(dotenv_path: str) -> None:
    """
    Carga las variables de entorno desde un archivo .env específico.

    Parámetros:
        dotenv_path (str): Ruta completa al archivo .env.

    Buenas prácticas implementadas:
    - Validación explícita de la existencia del archivo .env.
    - Registro detallado del estado del archivo .env.
    """
    if os.path.exists(dotenv_path):
        load_dotenv(dotenv_path=dotenv_path)
        logging.info(f"Archivo .env cargado desde: {dotenv_path}")
    else:
        logging.error(f"El archivo .env no se encontró en la ruta especificada: {dotenv_path}")
        raise FileNotFoundError(f"No se encontró el archivo .env en la ruta: {dotenv_path}")

def validate_env_variables(required_variables: List[str]) -> None:
    """
    Valida que todas las variables de entorno necesarias estén configuradas.

    Parámetros:
        required_variables (List[str]): Lista de nombres de variables de entorno requeridas.

    Excepciones:
        EnvironmentError: Se lanza si alguna de las variables requeridas no está configurada.

    Buenas prácticas implementadas:
    - Validación robusta y centralizada.
    - Uso de logs claros para informar el estado de las variables.
    - Evitar la exposición de datos sensibles como CLIENT_SECRET.
    """
    missing_vars = [var for var in required_variables if not os.getenv(var)]
    if missing_vars:
        error_message = (
            f"Variables de entorno faltantes: {', '.join(missing_vars)}. "
            "Verifique que las variables estén definidas en el entorno o en un archivo .env."
        )
        logging.critical(error_message)
        raise EnvironmentError(error_message)

    # Loggear las variables pero ocultando información sensible
    logging.info("Validación exitosa de variables de entorno. Valores configurados:")
    for var in required_variables:
        if var == "CLIENT_SECRET":
            logging.info(f"{var}: **** (oculto por seguridad)")
        else:
            logging.info(f"{var}: {os.getenv(var)}")

def log_exception(e: Exception, context: str = "") -> None:
    """
    Loguea excepciones de manera uniforme, proporcionando contexto adicional.

    Parámetros:
        e (Exception): Excepción capturada.
        context (str): Descripción opcional del contexto en el que ocurrió la excepción.

    Buenas prácticas implementadas:
    - Uso de contexto para ayudar a diagnosticar problemas.
    - Uso de exc_info=True para incluir el traceback completo en los logs.
    """
    logging.error(f"Excepción capturada en {context}: {e}", exc_info=True)

def initialize_application() -> None:
    """
    Función de inicialización de la aplicación.

    Realiza las validaciones y configuraciones iniciales necesarias antes de comenzar
    la ejecución principal del programa.

    Buenas prácticas implementadas:
    - Centralización de las configuraciones iniciales para mayor claridad.
    - Registro detallado del proceso de inicialización para facilitar el diagnóstico.
    """
    try:
        logging.info("Cargando archivo .env...")
        load_environment(DOTENV_PATH)

        logging.info("Iniciando validación de configuraciones...")
        validate_env_variables(REQUIRED_ENV_VARS)

        logging.info("Todas las configuraciones iniciales son correctas. Aplicación lista para ejecutarse.")
    except FileNotFoundError as fnf_error:
        log_exception(fnf_error, context="carga del archivo .env")
        raise
    except EnvironmentError as env_error:
        log_exception(env_error, context="validación de variables de entorno")
        raise
    except Exception as e:
        log_exception(e, context="proceso de inicialización general")
        raise
>>>>>>> b2f110ae6211a784e2900ac11ca5a7768e220796

