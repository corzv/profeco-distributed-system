package mx.profeco.entidades;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Producto que se vende en LAS TIENDAS
 */

@Entity
@Table(name= "productos")
public class Producto extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String marca;

    @Column(length = 100)
    private String categoria;

    @Column(length = 50)
    private String presentacion;

    @Column(unique = true, length = 20)
    private String codigoBarras;

    public Producto(){}

    public Producto(String nombre, String marca, String categoria, String presentacion, String codigoBarras) {
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.presentacion = presentacion;
        this.codigoBarras = codigoBarras;
    }

    /*
    Precio mas bajo del producto entre todas las tiendas. ya que se guarda el historial de cada tienda solo cuenta su precio mas reciente.
    Devuelve null si el producto no tiene precio todavia
     */
    public Double obtenerPrecioMinimo() {
        List<Precio> precios = Precio.list("producto", Sort.descending("fechaRegistro"), this);

        List<Long> tiendasRevisadas = new ArrayList<>();
        Double minimo = null;

        for (Precio precio : precios) {
            Long idTienda = precio.getTienda().getId();

            if (!tiendasRevisadas.contains(idTienda)) {
                tiendasRevisadas.add(idTienda);

                if (minimo == null) {
                    minimo = precio.getMonto();
                } else if (precio.getMonto() < minimo) {
                    minimo = precio.getMonto();
                }
            }
        }
        return minimo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
}