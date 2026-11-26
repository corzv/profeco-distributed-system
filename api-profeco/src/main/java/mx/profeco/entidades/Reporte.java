package mx.profeco.entidades;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
public class Reporte extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MotivoReporte motivo;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReporte estado;

    @Column(nullable = false)
    private Long consumidorId;

    @Column(nullable = false)
    private Long tiendaId;

    private Long productoId;

    private Long agenteId;

    public Reporte() {
    }

    public Reporte(MotivoReporte motivo, String descripcion, Long consumidorId, Long tiendaId, Long productoId) {
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.consumidorId = consumidorId;
        this.tiendaId = tiendaId;
        this.productoId = productoId;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoReporte.PENDIENTE;
    }

    public Long getId() { return id; }

    public MotivoReporte getMotivo() { return motivo; }
    public void setMotivo(MotivoReporte motivo) { this.motivo = motivo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }

    public EstadoReporte getEstado() { return estado; }
    public void setEstado(EstadoReporte estado) { this.estado = estado; }

    public Long getConsumidorId() { return consumidorId; }
    public void setConsumidorId(Long consumidorId) { this.consumidorId = consumidorId; }

    public Long getTiendaId() { return tiendaId; }
    public void setTiendaId(Long tiendaId) { this.tiendaId = tiendaId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Long getAgenteId() { return agenteId; }
    public void setAgenteId(Long agenteId) { this.agenteId = agenteId; }
}
