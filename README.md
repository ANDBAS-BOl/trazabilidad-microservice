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

## Pruebas y cobertura (JUnit 5 + JaCoCo)

Las 74 pruebas están escritas en **JUnit 5 (Jupiter)** y la cobertura se mide con
**JaCoCo 0.8.9**, ya configurado en `build.gradle` (plugin `jacoco`, exclusiones y
reglas de verificación).

Los tests de integración levantan un **MongoDB embebido** (`de.flapdoodle.embed.mongo`),
así que `./gradlew test` corre de principio a fin **sin Docker ni ninguna base de datos
externa**.

### Tareas Gradle disponibles

| Tarea | Qué hace |
|---|---|
| `./gradlew test` | Ejecuta los tests JUnit 5 (`useJUnitPlatform`) y dispara `jacocoTestReport` al finalizar. |
| `./gradlew jacocoTestReport` | Genera los reportes HTML, XML y CSV bajo `build/reports/jacoco/`. |
| `./gradlew jacocoTestCoverageVerification` | Valida los umbrales mínimos de cobertura. |
| `./gradlew check` | Ejecuta tests + reporte + verificación de cobertura. |

### Generar el reporte

```bash
# Linux / macOS
./gradlew clean test jacocoTestReport

# Windows (PowerShell o CMD)
.\gradlew.bat clean test jacocoTestReport
```

### Visualizar el reporte

Los archivos quedan en:

- **HTML (recomendado)**: `build/reports/jacoco/html/index.html`
- **XML (CI / SonarQube)**: `build/reports/jacoco/jacoco.xml`
- **CSV**: `build/reports/jacoco/jacoco.csv`

### Reglas de cobertura activas

Definidas en `jacocoTestCoverageVerification` (build.gradle):

| Ámbito | Métrica | Umbral mínimo |
|---|---|---|
| Bundle (global) | INSTRUCTION | 75% |
| Bundle (global) | BRANCH | 65% |
| `domain.usecase` | INSTRUCTION | 90% |
| `domain.usecase` | BRANCH | 85% |

`check.dependsOn jacocoTestCoverageVerification`, por lo que el build romperá si
una regla no se cumple.

### Cobertura actual del microservicio

Última ejecución (`./gradlew clean test jacocoTestReport`): **74 pruebas en 19 clases,
0 fallos**.

| Métrica | Cubierto / Total | % |
|---|---|---|
| Instrucciones | 284 / 285 | **99,65%** |
| Ramas | 17 / 18 | **94,44%** |
| Líneas | 66 / 66 | **100%** |
| Métodos | 19 / 19 | **100%** |
| Clases | 8 / 8 | **100%** |

Cobertura por paquete:

| Paquete | Instrucciones | Ramas |
|---|---|---|
| `domain.usecase` (núcleo hexagonal) | **100%** | — |
| `domain.model` | 99,25% | 94,44% |
| `domain.utils` | 100% | — |
| `application.handler.impl` | 100% | — |
| `infrastructure.input.rest` | 100% | — |
| `infrastructure.out.mongo.adapter` | 100% | — |

> **Sobre qué se mide:** los porcentajes corresponden al bundle **después de aplicar
> `jacocoExclusions`**, que deja fuera la clase `main`, la configuración de beans, el
> `ControllerAdvisor`, el paquete `security`, los DTOs, los documentos de Mongo, las
> excepciones de dominio y los `*MapperImpl` generados por MapStruct. Por eso el total
> son 8 clases y no todo `src/main`: se mide la lógica propia, no el cableado ni el
> código generado. Los paquetes sin ramas aparecen como `—`.
