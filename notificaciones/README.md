# Servicio de Notificaciones — Abraham Coronel

Entrega de Abraham: consumidor RabbitMQ, envío de avisos mediante SSE y diagrama de componentes actualizado. No implementa la API Profeco ni la infraestructura compartida de Manuel.

Se utiliza Java 21 y Quarkus 3.20.3. La primera imagen define SSE y puerto **8082**, por lo que reemplazan WebSocket y 8081 de los diagramas originales. Firebase queda como integración futura, fuera de esta entrega.

## Ejecutar

Requisitos: JDK 21, Maven 3.9+ y el RabbitMQ proporcionado por infraestructura en localhost:5672. Credenciales locales predeterminadas: `profeco` / `REMOVIDO`. Configurar `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USERNAME` y `RABBITMQ_PASSWORD` para el entorno del equipo.

Desde esta carpeta:

```powershell
mvn test
mvn quarkus:dev
```

Abrir http://localhost:8082 en dos pestañas para observar la difusión a varios clientes. El endpoint es `GET /notificaciones/stream` (`text/event-stream`). También puede usarse:

```powershell
curl.exe -N http://localhost:8082/notificaciones/stream
```

Salud: `GET /q/health`, `GET /q/health/ready` y `GET /q/health/live`. La disponibilidad incluye el conector RabbitMQ.

Para empaquetar: `mvn package`, luego `java -jar target/quarkus-app/quarkus-run.jar`. Se incluye un Dockerfile exclusivo del servicio para que Manuel lo integre al despliegue.

## Contrato para Sebastián y Ángel

Publicar un objeto JSON con propiedad AMQP `content_type=application/json`. Los mensajes deben ser persistentes (`delivery_mode=2`) si se requiere conservarlos durante un reinicio del broker. Se consume la cola durable **profeco.eventos**, enlazada al exchange **profeco.eventos** de tipo `topic` mediante `#`. La API puede publicar en ese exchange con routing key igual al tipo de evento o directamente en el exchange predeterminado (`""`) con routing key `profeco.eventos`, una vez declarada la cola.

```json
{
  "tipo": "OFERTA_PUBLICADA",
  "mensaje": "Nueva oferta en la tienda Centro",
  "tiendaId": 1,
  "productoId": 3,
  "fecha": "2026-11-23T10:00:00"
}
```

Tipos aceptados: `OFERTA_PUBLICADA`, `REPORTE_CREADO`, `MULTA_APLICADA`. `tipo`, `mensaje` no vacío y `fecha` ISO local son obligatorios. `tiendaId` y `productoId` pueden omitirse o ser null cuando no aplican; si se proporcionan deben ser positivos. No incluir datos personales: los avisos son públicos y se difunden a todos los clientes conectados.

El servicio declara la cola, exchange y cola de mensajes rechazados `profeco.eventos.dlq` con exchange `DLX`. Si infraestructura ya declaró la cola sin esos argumentos, hay que acordar la misma configuración antes de iniciar: RabbitMQ rechaza declaraciones incompatibles. No borrar una cola con mensajes para resolverlo.

## Demostración sin modificar las APIs

1. Iniciar el RabbitMQ del equipo y este servicio.
2. Abrir la página de demostración en dos pestañas y esperar el estado conectado.
3. En el panel RabbitMQ (normalmente http://localhost:15672), abrir **Exchanges → profeco.eventos → Publish message**.
4. Seleccionar routing key `OFERTA_PUBLICADA`, agregar `content_type=application/json` en las propiedades y pegar el JSON anterior como payload.
5. Verificar el mismo aviso en ambas pestañas. Repetir con los otros dos tipos.
6. Enviar un tipo desconocido y comprobar que el mensaje termina en `profeco.eventos.dlq` sin aparecer en los clientes.

## Alcance y garantías

El ACK confirma procesamiento y difusión en memoria, no recepción ni lectura por cada usuario. SSE entrega avisos solo mientras el cliente está conectado; no hay historial ni recuperación mediante Last-Event-ID. La página conserva como máximo 100 avisos en memoria y reconecta automáticamente. No se implementan persistencia de Notificacion ni marcarComoLeida del diagrama de dominio porque requieren identidad e historial, fuera del alcance asignado.

Usar **una sola instancia** del servicio: varias instancias consumiendo la misma cola se reparten los mensajes y cada una solo notifica a sus clientes. Para escalar hay que acordar una cola por instancia enlazada al mismo exchange. Un cliente lento puede perder la conexión por backpressure y reconectar. En un proxy inverso se debe desactivar el buffering de SSE y ajustar el timeout de lectura.

Las pruebas unitarias comprueban difusión a dos suscriptores, los tres tipos, ACK y rechazo de eventos inválidos. La prueba completa RabbitMQ → SSE se realiza con la demostración anterior.

Referencias: [RabbitMQ en Quarkus](https://quarkus.io/version/3.20/guides/rabbitmq-reference/) y [SSE en Quarkus REST](https://quarkus.io/version/3.20/guides/rest/).
