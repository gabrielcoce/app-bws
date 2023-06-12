package com.gcoce.bc.ws.dto.peso_cabal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
@Getter
@Setter
@ToString
public class ParcialidadPcDto {
    @NotBlank
    @NotNull
    private String noCuenta;

    @NotNull
    private UUID parcialidadId;

    @NotNull
    private Double pesoRegistrado;
}
