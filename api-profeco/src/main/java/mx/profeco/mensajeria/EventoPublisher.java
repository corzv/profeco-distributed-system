package mx.profeco.mensajeria;

import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EventoPublisher {
    private static final Logger LOG = Logger.getLogger(EventoPublisher.class);

    @Inject
    @Channel("eventos")
    Emitter<EventoProfeco> emitter;

    public void publicar(EventoProfeco evento) {
        OutgoingRabbitMQMetadata metadata = OutgoingRabbitMQMetadata.builder()
                .withRoutingKey(evento.getTipo())
                .withContentType("application/json")
                .withDeliveryMode(2)
                .build();

        emitter.send(Message.of(evento, Metadata.of(metadata)));
        LOG.infof("Evento %s enviado a RabbitMQ: %s", evento.getTipo(), evento.getMensaje());
    }
}
