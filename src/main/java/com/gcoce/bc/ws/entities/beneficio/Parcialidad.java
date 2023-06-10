package com.gcoce.bc.ws.entities.beneficio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gcoce.bc.ws.dto.beneficio.ParcialidadDto;
import com.gcoce.bc.ws.utils.Fechas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 28/05/2023
 */
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "parcialidad", schema = "beneficio_ws")
public class Parcialidad {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "parcialidad_id")
    private UUID parcialidadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "no_cuenta")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Cuenta cuenta;

    @Column(name = "peso_ingresado")
    private Double pesoIngresado;

    @Column(name = "placa_transporte")
    private String placaTransporte;

    @Column(name = "licencia_piloto")
    private String licenciaPiloto;

    @Column(name = "parcialidad_verificada")
    private Boolean verified;

    @Column(name = "user_created")
    private String userCreated;

    @Column(name = "user_updated")
    private String userUpdated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public static Parcialidad createdParcialidad(ParcialidadDto parcialidadDto, Cuenta cuenta, String user) {
        Parcialidad parcialidad = new Parcialidad();
        parcialidad.setCuenta(cuenta);
        parcialidad.setPesoIngresado(Double.valueOf(parcialidadDto.getPesoIngresado()));
        parcialidad.setPlacaTransporte(parcialidadDto.getPlacaTransporte());
        parcialidad.setLicenciaPiloto(parcialidadDto.getLicenciaPiloto());
        parcialidad.setVerified(false);
        parcialidad.setUserCreated(user);
        parcialidad.setCreatedAt(Fechas.setTimeZoneDateGT(new Date()));
        return parcialidad;
    }
}
