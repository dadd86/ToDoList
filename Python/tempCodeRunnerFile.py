<<<<<<< HEAD
example
=======
if __name__ == "__main__":
    try:
        # Obtener el token de acceso
        token = get_access_token()
        logging.info(f"Token obtenido: {token}")

        # Subir un archivo de ejemplo
        response = upload_file(token, "example.jpg", "example.jpg")
        logging.info(f"Archivo subido exitosamente: {response}")
    except Exception as e:
        logging.error(f"Error durante la ejecución: {e}")
>>>>>>> b2f110ae6211a784e2900ac11ca5a7768e220796
