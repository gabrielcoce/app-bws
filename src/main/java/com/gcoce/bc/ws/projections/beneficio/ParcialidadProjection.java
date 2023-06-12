package com.gcoce.bc.ws.projections.beneficio;

import com.gcoce.bc.ws.dto.peso_cabal.ResParcialidadDto;

import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
public interface ParcialidadProjection {
    UUID getParcialidadId();

    Double getPesoIngresado();

    Boolean getParcialidadVerificada();

}
