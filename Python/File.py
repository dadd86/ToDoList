from googleapiclient.http import MediaFileUpload, MediaIoBaseDownload
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
import io
import os

# ===========================================
# 🔹 Función para subir un archivo a Google Drive
# ===========================================

def upload_file_to_photos(service, folder_id, file_name, file_path):
    """
    Sube un archivo a una carpeta específica en Google Drive.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Servicio autenticado de Google Drive.
    folder_id : str
        ID de la carpeta donde se guardará el archivo.
    file_name : str
        Nombre con el que el archivo aparecerá en Google Drive.
    file_path : str
        Ruta local del archivo a subir.

    Retorna:
    -------
    str
        ID del archivo subido.

    Excepciones:
    -----------
    FileNotFoundError:
        Si el archivo local no existe.
    HttpError:
        Si ocurre un error en la API de Google Drive.
    """
    if not os.path.exists(file_path):
        raise FileNotFoundError(f"❌ El archivo '{file_path}' no existe.")

    file_metadata = {
        'name': file_name,
        'parents': [folder_id]  
    }

    try:
        media = MediaFileUpload(file_path, resumable=True)
        file = service.files().create(body=file_metadata, media_body=media, fields='id').execute()
        print(f"✅ Archivo '{file_name}' subido con éxito. ID: {file.get('id')}")
        return file.get('id')
    except HttpError as error:
        print(f"❌ Error al subir el archivo: {error}")
        raise


# ===========================================
# 🔹 Función para listar archivos en una carpeta
# ===========================================

def list_files_in_folder(service, folder_id):
    """
    Lista todos los archivos en una carpeta específica en Google Drive.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Servicio autenticado de Google Drive.
    folder_id : str
        ID de la carpeta en Drive.

    Excepciones:
    -----------
    HttpError:
        Si ocurre un error en la API de Google Drive.
    """
    try:
        query = f"'{folder_id}' in parents"
        results = service.files().list(q=query, spaces='drive', fields='files(id, name)').execute()
        files = results.get('files', [])

        if not files:
            print(f"⚠️ No hay archivos en la carpeta con ID: {folder_id}.")
            return

        print("📂 Archivos en la carpeta:")
        for file in files:
            print(f" - {file['name']} (ID: {file['id']})")

    except HttpError as error:
        print(f"❌ Error al listar archivos: {error}")
        raise


# ===========================================
# 🔹 Función para abrir (descargar) un archivo
# ===========================================

def open_file_from_photos(service, folder_id, file_name, download_path):
    """
    Descarga un archivo desde una carpeta específica en Google Drive.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Servicio autenticado de Google Drive.
    folder_id : str
        ID de la carpeta donde se encuentra el archivo.
    file_name : str
        Nombre del archivo a descargar.
    download_path : str
        Ruta local donde se guardará el archivo descargado.

    Retorna:
    -------
    str
        Ruta local del archivo descargado.

    Excepciones:
    -----------
    FileNotFoundError:
        Si el archivo no se encuentra en Google Drive.
    HttpError:
        Si ocurre un error en la API de Google Drive.
    """
    query = f"'{folder_id}' in parents and name='{file_name}'"
    try:
        results = service.files().list(q=query, spaces='drive', fields='files(id, name)').execute()
        items = results.get('files', [])

        if not items:
            raise FileNotFoundError(f"❌ Archivo '{file_name}' no encontrado en la carpeta.")

        file_id = items[0]['id']
        print(f"📂 Archivo encontrado: {file_name} (ID: {file_id}). Descargando...")

        request = service.files().get_media(fileId=file_id)
        file_path = os.path.join(download_path, file_name)

        with io.FileIO(file_path, 'wb') as fh:
            downloader = MediaIoBaseDownload(fh, request)
            done = False
            while not done:
                status, done = downloader.next_chunk()
                print(f"📥 Descarga: {int(status.progress() * 100)}%")

        print(f"✅ Archivo descargado en: {file_path}")
        return file_path

    except FileNotFoundError as error:
        print(error)
        raise
    except HttpError as error:
        print(f"❌ Error al descargar el archivo: {error}")
        raise


# ===========================================
# 🔹 Función para listar permisos de un archivo
# ===========================================

def list_file_permissions(service, file_id):
    """
    Lista los permisos de un archivo en Google Drive.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Servicio autenticado de Google Drive.
    file_id : str
        ID del archivo en Google Drive.

    Excepciones:
    -----------
    HttpError:
        Si ocurre un error en la API de Google Drive.
    """
    try:
        permissions = service.permissions().list(fileId=file_id, fields="permissions").execute()
        print(f"🔑 Permisos del archivo ID: {file_id}")
        for perm in permissions.get("permissions", []):
            print(f" - ID: {perm.get('id')}, Rol: {perm.get('role')}, Tipo: {perm.get('type')}, Email: {perm.get('emailAddress', 'N/A')}")
    except HttpError as error:
        print(f"❌ Error al listar permisos: {error}")
        raise


# ===========================================
# 🔹 Función para descargar un archivo con su ID
# ===========================================

def download_file(service, file_id):
    """
    Descarga un archivo desde Google Drive usando su ID.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Servicio autenticado de Google Drive.
    file_id : str
        ID del archivo en Google Drive.

    Excepciones:
    -----------
    HttpError:
        Si ocurre un error en la API de Google Drive.
    """
    try:
        request = service.files().get_media(fileId=file_id)
        file = io.BytesIO()
        downloader = MediaIoBaseDownload(file, request)
        done = False

        while not done:
            status, done = downloader.next_chunk()
            print(f"📥 Descarga: {int(status.progress() * 100)}%")

        print("✅ Archivo descargado exitosamente.")
        return file.getvalue()

    except HttpError as error:
        print(f"❌ Error al descargar el archivo: {error}")
        raise
