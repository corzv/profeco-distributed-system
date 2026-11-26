package mx.profeco.recursos;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import mx.profeco.dto.PrecioRequest;
import mx.profeco.entidades.Precio;
import mx.profeco.entidades.Producto;
import mx.profeco.entidades.Tienda;

@Path("/precios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PrecioResource {
    @POST
    @Transactional
    public Response registrar(PrecioRequest datos) {
        if (datos == null || datos.tiendaId == null || datos.productoId == null) {
            return error(Response.Status.BAD_REQUEST, "Envía tiendaId y productoId");
        }
        if (datos.monto == null || datos.monto <= 0) {
            return error(Response.Status.BAD_REQUEST, "El monto debe ser mayor a 0");
        }

        Tienda tienda = Tienda.findById(datos.tiendaId);
        if (tienda == null) {
            return error(Response.Status.NOT_FOUND, "No existe la tienda " + datos.tiendaId);
        }
        Producto producto = Producto.findById(datos.productoId);
        if (producto == null) {
            return error(Response.Status.NOT_FOUND, "No existe el producto " + datos.productoId);
        }

        Precio precio = new Precio(tienda, producto, datos.monto);
        precio.persist();
        return Response.status(Response.Status.CREATED).entity(precio).build();
    }

    private Response error(Response.Status status, String mensaje) {
        return Response.status(status).entity(Map.of("mensaje", mensaje)).build();
    }
}
