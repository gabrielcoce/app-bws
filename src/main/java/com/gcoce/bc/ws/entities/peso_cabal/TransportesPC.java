package com.gcoce.bc.ws.entities.peso_cabal;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "transportes", uniqueConstraints = {@UniqueConstraint(columnNames = "placa_transporte")}, schema = "peso_cabal_db")
public class TransportesPC {
    @Id
    @Column(name = "placa_transporte")
    private String placaTransporte;

    @Column(name = "marca")
    private String marca;

    @Column(name = "color")
    private String color;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
