package com.gcoce.bc.ws.entities.beneficio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Gabriel Coc Estrada
 * @since 28/05/2023
 */

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "catalogo", schema = "beneficio_ws")
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_catalogo")
    private Integer codigoCatalogo;

    @Column(name = "nombre_catalogo")
    private String nombre;

    @Column(name = "descripcion_catalogo")
    private String descripcion;

    @Column(name = "estado_catalogo")
    private boolean estadoCatalogo;

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
