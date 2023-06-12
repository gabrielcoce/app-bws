package com.gcoce.bc.ws.projections.peso_cabal;

import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
public interface ParcialidadPcProjection {
    UUID getParcialidadId();

    Double getPesoIngresado();

    Double getPesoRegistrado();

    Double getPesoExcedido();

    Double getPesoFaltante();
}
