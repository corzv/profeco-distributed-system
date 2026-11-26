package mx.profeco.dto;

import java.time.LocalDate;

/*
    Datos que manda el cliente en formato JSON para registrar una oferta.
 */
public class OfertaRequest {
    public Long tiendaId;
    public Long productoId;
    public String descripcion;
    public Double precioOferta;
    public LocalDate fechaInicio;
    public LocalDate fechaFin;
}
