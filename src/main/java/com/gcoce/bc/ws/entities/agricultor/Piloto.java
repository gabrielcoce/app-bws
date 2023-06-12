package com.gcoce.bc.ws.entities.agricultor;

import com.gcoce.bc.ws.utils.Fechas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "piloto", uniqueConstraints = {@UniqueConstraint(columnNames = "licencia_piloto")}, schema = "agricultor_db")
public class Piloto {
    @Id
    @Column(name = "licencia_piloto")
    private String licenciaPiloto;

    @Column(name = "nombre")
    private String nombre;

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

    public static Piloto createPilot(String licenciaPiloto, String nombre, String user) {
        Piloto piloto = new Piloto();
        piloto.setLicenciaPiloto(licenciaPiloto);
        piloto.setNombre(nombre);
        piloto.setUserCreated(user);
        piloto.setCreatedAt(Fechas.setTimeZoneDateGT(new Date()));
        return piloto;
    }
}
