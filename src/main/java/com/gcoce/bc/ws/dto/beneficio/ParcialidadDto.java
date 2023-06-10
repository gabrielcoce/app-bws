package com.gcoce.bc.ws.dto.beneficio;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Gabriel Coc Estrada
 * @since 28/05/2023
 */
@Getter
@Setter
@ToString
public class ParcialidadDto {
    @NotBlank
    @NotNull
    private String noCuenta;

    @Min(1)
    @NotNull
    private Integer pesoIngresado;

    @NotBlank
    @NotNull
    private String licenciaPiloto;

    @NotBlank
    @NotNull
    private String placaTransporte;

    /*@NotBlank
    @NotNull
    private String user;*/
}
