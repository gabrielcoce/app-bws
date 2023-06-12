package com.gcoce.bc.ws.entities.beneficio;

import com.gcoce.bc.ws.dto.beneficio.TransporteBcDto;
import com.gcoce.bc.ws.utils.Fechas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "transporte", uniqueConstraints = {@UniqueConstraint(columnNames = "placa_transporte")}, schema = "beneficio_ws")
public class TransporteBc {

    @Id
    @Column(name = "placa_transporte")
    private String placaTransporte;

    @Column(name = "marca")
    private String marca;

    @Column(name = "color")
    private String color;

    /*@Column(name = "owner")
    private String owner;*/

    @Column(name = "status")
    private Boolean status;

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

    public static TransporteBc createTransporte(TransporteBcDto transporteBcDto, String user){
        TransporteBc transporteBc = new TransporteBc();
        transporteBc.setPlacaTransporte(transporteBcDto.getPlacaTransporte());
        transporteBc.setMarca(transporteBcDto.getMarca());
        transporteBc.setColor(transporteBcDto.getColor());
        transporteBc.setStatus(true);
        transporteBc.setUserCreated(user);
        transporteBc.setCreatedAt(Fechas.setTimeZoneDateGT(new Date()));
        return transporteBc;
    }
}
