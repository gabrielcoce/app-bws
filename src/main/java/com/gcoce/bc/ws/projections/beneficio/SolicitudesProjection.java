package com.gcoce.bc.ws.projections.beneficio;

/**
 * @author Gabriel Coc Estrada
 * @since 10/06/2023
 */
public interface SolicitudesProjection {
    public String getNoSolicitud();

    public String getTipoSolicitud();

    public String getEstadoSolicitud();

    public Integer getPesoTotal();

    public Integer getCantidadParcialidades();
}
