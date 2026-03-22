# Trazabilidad Microservice

Este microservicio gestiona y registra el historial de cambios de estado de cada pedido en el Sistema Plaza de Comidas, proporcionando métricas de eficiencia.

## Rol en el Sistema
* **Trazabilidad:** Guarda logs inmutables cada vez que un pedido cambia de estado.
* **Métricas:** Permite analizar la eficiencia operativa (tiempos desde la creación hasta ENTREGADO) para los Propietarios y listar el tiempo transcurrido por estado para los Clientes.
* **Autenticación:** Valida tokens JWT con la clave compartida.
* **Base de Datos:** MongoDB.

## Requisitos Previos
* JDK 17 o superior (Recomendado JDK 21 compilando a Target 17).
* Gradle 8.5.
* Docker y Docker Compose para levantar la base de datos MongoDB (puerto 27017).

## Configuración y Ejecución
1. Levantar la base de datos:
   Desde la raíz del proyecto principal, ejecute:
   ```bash
   docker compose -f docker/compose-db.yml up -d
   ```
2. Iniciar el microservicio:
   Desde la carpeta `trazabilidad-microservice`, ejecute:
   ```bash
   ./gradlew bootRun
   ```

El servicio se iniciará por defecto en el puerto `8083`.
