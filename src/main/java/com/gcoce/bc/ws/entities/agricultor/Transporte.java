package com.gcoce.bc.ws.entities.agricultor;

import com.gcoce.bc.ws.dto.agricultor.TransporteDto;
import com.gcoce.bc.ws.utils.Fechas;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "transporte", uniqueConstraints = {@UniqueConstraint(columnNames = "placa_transporte")}, schema = "agricultor_db")
public class Transporte {
    @Id
    @Column(name = "placa_transporte")
    private String placaTransporte;

    @Column(name = "marca")
    private String marca;

    @Column(name = "color")
    private String color;

    @Column(name = "user_created")
    private String userCreated;

    @Column(name = "user_updated")
    private String userUpdated;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public static Transporte createTransport(TransporteDto transporteDto, String user) {
        Transporte transporte = new Transporte();
        transporte.setPlacaTransporte(transporteDto.getPlacaTransporte());
        transporte.setMarca(transporte.getMarca());
        transporte.setColor(transporte.getColor());
        transporte.setUserCreated(user);
        transporte.setCreatedAt(Fechas.setTimeZoneDateGT(new Date()));
        return transporte;
    }

}
