package com.gcoce.bc.ws.dto.beneficio;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Gabriel Coc Estrada
 * @since 18/05/2023
 */
@Getter
@Setter
@ToString
public class SolicitudDto {
    @Min(10)
    @NotNull
    private Integer tipoSolicitud;

    @NotBlank
    @NotNull
    private String usuarioSolicita;

    @Min(1)
    @NotNull
    private Integer pesoTotal;

    @Min(1)
    @NotNull
    private Integer cantidadParcialidades;

}
