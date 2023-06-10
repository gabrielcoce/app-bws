package com.gcoce.bc.ws.dto.beneficio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class VerificarParcialidadDto {
    @NotNull
    private UUID parcialidadId;

    /*@NotBlank
    @NotNull
    private String userUpdated;*/
}
