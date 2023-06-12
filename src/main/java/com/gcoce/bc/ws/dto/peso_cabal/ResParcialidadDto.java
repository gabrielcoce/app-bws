package com.gcoce.bc.ws.dto.peso_cabal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResParcialidadDto {

    private UUID parcialidadId;

    private Double pesoIngresado;

    private Boolean parcialidadVerificada;

}
