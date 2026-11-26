package mx.profeco.mensajeria;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventoProfeco {
    public static final String OFERTA_PUBLICADA = "OFERTA_PUBLICADA";
    public static final String REPORTE_CREADO = "REPORTE_CREADO";
    public static final String MULTA_APLICADA = "MULTA_APLICADA";

    private String tipo;
    private String mensaje;
    private Long tiendaId;
    private Long productoId;
    private String fecha;

    public EventoProfeco() {
    }

    public EventoProfeco(String tipo, String mensaje, Long tiendaId, Long productoId) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.tiendaId = tiendaId;
        this.productoId = productoId;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.fecha = LocalDateTime.now().format(formato);
    }

    public String getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Long getTiendaId() {
        return tiendaId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getFecha() {
        return fecha;
    }
}
