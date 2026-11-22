package mx.profeco.entidades;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


/**
 * Establecimiento deonde se venden productos.
 */

@Entity
@Table(name= "tiendas")
public class Tienda extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 200)
    private String direccion;

    @Column(length = 100)
    private String cadena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEstablecimiento tipoEstablecimiento;

    public Tienda(){}

    // Constructor para crear tienda desde el código (sin id)
    public Tienda(String nombre, String direccion, String cadena, TipoEstablecimiento tipoEstablecimiento) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.cadena = cadena;
    }

    public Long getId() {return id;}
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getCadena() {
        return cadena;
    }
    public void setCadena(String cadena) {
        this.cadena = cadena;
    }
    public TipoEstablecimiento getTipoEstablecimiento() {return tipoEstablecimiento;}
    public void setTipoEstablecimiento(TipoEstablecimiento tipoEstablecimiento) {
        this.tipoEstablecimiento = tipoEstablecimiento;
    }
}