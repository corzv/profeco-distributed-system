package mx.profeco.notificaciones;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;

class EventoConsumerTest {
    private EventoConsumer consumer(NotificacionBroadcaster broadcaster) {
        var consumer = new EventoConsumer();
        consumer.mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        consumer.broadcaster = broadcaster;
        return consumer;
    }

    @Test
    void entregaLosTresTiposATodosLosClientesYAceptaElMensaje() {
        var broadcaster = new NotificacionBroadcaster();
        var consumer = consumer(broadcaster);
        List<EventoProfeco> clienteUno = new ArrayList<>();
        List<EventoProfeco> clienteDos = new ArrayList<>();
        var uno = broadcaster.stream().subscribe().with(clienteUno::add);
        var dos = broadcaster.stream().subscribe().with(clienteDos::add);
        try {
            for (var tipo : EventoProfeco.Tipo.values()) {
                var ack = new AtomicBoolean();
                var payload = new JsonObject().put("tipo", tipo.name()).put("mensaje", "Aviso")
                        .put("tiendaId", 1).put("productoId", 3).put("fecha", "2026-11-23T10:00:00");
                var message = Message.of(payload, () -> {
                    ack.set(true);
                    return CompletableFuture.completedFuture(null);
                });
                consumer.consumir(message).toCompletableFuture().join();
                assertTrue(ack.get());
            }
            assertEquals(3, clienteUno.size());
            assertEquals(clienteUno, clienteDos);
            assertEquals(1L, clienteUno.getFirst().tiendaId());
        } finally {
            uno.cancel();
            dos.cancel();
        }
    }

    @Test
    void rechazaEventosInvalidosSinPublicarlos() {
        var broadcaster = new NotificacionBroadcaster();
        var consumer = consumer(broadcaster);
        List<EventoProfeco> recibidos = new ArrayList<>();
        var subscription = broadcaster.stream().subscribe().with(recibidos::add);
        try {
            for (String payload : List.of(
                    "{}",
                    "{\"tipo\":\"DESCONOCIDO\",\"mensaje\":\"Aviso\",\"fecha\":\"2026-11-23T10:00:00\"}",
                    "{\"tipo\":\"REPORTE_CREADO\",\"mensaje\":\" \",\"fecha\":\"2026-11-23T10:00:00\"}",
                    "{\"tipo\":\"MULTA_APLICADA\",\"mensaje\":\"Aviso\",\"fecha\":\"ayer\"}")) {
                var rejected = new AtomicBoolean();
                var message = Message.of(new JsonObject(payload),
                        () -> CompletableFuture.failedFuture(new AssertionError("No debe confirmar")),
                        error -> {
                            rejected.set(true);
                            return CompletableFuture.completedFuture(null);
                        });
                consumer.consumir(message).toCompletableFuture().join();
                assertTrue(rejected.get());
            }
            assertTrue(recibidos.isEmpty());
        } finally {
            subscription.cancel();
        }
    }
}
