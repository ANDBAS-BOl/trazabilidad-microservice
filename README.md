# Trazabilidad Microservice

Este microservicio gestiona y registra el historial de cambios de estado de cada pedido en el Sistema Plaza de Comidas, proporcionando métricas de eficiencia.

## El sistema completo

Este repositorio es **un componente del Sistema Plaza de Comidas**, compuesto por 4 microservicios independientes más su infraestructura. Cada servicio tiene su propia base de datos y valida el JWT de forma autónoma.

> **Para levantar el sistema, empieza por [`plazoleta-deployment`](https://github.com/ANDBAS-BOl/plazoleta-deployment)**, que arranca MySQL y MongoDB.

| Repositorio | Responsabilidad | Datos |
|---|---|---|
| [`usuarios-microservice`](https://github.com/ANDBAS-BOl/usuarios-microservice) | Usuarios, roles y **emisión de JWT** (único emisor del sistema) | MySQL |
| [`plazoleta-microservice`](https://github.com/ANDBAS-BOl/plazoleta-microservice) | Catálogo de restaurantes/platos, flujo de pedidos y PIN de entrega | MySQL |
| **`trazabilidad-microservice`** ← estás aquí | Historial de estados de pedidos y métricas de eficiencia | MongoDB |
| [`mensajeria-microservice`](https://github.com/ANDBAS-BOl/mensajeria-microservice) | Envío del SMS con el PIN, vía Twilio | — |
| [`plazoleta-deployment`](https://github.com/ANDBAS-BOl/plazoleta-deployment) | Infraestructura Docker: MySQL y MongoDB del sistema | — |

---
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