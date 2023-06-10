package com.gcoce.bc.ws.projections.beneficio;

/**
 * @author Gabriel Coc Estrada
 * @since 20/05/2023
 */
public interface SolicitudProjection {
    public String getNoSolicitud();
    public String getUsuarioSolicita();

    public Integer getPesoTotal();

    public Integer getCantidadParcialidades();

    //public Date
}
