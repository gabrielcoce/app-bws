package com.gcoce.bc.ws.dto.beneficio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Gabriel Coc Estrada
 * @since 06/06/2023
 */
@Getter
@Setter
@ToString
public class ActualizarCuentaDto {
    @NotBlank
    @NotNull
    private String noCuenta;

    /*@NotBlank
    @NotNull
    private String userUpdated;*/

    @NotNull
    private Integer nuevoEstado;
}
