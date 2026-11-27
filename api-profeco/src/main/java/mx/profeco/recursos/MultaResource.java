package mx.profeco.recursos;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import mx.profeco.dto.MultaRequest;
import mx.profeco.entidades.EstadoReporte;
import mx.profeco.entidades.Multa;
import mx.profeco.entidades.Reporte;
import mx.profeco.mensajeria.EventoProfeco;
import mx.profeco.mensajeria.EventoPublisher;

@Path("/multas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MultaResource {
    @Inject
    EventoPublisher publisher;

    @GET
    public List<Multa> listar(@QueryParam("tiendaId") Long tiendaId) {
        if (tiendaId != null) {
            return Multa.list("tiendaId", tiendaId);
        }
        return Multa.listAll();
    }

    @POST
    @Transactional
    public Response crear(MultaRequest datos) {
        if (datos == null || datos.tiendaId == null) {
            return error(Response.Status.BAD_REQUEST, "Envía tiendaId");
        }
        if (datos.monto == null || datos.monto <= 0) {
            return error(Response.Status.BAD_REQUEST, "El monto debe ser mayor a 0");
        }
        if (datos.motivo == null || datos.motivo.isBlank()) {
            return error(Response.Status.BAD_REQUEST, "Envía el motivo de la multa");
        }

        // Si viene reporteId, validar que exista y marcarlo como resuelto
        if (datos.reporteId != null) {
            Reporte reporte = Reporte.findById(datos.reporteId);
            if (reporte == null) {
                return error(Response.Status.NOT_FOUND, "No existe el reporte " + datos.reporteId);
            }
            reporte.setEstado(EstadoReporte.RESUELTO);
        }

        Multa multa = new Multa(datos.monto, datos.motivo, datos.tiendaId,
                datos.agenteId, datos.reporteId);
        multa.persist();

        String mensaje = "Multa de $" + datos.monto + " aplicada a tienda " + datos.tiendaId;
        EventoProfeco evento = new EventoProfeco(EventoProfeco.MULTA_APLICADA, mensaje,
                datos.tiendaId, null);
        publisher.publicar(evento);

        return Response.status(Response.Status.CREATED).entity(multa).build();
    }

    private Response error(Response.Status status, String mensaje) {
        return Response.status(status).entity(Map.of("mensaje", mensaje)).build();
    }
}
