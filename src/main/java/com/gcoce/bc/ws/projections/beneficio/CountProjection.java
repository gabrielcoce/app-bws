package com.gcoce.bc.ws.projections.beneficio;

import java.util.Date;
import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 14/06/2023
 */
public interface CountProjection {
    String getNoCuenta();

    String getEstadoCuenta();

    UUID getParcialidadId();

    Double getPesoIngresado();

    Double getPesoVerificado();

    Date getUpdatedAt();

}
