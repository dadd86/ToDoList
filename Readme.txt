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