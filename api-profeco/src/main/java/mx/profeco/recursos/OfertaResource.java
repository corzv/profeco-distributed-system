package mx.profeco.recursos;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mx.profeco.dto.OfertaRequest;
import mx.profeco.entidades.Oferta;
import mx.profeco.entidades.Producto;
import mx.profeco.entidades.Tienda;
import mx.profeco.mensajeria.EventoProfeco;
import mx.profeco.mensajeria.EventoPublisher;

@Path("/ofertas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OfertaResource {
    @Inject
    EventoPublisher publisher;

    @GET
    public List<Oferta> vigentes() {
        List<Oferta> todas = Oferta.listAll();
        List<Oferta> vigentes = new ArrayList<>();

        for (Oferta oferta : todas) {
            if (oferta.estaVigente()) {
                vigentes.add(oferta);
            }
        }
        return vigentes;
    }

    @POST
    @Transactional
    public Response publicar(OfertaRequest datos) {
        if (datos == null || datos.tiendaId == null || datos.productoId == null) {
            return error(Response.Status.BAD_REQUEST, "Envía tiendaId y productoId");
        }
        if (datos.descripcion == null || datos.descripcion.isBlank()) {
            return error(Response.Status.BAD_REQUEST, "Envía una descripción");
        }
        if (datos.precioOferta == null || datos.precioOferta <= 0) {
            return error(Response.Status.BAD_REQUEST, "El precio de oferta debe ser mayor a 0");
        }
        if (datos.fechaInicio == null || datos.fechaFin == null) {
            return error(Response.Status.BAD_REQUEST, "Envía fechaInicio y fechaFin (AAAA-MM-DD)");
        }
        if (datos.fechaFin.isBefore(datos.fechaInicio)) {
            return error(Response.Status.BAD_REQUEST, "La fechaFin no puede ser antes de fechaInicio");
        }

        Tienda tienda = Tienda.findById(datos.tiendaId);
        if (tienda == null) {
            return error(Response.Status.NOT_FOUND, "No existe la tienda " + datos.tiendaId);
        }
        Producto producto = Producto.findById(datos.productoId);
        if (producto == null) {
            return error(Response.Status.NOT_FOUND, "No existe el producto " + datos.productoId);
        }

        Oferta oferta = new Oferta(tienda, producto, datos.descripcion, datos.precioOferta,
                datos.fechaInicio, datos.fechaFin);
        oferta.persist();

        String mensaje = "Nueva oferta en " + tienda.getNombre() + ": "
                + producto.getNombre() + " a $" + datos.precioOferta;
        EventoProfeco evento = new EventoProfeco(EventoProfeco.OFERTA_PUBLICADA, mensaje,
                tienda.getId(), producto.getId());
        publisher.publicar(evento);

        return Response.status(Response.Status.CREATED).entity(oferta).build();
    }

    private Response error(Response.Status status, String mensaje) {
        return Response.status(status).entity(Map.of("mensaje", mensaje)).build();
    }
}
