package mx.profeco.dto;

/*
    Datos que mnda el cliente en formato JSON para registrar un precio.
 */
public class PrecioRequest {
    public Long tiendaId;
    public Long productoId;
    public Double monto;
}
