"""
Script para leer un archivo desde OneDrive utilizando Microsoft Graph API.
- Implementa un flujo robusto con manejo de errores detallado y logging estructurado.

Características:
1. Manejo de errores con logs claros y detallados.
2. Uso de un manejador de archivos para realizar operaciones en OneDrive.
3. Validación de configuraciones iniciales para garantizar una ejecución segura.
"""

import logging
from microsoft_graph import MicrosoftGraphAuth, OneDriveFileManager
from util import initialize_application, log_exception

# Configuración de logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
    handlers=[
        logging.StreamHandler()  # Permite loguear en la consola
    ]
)

def main():
    """
    Punto de entrada principal del script:
    - Valida configuraciones iniciales.
    - Obtiene el token de acceso.
    - Inicializa el manejador de archivos de OneDrive.
    - Abre y lee un archivo específico dentro de OneDrive.

    Buenas prácticas implementadas:
    - Validación robusta de configuraciones iniciales.
    - Manejo de errores con logs y contexto adicional.
    - Modularidad para garantizar una fácil extensibilidad.
    """
    try:
        # Inicializar la aplicación y validar configuraciones
        logging.info("Inicializando la aplicación...")
        initialize_application()

        # Inicializar autenticación y obtener el token de acceso
        logging.info("Obteniendo el token de acceso desde Microsoft Graph...")
        auth = MicrosoftGraphAuth()
        token = auth.get_access_token()

        # Inicializar el manejador de archivos de OneDrive
        logging.info("Inicializando el manejador de archivos de OneDrive...")
        file_manager = OneDriveFileManager(access_token=token)

        # Nombre del archivo que se desea abrir en OneDrive
        file_name = "prueba1.jpg"  # Solo el nombre del archivo
        logging.info(f"Intentando abrir el archivo: '{file_name}' en la carpeta predeterminada de OneDrive...")

        # Verificar si el archivo existe antes de leerlo
        if file_manager.file_exists(file_name):
            # Abrir y leer el archivo desde OneDrive
            file_content = file_manager.open_file(file_name)
            
            # Loggear el éxito y mostrar información del contenido
            logging.info(f"Archivo leído exitosamente desde OneDrive: '{file_name}'.")
            print(f"Contenido del archivo (primeros 100 caracteres): {file_content[:100]}...")  # Mostrar primeros 100 caracteres
        else:
            logging.error(f"El archivo '{file_name}' no existe en la carpeta predeterminada de OneDrive.")

    except ValueError as ve:
        log_exception(ve, context="validación de parámetros")
    except ConnectionError as ce:
        log_exception(ce, context="error de conexión")
    except Exception as e:
        log_exception(e, context="ejecución general del programa")

if __name__ == "__main__":
    main()
