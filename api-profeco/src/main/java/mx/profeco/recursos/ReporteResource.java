package mx.profeco.recursos;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import mx.profeco.dto.EstadoReporteRequest;
import mx.profeco.dto.ReporteRequest;
import mx.profeco.entidades.Reporte;
import mx.profeco.mensajeria.EventoProfeco;
import mx.profeco.mensajeria.EventoPublisher;

@Path("/reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteResource {
    @Inject
    EventoPublisher publisher;

    @GET
    public List<Reporte> listar(@QueryParam("tiendaId") Long tiendaId) {
        if (tiendaId != null) {
            return Reporte.list("tiendaId", tiendaId);
        }
        return Reporte.listAll();
    }

    @POST
    @Transactional
    public Response crear(ReporteRequest datos) {
        if (datos == null || datos.motivo == null) {
            return error(Response.Status.BAD_REQUEST, "Envía el motivo del reporte");
        }
        if (datos.consumidorId == null || datos.tiendaId == null) {
            return error(Response.Status.BAD_REQUEST, "Envía consumidorId y tiendaId");
        }

        Reporte reporte = new Reporte(datos.motivo, datos.descripcion,
                datos.consumidorId, datos.tiendaId, datos.productoId);
        reporte.persist();

        String mensaje = "Reporte creado por consumidor " + datos.consumidorId
                + " contra tienda " + datos.tiendaId;
        EventoProfeco evento = new EventoProfeco(EventoProfeco.REPORTE_CREADO, mensaje,
                datos.tiendaId, datos.productoId);
        publisher.publicar(evento);

        return Response.status(Response.Status.CREATED).entity(reporte).build();
    }

    @PATCH
    @Path("/{id}/estado")
    @Transactional
    public Response actualizarEstado(@PathParam("id") Long id, EstadoReporteRequest datos) {
        if (datos == null || datos.estado == null) {
            return error(Response.Status.BAD_REQUEST, "Envía el nuevo estado");
        }

        Reporte reporte = Reporte.findById(id);
        if (reporte == null) {
            return error(Response.Status.NOT_FOUND, "No existe el reporte " + id);
        }

        reporte.setEstado(datos.estado);
        if (datos.agenteId != null) {
            reporte.setAgenteId(datos.agenteId);
        }

        return Response.ok(reporte).build();
    }

    private Response error(Response.Status status, String mensaje) {
        return Response.status(status).entity(Map.of("mensaje", mensaje)).build();
    }
}
