ToDoList app

1. Planificación del Proyecto
El proyecto constará de:

Interfaz gráfica (JavaFX): Para permitir al usuario seleccionar y subir archivos.
Conexión a SQL (MySQL): Para almacenar los metadatos de los archivos.
Integración con OneDrive: Usaremos Microsoft Graph API para guardar los archivos en la nube.
Gestión de dependencias: Usaremos Maven para organizar y gestionar las dependencias.

2. Configuración del Entorno
Requisitos previos
	Java JDK: Instala JDK 11 o superior y configura la variable de entorno JAVA_HOME.
	MySQL: Instala y configura un servidor MySQL. También puedes usar otra base SQL como PostgreSQL.
	IntelliJ IDEA: Descarga IntelliJ IDEA Community o Ultimate.
	Azure (Microsoft OneDrive API): Configura una aplicación en el portal de Azure para acceder a OneDrive.

3. Configuración de Azure y OneDrive API
	Paso 1: Configurar Azure
		Ve al Portal de Azure.

		Navega a Azure Active Directory > Registros de aplicaciones > Nuevo registro:

		Nombre: AplicacionOneDrive.
		URI de redirección: http://localhost.
		Crea credenciales de cliente:

		Ve a Certificados y secretos > Nuevo secreto de cliente.
		Copia el client_id y el client_secret.
		Configura permisos API:

		Ve a Permisos API > Agregar un permiso > Microsoft Graph.
		Habilita Files.ReadWrite.

Paso 2: Obtén el código de autorización
	Genera un URL para autenticarte:

	https://login.microsoftonline.com/common/oauth2/v2.0/authorize
	?client_id=TU_CLIENT_ID
	&response_type=code
	&redirect_uri=http://localhost
	&scope=Files.ReadWrite


	Accede a este enlace y copia el código de autorización obtenido en la redirección.


	1. Configurar Azure Active Directory (AAD)
    Acceder a Azure Active Directory:

    En el portal de Azure, busca "Azure Active Directory" en la barra de búsqueda (como se muestra en tu captura) y selecciona el servicio Microsoft Entra ID (Azure Active Directory).
    Registrar una nueva aplicación:

    En el panel de Azure AD, selecciona "Registros de aplicaciones".
    Haz clic en "Nuevo registro".
    Proporciona un nombre para la aplicación, por ejemplo, JavaApp-OneDrive.
    Selecciona Cuentas en cualquier directorio organizativo y cuentas personales de Microsoft como tipo de cuenta.
    Configura una URL de redirección para tu aplicación, por ejemplo:
    Para desarrollo local: http://localhost:8080
    Haz clic en Registrar.
    Guardar los detalles de la aplicación registrada:

    Una vez registrada, anota:
    ID de la aplicación (cliente).
    ID de directorio (inquilino).
    Configurar un secreto de cliente:

    En la página de la aplicación registrada, ve a Certificados y secretos.
    Haz clic en Nuevo secreto de cliente.
    Asigna un nombre al secreto y selecciona una duración (por ejemplo, 6 meses).
    Copia el valor del secreto generado. No podrás verlo nuevamente.
    Asignar permisos de API a Microsoft Graph:

    En la misma aplicación, ve a Permisos de API.
    Haz clic en Agregar un permiso.
    Selecciona Microsoft Graph.
    Elige Delegados y habilita los permisos necesarios para OneDrive:
    Files.ReadWrite (para leer y escribir archivos en OneDrive).
    offline_access (para tokens de actualización).
    Haz clic en Agregar permisos.
    Haz clic en Grant admin consent (Otorgar consentimiento de administrador).