package com.gcoce.bc.ws.projections.beneficio;

/**
 * @author Gabriel Coc Estrada
 * @since 14/06/2023
 */
public interface AprobarSolicitudesProjection {
     String getNoSolicitud();

     String getTipoSolicitud();

     String getEstadoSolicitud();

     Integer getPesoTotal();

     Integer getCantidadParcialidades();

    String getUsuarioSolicita();
}
