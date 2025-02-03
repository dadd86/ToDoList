import os
from googleapiclient.errors import HttpError
from googleapiclient.http import MediaFileUpload
from util import authenticate, set_folder_permissions, extract_drive_id
from File import open_file_from_photos, list_file_permissions, download_file, upload_file_to_photos
from Folder import find_folder, get_folder_parents, create_folder, list_files_in_folder, list_folder_permissions, assign_permissions_to_all_files

def main():
    """
    Función principal para gestionar archivos en Google Drive:
    
    1️⃣ Autentica el usuario con Google Drive.
    2️⃣ Obtiene y verifica los IDs del archivo y la carpeta.
    3️⃣ Verifica y asigna permisos a la carpeta y archivos.
    4️⃣ Descarga un archivo desde la carpeta 'Fotos' en Google Drive.
    5️⃣ Sube un archivo a la carpeta 'Fotos'.

    Excepciones manejadas:
    ----------------------
    - ValueError: Si hay problemas con los IDs extraídos.
    - FileNotFoundError: Si el archivo o carpeta no se encuentra.
    - googleapiclient.errors.HttpError: Errores en la API de Google Drive.
    - Exception: Otros errores generales.
    """
    try:
        # 🔹 1. Autenticación con Google Drive
        print("🔄 Iniciando autenticación con Google Drive...")
        service = authenticate()
        print("✅ Autenticación exitosa.")

        # 🔹 2. Extraer IDs de la URL compartida
        file_url = "https://drive.google.com/file/d/1zEP9whFIM2HoLzFxSsyvYGpYlkGPds-e/view?usp=sharing"
        folder_url = "https://drive.google.com/drive/folders/1N_zHypYe1TL5F6lSacKlJYiEyJutopKB?usp=sharing"

        try:
            file_id = extract_drive_id(file_url)
            folder_id = extract_drive_id(folder_url)
            print(f"📌 ID del archivo: {file_id}")
            print(f"📌 ID de la carpeta: {folder_id}")
        except ValueError as e:
            print(f"❌ Error al extraer IDs de Google Drive: {e}")
            return

        # 🔹 3. Verificar permisos del archivo
        try:
            print("🔎 Verificando permisos del archivo...")
            list_file_permissions(service, file_id)
        except Exception as e:
            print(f"❌ Error al obtener permisos del archivo: {e}")

        # 🔹 4. Descargar el archivo
        try:
            print(f"⬇️ Descargando archivo con ID: {file_id}...")
            download_file(service, file_id)
            print("✅ Descarga completada.")
        except Exception as e:
            print(f"❌ Error al descargar el archivo: {e}")

        # 🔹 5. Asignar permisos a la carpeta 'Fotos'
        try:
            print("🔑 Asignando permisos a la carpeta 'Fotos'...")
            set_folder_permissions(service, folder_id, "writer", "anyone")
            print("✅ Permisos asignados correctamente.")
        except Exception as e:
            print(f"❌ Error al asignar permisos a la carpeta: {e}")

        # 🔹 6. Buscar o crear la carpeta 'Fotos'
        try:
            folder_id = find_folder(service, "Fotos")
            if folder_id:
                print(f"✅ Carpeta 'Fotos' encontrada. ID: {folder_id}")
                list_files_in_folder(service, folder_id)
            else:
                print("⚠️ La carpeta 'Fotos' no existe. Creándola...")
                folder_id = create_folder(service, "Fotos")
                print(f"✅ Carpeta creada con éxito. ID: {folder_id}")
        except Exception as e:
            print(f"❌ Error al buscar/crear la carpeta: {e}")
            return

        # 🔹 7. Obtener carpeta padre de 'Fotos'
        try:
            parent_ids = get_folder_parents(service, folder_id)
            if parent_ids:
                print(f"📂 ID(s) de la carpeta padre: {parent_ids}")
            else:
                print("📂 La carpeta 'Fotos' está directamente en la raíz de 'Mi unidad'.")
        except Exception as e:
            print(f"❌ Error al obtener los padres de la carpeta: {e}")

        # 🔹 8. Asignar permisos a todos los archivos en 'Fotos'
        try:
            print("🔑 Asignando permisos a todos los archivos en la carpeta 'Fotos'...")
            assign_permissions_to_all_files(service, folder_id, role="writer", type="anyone")
            print("✅ Permisos asignados a todos los archivos.")
        except Exception as e:
            print(f"❌ Error al asignar permisos a los archivos: {e}")

        # 🔹 9. Descargar un archivo desde 'Fotos'
        file_name = "prueba1.jpg"
        download_path = "../descargas"

        os.makedirs(download_path, exist_ok=True)

        try:
            print(f"🖼️ Buscando y descargando '{file_name}' desde la carpeta 'Fotos'...")
            local_file_path = open_file_from_photos(service, folder_id, file_name, download_path)

            if os.path.exists(local_file_path):
                print(f"✅ Archivo descargado en: {local_file_path}")
            else:
                print("❌ El archivo no se descargó correctamente.")
        except FileNotFoundError as e:
            print(f"❌ Archivo no encontrado: {e}")
        except Exception as e:
            print(f"❌ Error al abrir el archivo '{file_name}': {e}")
            
        # 🔹 10. Subir archivo
        upload_file = "example.jpg"  # Reemplazar con un archivo existente en tu sistema
        upload_path = "../descargas/example.jpg"
        
        try:
            print(f"📤 Subiendo el archivo {upload_file} a la carpeta 'Fotos'...")
            upload_file_to_photos(service, folder_id, upload_file, upload_path)
        except Exception as e:
            print(f"❌ Error al subir el archivo '{upload_file}': {e}")

    except Exception as e:
        print(f"❌ Se produjo un error durante la ejecución: {e}")

if __name__ == "__main__":
    main()
