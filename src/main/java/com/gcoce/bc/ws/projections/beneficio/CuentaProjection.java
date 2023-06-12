package com.gcoce.bc.ws.projections.beneficio;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
public interface CuentaProjection {
    Integer getEstadoCuenta();

    Integer getPesoTotal();

    Integer getCantidadParcialidades();
}
