# Sistema de Control de Equipos de Laboratorio

## 1. Descripción del proyecto

El Sistema de Control de Equipos de Laboratorio es una aplicación de escritorio desarrollada en Java que permite administrar la información de los equipos de un laboratorio.

Su propósito es facilitar el control del inventario mediante una interfaz gráfica que permite registrar, consultar, modificar, eliminar y buscar equipos. Además, cuenta con validaciones para garantizar que la información ingresada sea correcta.

La aplicación utiliza PostgreSQL para almacenar los datos y Hibernate con JPA para gestionar la persistencia de la información.

## 2. Objetivo del proyecto

Desarrollar una aplicación que permita llevar un control organizado de los equipos de laboratorio, aplicando operaciones CRUD, validaciones de datos y conexión con una base de datos relacional.

## 3. Tecnologías utilizadas

| Tecnología | Descripción |
|---|---|
| Java 21 | Lenguaje utilizado para desarrollar la aplicación. |
| JavaFX | Permite crear la interfaz gráfica de usuario. |
| FXML | Define la estructura visual de la interfaz. |
| Hibernate | Facilita la comunicación entre Java y la base de datos. |
| JPA | Permite mapear las clases Java con las tablas de la base de datos. |
| PostgreSQL | Almacena la información de los equipos. |
| Maven | Administra las dependencias del proyecto. |
| IntelliJ IDEA | Entorno utilizado para desarrollar la aplicación. |

## 4. Funcionalidades principales

El sistema cuenta con las siguientes funcionalidades:

- **Registrar equipos:** permite ingresar nuevos equipos con su código, nombre, marca, tipo, estado y cantidad.
- **Consultar equipos:** muestra los equipos almacenados en la base de datos.
- **Editar equipos:** permite modificar la información de un equipo previamente registrado.
- **Eliminar equipos:** permite eliminar equipos del inventario mediante una confirmación.
- **Buscar por nombre:** facilita encontrar equipos utilizando su nombre o una parte de este.
- **Filtrar por tipo:** permite visualizar los equipos según su categoría.
- **Combinar búsquedas:** permite utilizar diferentes criterios para localizar equipos.
- **Validar información:** comprueba que los datos ingresados cumplan con las condiciones establecidas.

## 5. Validaciones del sistema

Para mantener la integridad de la información, se implementan las siguientes validaciones:

- El código del equipo no debe estar duplicado.
- Los campos obligatorios deben contener información.
- La cantidad de equipos no puede ser negativa.
- Cada equipo debe tener un tipo asignado.
- El estado del equipo debe corresponder a uno de los valores permitidos.
- Antes de eliminar un equipo, se solicita confirmación.

Los estados contemplados son:

- Disponible.
- En mantenimiento.
- Fuera de servicio.

## 6. Estructura del proyecto

El código está organizado en diferentes paquetes para facilitar su mantenimiento y comprensión.

```text
laboratorio-equipos/
│
├── Evidencias/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/laboratorioequipos/
│       │       ├── controller/
│       │       ├── dao/
│       │       ├── database/
│       │       ├── model/
│       │       ├── Main.java
│       │       └── MainApp.java
│       │
│       └── resources/
│           ├── META-INF/
│           │   └── persistence.xml
│           └── view/
│               └── equipo_view.fxml
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── Script de la base de datos.sql
└── .gitignore
```

### Descripción de los paquetes

- **controller:** contiene el controlador que administra las acciones de la interfaz gráfica.
- **dao:** contiene las clases encargadas de realizar las operaciones de acceso a los datos.
- **database:** administra la configuración y creación de los recursos de persistencia mediante JPA.
- **model:** contiene las entidades `Equipo` y `TipoEquipo`, que representan los datos almacenados.
- **resources:** contiene el diseño FXML y el archivo de configuración de Hibernate y JPA.
- **Evidencias:** almacena capturas de pantalla que muestran el funcionamiento de las diferentes operaciones.

## 7. Base de datos

El sistema utiliza PostgreSQL con la siguiente configuración:

- **Nombre:** `bd_laboratorio_orm`
- **Esquema:** `inventario`
- **Tablas principales:** `equipo` y `tipo_equipo`.

### Relación entre las tablas

La tabla `tipo_equipo` almacena las categorías disponibles, mientras que la tabla `equipo` contiene los datos de cada equipo registrado.

Ambas tablas están relacionadas mediante el campo `id_tipo`, permitiendo que varios equipos pertenezcan a una misma categoría.

### Script SQL

El proyecto incluye el archivo `Script de la base de datos.sql`, que contiene las instrucciones necesarias para crear las tablas, establecer sus relaciones e insertar datos iniciales de prueba.

## 8. Instalación y ejecución

### Requisitos previos

Antes de ejecutar el proyecto, se necesita:

- Tener instalado Java JDK 21.
- Tener PostgreSQL instalado.
- Contar con IntelliJ IDEA.
- Disponer de conexión a internet para descargar las dependencias de Maven.

### Paso 1. Clonar el repositorio

```bash
git clone https://github.com/RMSE233/laboratorio-equipos.git
```

### Paso 2. Crear la base de datos

Desde PostgreSQL o pgAdmin, ejecutar:

```sql
CREATE DATABASE bd_laboratorio_orm;
```

### Paso 3. Ejecutar el script SQL

Abrir el archivo `Script de la base de datos.sql` y ejecutar su contenido dentro de la base de datos creada.

### Paso 4. Configurar la conexión

Revisar el archivo:

`src/main/resources/META-INF/persistence.xml`

Configurar las credenciales correspondientes de PostgreSQL. La contraseña se obtiene mediante la variable de entorno `BD_PASSWORD`.

En Windows PowerShell se puede establecer de la siguiente manera:

```powershell
$env:BD_PASSWORD="tu_contraseña"
```

### Paso 5. Ejecutar el proyecto

1. Abrir el proyecto desde IntelliJ IDEA.
2. Esperar a que Maven descargue las dependencias.
3. Localizar la clase `Main.java`.
4. Ejecutar su método principal para iniciar la aplicación.

## 9. Evidencias de funcionamiento

El proyecto incluye una carpeta llamada `Evidencias`, donde se encuentran capturas que permiten comprobar el funcionamiento del sistema.

Entre las pruebas realizadas se encuentran:

- Registro de equipos.
- Visualización de equipos registrados.
- Búsqueda por nombre parcial.
- Filtrado por tipo de equipo.
- Edición de información.
- Eliminación con confirmación.
- Validación de cantidades negativas.
- Rechazo de códigos duplicados.
- Reinicio de la aplicación.

## 10. Conclusión

El desarrollo de este proyecto permite aplicar conocimientos de programación orientada a objetos, interfaces gráficas, conexión con bases de datos y persistencia de información.

Mediante la integración de JavaFX, Hibernate, JPA y PostgreSQL, se obtiene una aplicación que facilita la administración de los equipos de laboratorio, manteniendo la información organizada y permitiendo realizar las operaciones necesarias para el control del inventario.

## 11. Repositorio

**GitHub:** [RMSE233/laboratorio-equipos](https://github.com/RMSE233/laboratorio-equipos)
