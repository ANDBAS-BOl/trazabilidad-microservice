# Trazabilidad Microservice

Este microservicio gestiona y registra el historial de cambios de estado de cada pedido en el Sistema Plaza de Comidas, proporcionando métricas de eficiencia.

## Rol en el Sistema
* **Trazabilidad (HU 17):** Persiste eventos de cambio de estado por pedido; el cliente consulta el historial solo de sus propios pedidos.
* **Escritura de eventos:** Invocada por Plazoleta (empleado) o rutas que registren transiciones; roles `EMPLEADO` o `CLIENTE` en `POST /api/v1/trazabilidad/eventos` según el diseño del flujo.
* **Eficiencia operativa (HU 18):** Las agregaciones y ranking por empleado se exponen desde **Plazoleta**, no desde este servicio.
* **Autenticación:** Valida tokens JWT con la clave compartida.
* **Base de Datos:** MongoDB.

## Requisitos Previos
* JDK 17 o superior (Recomendado JDK 21 compilando a Target 17).
* Gradle 8.5.
* Docker y Docker Compose para levantar la base de datos MongoDB (puerto 27017).

## Cómo ejecutar localmente
Repositorio de infraestructura: [plazoleta-deployment](https://github.com/ANDBAS-BOl/plazoleta-deployment)

1. Levantar bases de datos:
   Desde la carpeta `plazoleta-deployment`, ejecute:
   ```bash
   docker compose -f docker/compose-db.yml up -d
   ```
2. Iniciar el microservicio:
   Desde la carpeta `trazabilidad-microservice`, ejecute:
   ```bash
   ./gradlew bootRun
   ```

El servicio se iniciará por defecto en el puerto `8083`.