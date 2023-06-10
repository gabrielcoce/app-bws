package com.gcoce.bc.ws.entities.beneficio;

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
@Table(name = "piloto", uniqueConstraints = {@UniqueConstraint(columnNames = "licencia_piloto")}, schema = "beneficio_ws")
public class PilotoBc {
    @Id
    @Column(name = "licencia_piloto")
    private String licenciaPiloto;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "estado_piloto")
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

    public static PilotoBc createPiloto(String licenciaPiloto, String nombre, String user){
        PilotoBc pilotoBc = new PilotoBc();
        pilotoBc.setLicenciaPiloto(licenciaPiloto);
        pilotoBc.setNombre(nombre);
        pilotoBc.setStatus(false);
        pilotoBc.setUserCreated(user);
        pilotoBc.setCreatedAt(Fechas.setTimeZoneDateGT(new Date()));
        return pilotoBc;
    }
}
