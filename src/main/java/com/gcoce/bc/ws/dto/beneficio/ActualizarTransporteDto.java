package com.gcoce.bc.ws.dto.beneficio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
@Getter
@Setter
@ToString
public class ActualizarTransporteDto {
    @NotBlank
    @NotNull
    private String placaTransporte;

    @NotNull
    private Boolean status;
}
