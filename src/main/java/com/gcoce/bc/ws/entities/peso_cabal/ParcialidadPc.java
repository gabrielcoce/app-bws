package com.gcoce.bc.ws.entities.peso_cabal;

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
 * @since 30/05/2023
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "parcialidad", uniqueConstraints = {@UniqueConstraint(columnNames = "parcialidad_id")}, schema = "peso_cabal_db")
public class ParcialidadPc {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "parcialidad_pc_id")
    private UUID parcialidadPcId;

    @Column(name = "parcialidad_id")
    private UUID parcialidadId;

    @Column(name = "peso_ingresado")
    private Double pesoIngresado;

    @Column(name = "peso_registrado")
    private Double pesoRegistrado;

    @Column(name = "peso_excedido")
    private Double pesoExcedido;

    @Column(name = "peso_faltante")
    private Double pesoFaltante;

    @Column(name = "no_cuenta")
    private String noCuenta;

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
}
