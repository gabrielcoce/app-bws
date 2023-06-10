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
public class PilotoBcDto {
    @NotBlank
    @NotNull
    @Pattern(regexp = "^[0-9]{4}\\s?[0-9]{5}\\s?[0-9]{4}$", message = "Ingrese una licencia válida")
    private String licenciaPiloto;

    @NotBlank
    @NotNull
    private String nombre;
}
