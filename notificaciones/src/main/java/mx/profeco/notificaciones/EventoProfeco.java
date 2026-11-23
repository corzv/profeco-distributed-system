package mx.profeco.notificaciones;

import java.time.LocalDateTime;

/** Contrato compartido con los productores de la API; los IDs pueden no aplicar. */
public record EventoProfeco(Tipo tipo, String mensaje, Long tiendaId, Long productoId,
                            LocalDateTime fecha) {
    public enum Tipo { OFERTA_PUBLICADA, REPORTE_CREADO, MULTA_APLICADA }

    public EventoProfeco {
        if (tipo == null || mensaje == null || mensaje.isBlank() || fecha == null) {
            throw new IllegalArgumentException("tipo, mensaje no vacío y fecha son obligatorios");
        }
        if ((tiendaId != null && tiendaId <= 0) || (productoId != null && productoId <= 0)) {
            throw new IllegalArgumentException("Los identificadores deben ser positivos");
        }
    }
}
