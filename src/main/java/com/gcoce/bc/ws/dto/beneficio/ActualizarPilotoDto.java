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
public class ActualizarPilotoDto {
    @NotBlank
    @NotNull
    private String licenciaPiloto;

    @NotNull
    private Boolean status;
}
