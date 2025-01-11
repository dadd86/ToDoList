import os
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

