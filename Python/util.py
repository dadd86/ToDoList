import os
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

