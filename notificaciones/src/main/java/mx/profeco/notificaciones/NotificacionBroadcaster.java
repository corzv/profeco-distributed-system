package mx.profeco.notificaciones;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificacionBroadcaster {
    private final BroadcastProcessor<EventoProfeco> eventos = BroadcastProcessor.create();

    public Multi<EventoProfeco> stream() {
        return eventos;
    }

    public void publicar(EventoProfeco evento) {
        // Un cliente lento se desconecta por backpressure sin bloquear a los demás.
        eventos.onNext(evento);
    }
}
