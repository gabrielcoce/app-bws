package com.gcoce.bc.ws.entities.beneficio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gcoce.bc.ws.utils.Constants;
import com.gcoce.bc.ws.utils.Fechas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Gabriel Coc Estrada
 * @since 22/05/2023
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "cuenta", schema = "beneficio_ws")
public class Cuenta {
    @Id
    @Column(name = "no_cuenta")
    private String noCuenta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "no_solicitud")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Solicitud solicitud;

    @Column(name = "estado_cuenta")
    private Integer estadoCuenta;

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

    public static Cuenta createdAccFromDto(String user, Solicitud solicitud) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNoCuenta(Constants.generateAccount());
        cuenta.setSolicitud(solicitud);
        cuenta.setEstadoCuenta(Constants.CUENTA_CREADA);
        cuenta.setUserCreated(user);
        cuenta.setCreatedAt(Fechas.setTimeZoneDateGT(new Date()));
        return cuenta;
    }
}
