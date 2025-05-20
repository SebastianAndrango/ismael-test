# Ismael Andrango - Prueba técnica

Este proyecto construye dos servicios REST utilizando Java Quarkus:
-Concatenar cinco parámetros
-Consumir un API pública (PokeApi)
Emite mensajes usando EventBus y una tarea programada cada 5 minutos.

## Requisitos técnicos

-Java 17 o superior
-Maven 3.5 o superior

## Instalación, compilación y ejecución

1. Clonar repositorio:

```bash
git clone git@gitlab.com:developerba-g10/ismael-andrago.git
```

2. Acceder a la carpeta ismael-test:

```bash
cd ismael-test
```

3. Compilar el proyecto:

```bash
   mvn clean package
```

4. Iniciar el proyecto usando Maven en modo desarrollo:

```bash
mvn quarkus:dev
```

## Ejecución usando un Script

Se debe abrir una terminal BASH.

```bash
chmod +x start.sh
```

Iniciar el proyecto en modo desarrollo:

```bash
./start.sh dev
```

Iniciar el proyecto en modo producción:

```bash
./start.sh prod
```

## Puertos de ejecución
- Proyecto: `http://localhost:15050`
- Swagger UI: `http://localhost:15050/q/swagger-ui`
