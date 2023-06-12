package com.gcoce.bc.ws.projections.beneficio;

import java.util.Date;

/**
 * @author Gabriel Coc Estrada
 * @since 12/06/2023
 */
public interface AllCuentaProjection {
    String getNoCuenta();

    Double getPeso();

    Integer getParcialidades();

    String getEstado();

    Date getCreatedAt();
}
