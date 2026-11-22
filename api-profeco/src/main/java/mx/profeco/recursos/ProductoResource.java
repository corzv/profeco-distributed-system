package mx.profeco.recursos;

import io.quarkus.panache.common.Sort;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import mx.profeco.entidades.Precio;
import mx.profeco.entidades.Producto;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
public class ProductoResource {
    @GET
    public List<Producto> buscar(@QueryParam("nombre") String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return Producto.listAll(Sort.by("nombre"));
        }
        String patron = "%" + nombre.trim().toLowerCase() + "%";
        return Producto.list("lower(nombre) like ?1", Sort.by("nombre"), patron);
    }

    @GET
    @Path("/{id}")
    public Response obtener(@PathParam("id") Long id) {
        Producto producto = Producto.findById(id);
        if (producto == null) {
            return noEncontrado("No existe el producto " + id);
        }
        return Response.ok(producto).build();
    }

    @GET
    @Path("/{id}/precios")
    public Response compararPrecios(@PathParam("id") Long id) {
        Producto producto = Producto.findById(id);
        if (producto == null) {
            return noEncontrado("No existe el producto " + id);
        }

        List<Precio> precios = Precio.list(
                "from Precio p where p.producto.id = ?1 and p.fechaRegistro = "
                + "(select max(p2.fechaRegistro) from Precio p2 "
                + " where p2.producto = p.producto and p2.tienda = p.tienda) "
                + "order by p.monto", id);

        return Response.ok(precios).build();
    }

    private Response noEncontrado(String mensaje) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("mensaje", mensaje))
                .build();
    }
}
