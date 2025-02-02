from googleapiclient.errors import HttpError

# ===========================================
# 🔹 Función para crear una carpeta en Google Drive
# ===========================================

def create_folder(service, folder_name, parent_id=None):
    """
    Crea una nueva carpeta en Google Drive y retorna su ID.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Servicio autenticado de Google Drive.
    folder_name : str
        Nombre de la carpeta a crear en Google Drive.
    parent_id : str, opcional
        ID de la carpeta padre donde se creará la nueva carpeta.
        Si no se especifica, la carpeta se creará en la raíz de 'Mi unidad'.

    Retorna:
    -------
    str
        ID de la carpeta creada.

    Excepciones:
    -----------
    ValueError:
        Si el nombre de la carpeta es vacío o inválido.
    HttpError:
        Si ocurre un error en la API de Google Drive.
    """
    if not folder_name.strip():
        raise ValueError("❌ El nombre de la carpeta no puede estar vacío.")

    folder_metadata = {
        'name': folder_name.strip(),
        'mimeType': 'application/vnd.google-apps.folder'
    }

    if parent_id:
        folder_metadata['parents'] = [parent_id]

    try:
        folder = service.files().create(body=folder_metadata, fields='id').execute()
        print(f"✅ Carpeta '{folder_name}' creada con éxito. ID: {folder.get('id')}")
        return folder.get('id')
    except HttpError as e:
        print(f"❌ Error al crear la carpeta '{folder_name}': {e}")
        raise
    except Exception as e:
        print(f"❌ Error inesperado: {e}")
        raise


# ===========================================
# 🔹 Función para buscar una carpeta en Google Drive
# ===========================================

def find_folder(service, folder_name):
    """
    Busca una carpeta por nombre en Google Drive y retorna su ID.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Servicio autenticado de Google Drive.
    folder_name : str
        Nombre de la carpeta a buscar.

    Retorna:
    -------
    str o None
        ID de la carpeta encontrada o None si no existe.

    Excepciones:
    -----------
    ValueError:
        Si el nombre de la carpeta es inválido.
    HttpError:
        Si ocurre un error en la API de Google Drive.
    """
    if not folder_name.strip():
        raise ValueError("❌ El nombre de la carpeta no puede estar vacío.")

    query = f"name='{folder_name.strip()}' and mimeType='application/vnd.google-apps.folder'"

    try:
        results = service.files().list(q=query, spaces='drive', fields='files(id, name)').execute()
        items = results.get('files', [])

        if not items:
            print(f"⚠️ No se encontró ninguna carpeta con el nombre '{folder_name}'.")
            return None

        folder_id = items[0]['id']
        print(f"✅ Carpeta encontrada: {folder_name} (ID: {folder_id})")
        return folder_id
    except HttpError as e:
        print(f"❌ Error al buscar la carpeta '{folder_name}': {e}")
        raise
    except Exception as e:
        print(f"❌ Error inesperado: {e}")
        raise


# ===========================================
# 🔹 Función para obtener la carpeta padre
# ===========================================

def get_folder_parents(service, folder_id):
    """
    Obtiene el ID de la carpeta padre de una carpeta en Google Drive.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Servicio autenticado de Google Drive.
    folder_id : str
        ID de la carpeta de la cual se quieren obtener los padres.

    Retorna:
    -------
    list
        Lista de IDs de las carpetas padres.

    Excepciones:
    -----------
    HttpError:
        Si ocurre un error en la API de Google Drive.
    """
    try:
        file = service.files().get(fileId=folder_id, fields="parents").execute()
        parents = file.get('parents', [])
        if not parents:
            print(f"📂 La carpeta con ID {folder_id} está en la raíz de 'Mi unidad'.")
        return parents
    except HttpError as e:
        print(f"❌ Error al obtener los padres de la carpeta: {e}")
        raise
    except Exception as e:
        print(f"❌ Error inesperado: {e}")
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

    except HttpError as e:
        print(f"❌ Error al listar archivos: {e}")
        raise


# ===========================================
# 🔹 Función para asignar permisos a todos los archivos en una carpeta
# ===========================================

def assign_permissions_to_all_files(service, folder_id, role="writer", type="anyone"):
    """
    Asigna permisos a todos los archivos dentro de una carpeta específica en Google Drive.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Servicio autenticado de Google Drive.
    folder_id : str
        ID de la carpeta donde se encuentran los archivos.
    role : str
        Rol a asignar: 'reader' (lector) o 'writer' (escritor).
    type : str
        Tipo de entidad: 'user', 'group', 'domain' o 'anyone'.

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
            print(f"⚠️ No se encontraron archivos en la carpeta con ID: {folder_id}")
            return

        for file in files:
            try:
                print(f"🔑 Asignando permisos al archivo: {file['name']} (ID: {file['id']})")
                service.permissions().create(
                    fileId=file['id'],
                    body={"role": role, "type": type},
                    fields="id"
                ).execute()
                print(f"✅ Permisos asignados al archivo: {file['name']}")
            except HttpError as e:
                print(f"❌ Error al asignar permisos al archivo {file['name']}: {e}")
            except Exception as e:
                print(f"❌ Error inesperado: {e}")

    except HttpError as e:
        print(f"❌ Error al listar archivos en la carpeta {folder_id}: {e}")
        raise
    except Exception as e:
        print(f"❌ Error inesperado: {e}")
        raise


def list_folder_permissions(service, folder_id):
    """
    Lista los permisos actuales de una carpeta en Google Drive.

    Parámetros:
    ----------
    service : googleapiclient.discovery.Resource
        Objeto autenticado para interactuar con la API de Google Drive.
    folder_id : str
        ID de la carpeta a verificar.

    Retorna:
    -------
    None
        Imprime los permisos en la consola.

    Excepciones:
    -----------
    HttpError:
        Si ocurre un error durante la interacción con la API de Google Drive.
    """
    try:
        permissions = service.permissions().list(fileId=folder_id, fields="permissions").execute()
        print(f"🔑 Permisos actuales de la carpeta ID: {folder_id}")
        for perm in permissions.get("permissions", []):
            print(f" - ID: {perm.get('id')}, Rol: {perm.get('role')}, Tipo: {perm.get('type')}, Email: {perm.get('emailAddress')}")
    except HttpError as e:
        print(f"❌ Error al listar permisos: {e}")
        raise
    except Exception as e:
        print(f"❌ Error inesperado: {e}")
        raise
    
def delete_folder(service, folder_id):
    """
    Elimina una carpeta en Google Drive si la aplicación tiene permisos.
    """
    try:
        service.files().delete(fileId=folder_id).execute()
        print(f"✅ Carpeta con ID {folder_id} eliminada correctamente.")
    except Exception as e:
        print(f"❌ Error al eliminar la carpeta: {e}")

