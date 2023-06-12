package com.gcoce.bc.ws.projections.beneficio;

import java.util.Date;

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

    public Date getCreatedAt();
}
