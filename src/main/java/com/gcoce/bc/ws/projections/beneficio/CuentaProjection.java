package com.gcoce.bc.ws.projections.beneficio;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
public interface CuentaProjection {
    //public String getNoCuenta();

    public Integer getEstadoCuenta();

    public Integer getPesoTotal();

    public Integer getCantidadParcialidades();
}
