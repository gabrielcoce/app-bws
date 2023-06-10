package com.gcoce.bc.ws.dto.beneficio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
@Getter
@Setter
@ToString
public class TransporteBcDto {
    @NotBlank
    @NotNull
    @Pattern(regexp = "^(C)([0-9]{3})([A-Z]{3})$", message = "Ingrese una placa válida")
    private String placaTransporte;

    @NotBlank
    @NotNull
    private String marca;

    @NotBlank
    @NotNull
    private String color;
}
