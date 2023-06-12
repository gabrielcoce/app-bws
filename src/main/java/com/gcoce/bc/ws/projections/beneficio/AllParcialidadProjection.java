package com.gcoce.bc.ws.projections.beneficio;

import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
public interface AllParcialidadProjection {
    UUID getParcialidadId();

    String getLicenciaPiloto();

    String getPlacaTransporte();

    Double getPesoIngresado();
}
