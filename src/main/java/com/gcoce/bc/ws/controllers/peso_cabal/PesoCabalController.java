package com.gcoce.bc.ws.controllers.peso_cabal;

import com.gcoce.bc.ws.dto.peso_cabal.ParcialidadPcDto;
import com.gcoce.bc.ws.dto.peso_cabal.ResParcialidadDto;
import com.gcoce.bc.ws.projections.peso_cabal.ParcialidadPcProjection;
import com.gcoce.bc.ws.services.peso_cabal.ParcialidadPcSvc;
import com.gcoce.bc.ws.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 30/05/2023
 */
@Tag(name = "peso-cabal-controller", description = "Peso Cabal Services")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/peso-cabal")
public class PesoCabalController {
    @Autowired
    private ParcialidadPcSvc parcialidadPcSvc;

    @Operation(summary = "Verifica Existencia de Parcialidad", description = "Método para verificar si existe parcialidad en Peso Cabal")
    @GetMapping("/parcialidad/verifica-existe-parcialidad/{parcialidadId}")
    public Boolean existeParcialidad(@PathVariable UUID parcialidadId) {
        return parcialidadPcSvc.existeParcialidadSvc(parcialidadId);
    }

    @Operation(summary = "Verifica Existencia de Parcialidades por Cuenta", description = "Método para verificar si existe parcialidades por Cuenta en Peso Cabal")
    @GetMapping("/parcialidad/verifica-existencia-parcialidades/{noCuenta}")
    public Boolean existeParcialidadesByCuenta(@PathVariable String noCuenta) {
        return parcialidadPcSvc.existeParcialidadesByCuentaSvc(noCuenta);
    }

    @Operation(summary = "Registra Parcialidad", description = "Método para obtener las parcialidades de una cuenta")
    @PostMapping("/parcialidad/registrar-parcialidad")
    public ResponseEntity<?> registrarParcialidad(@Valid @RequestBody ParcialidadPcDto dto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return parcialidadPcSvc.registrarParcialidadSvc(dto, token);
    }

    @Operation(summary = "Obtiene Parcialidades", description = "Método para obtener las parcialidades de una cuenta")
    @GetMapping("/parcialidad/obtener-parcialidades/{noCuenta}")
    public List<ResParcialidadDto> obtenerParcialidades(@PathVariable String noCuenta) {
        return parcialidadPcSvc.obtenerParcialidadesSvc(noCuenta);
    }

    @Operation(summary = "Actualiza Parcialidad", description = "Método para actualizar parcialidad")
    @PutMapping("/parcialidad/actualizar-parcialidad")
    public ResponseEntity<?> actualizarParcialidad(@Valid @RequestBody ParcialidadPcDto dto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return parcialidadPcSvc.actualizarParcialidadSvc(dto, token);
    }
    @Operation(summary = "Verifica Parcialidad", description = "Método para verificar parcialidad en Beneficio")
    @PutMapping("/parcialidad/verificar-parcialidad/{parcialidadId}")
    public ResponseEntity<?> verificarParcialidad(@PathVariable UUID parcialidadId, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return parcialidadPcSvc.verificarParcialidadSvc(parcialidadId, token);
    }

    @Operation(summary = "Inicia Pesaje", description = "Método cambia el estado de la Cuenta a Pesaje Iniciado en Beneficio")
    @PutMapping("/cuenta/iniciar-pesaje/{noCuenta}")
    public ResponseEntity<?> iniciarPesaje(@PathVariable String noCuenta, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return parcialidadPcSvc.iniciarPesajeSvc(noCuenta, token);
    }

    @Operation(summary = "Finaliza Pesaje", description = "Método cambia el estado de la Cuenta a Pesaje Finalizado en Beneficio")
    @PutMapping("/cuenta/finalizar-pesaje/{noCuenta}")
    public ResponseEntity<?> finalizarPesaje(@PathVariable String noCuenta, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return parcialidadPcSvc.finalizarPesajeSvc(noCuenta, token);
    }

    @GetMapping("/parcialidad/obtener-parcialidades-registradas/{noCuenta}")
    public List<ParcialidadPcProjection>obtenerParcialidadesRegistradas(@PathVariable String noCuenta){
        return parcialidadPcSvc.obtenerParcialidadesRegistradasSvc(noCuenta);
    }
}
