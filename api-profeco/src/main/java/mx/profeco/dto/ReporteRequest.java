package mx.profeco.dto;

import mx.profeco.entidades.MotivoReporte;

public class ReporteRequest {
    public MotivoReporte motivo;
    public String descripcion;
    public Long consumidorId;
    public Long tiendaId;
    public Long productoId;
}
