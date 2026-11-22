package mx.profeco.notificaciones;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EventoConsumer {
    private static final Logger LOG = Logger.getLogger(EventoConsumer.class);
    @Inject ObjectMapper mapper;
    @Inject NotificacionBroadcaster broadcaster;

    @Incoming("eventos")
    public CompletionStage<Void> consumir(Message<JsonObject> message) {
        try {
            EventoProfeco evento = mapper.readValue(message.getPayload().encode(), EventoProfeco.class);
            broadcaster.publicar(evento);
        } catch (Exception error) {
            LOG.warn("Evento inválido o no procesable; se rechaza hacia la DLQ", error);
            return message.nack(error);
        }
        // ACK significa procesado por este servicio, no leído por un usuario.
        return message.ack();
    }
}
