package mx.profeco.entidades;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "precios")
public class Precio extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "tienda_id", nullable = false)
    private Tienda tienda;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public Precio() {
    }

    public Precio(Tienda tienda, Producto producto, Double monto) {
        this.tienda = tienda;
        this.producto = producto;
        this.monto = monto;
        this.fechaRegistro = LocalDateTime.now();
    }

    public void actualizarMonto(Double nuevoMonto) {
        Precio nuevo = new Precio(this.tienda, this.producto, nuevoMonto);
        nuevo.persist();
    }

    public Long getId() {
        return id;
    }

    public Double getMonto() {
        return monto;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public Producto getProducto() {
        return producto;
    }
}
