package com.gcoce.bc.ws.controllers.beneficio;

import com.gcoce.bc.ws.dto.beneficio.ParcialidadDto;
import com.gcoce.bc.ws.dto.beneficio.SolicitudDto;
import com.gcoce.bc.ws.projections.beneficio.AllCuentaProjection;
import com.gcoce.bc.ws.projections.beneficio.AllParcialidadProjection;
import com.gcoce.bc.ws.projections.beneficio.SolicitudesProjection;
import com.gcoce.bc.ws.services.beneficio.CuentaSvc;
import com.gcoce.bc.ws.services.beneficio.ParcialidadSvc;
import com.gcoce.bc.ws.services.beneficio.SolicitudSvc;
import com.gcoce.bc.ws.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gabriel Coc Estrada
 * @since 9/06/2023
 */
@Tag(name = "beneficio-agricultor-controller", description = "Beneficio Agricultor Services")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/beneficio-agricultor")
public class BeneficioAgricultorController {
    @Autowired
    private SolicitudSvc solicitudSvc;

    @Autowired
    private ParcialidadSvc parcialidadSvc;

    @Autowired
    private CuentaSvc cuentaSvc;

    @PostMapping("/solicitud/crear-solicitud")
    public ResponseEntity<?> createSolicitud(@Valid @RequestBody SolicitudDto solicitudDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return solicitudSvc.createSolicitudSvc((solicitudDto), token);
    }

    @GetMapping("/solicitud/verifica-existe-solicitudes/{usuario}")
    public Boolean verificaSolicitudes(@PathVariable String usuario) {
        return solicitudSvc.verificaSolicitudesSvc(usuario);
    }

    @GetMapping("/solicitud/obtener-solicitudes/{usuario}")
    public List<SolicitudesProjection> obtenerSolicitudes(@PathVariable String usuario) {
        return solicitudSvc.obtenerSolicitudesSvc(usuario);
    }

    @PostMapping("/parcialidad/crear-parcialidad")
    public ResponseEntity<?> createParcialidad(@Valid @RequestBody ParcialidadDto parcialidadDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return parcialidadSvc.createParcialidadSvc(parcialidadDto, token);
    }

    @Operation(summary = "Obtener parcialidades por usuario", description = "Método para obtener parcialidades por Usuario")
    @GetMapping("/parcialidad/obtener-parcialidades/{user}")
    public List<AllParcialidadProjection> getAllParcialidades(@PathVariable String user) {
        return parcialidadSvc.allParcialidadesUserSvc(user);
    }
    @Operation(summary = "Obtener cuentas de un usuario", description = "Método para obtener cuenta")
    @GetMapping("/cuenta/obtener-cuentas/{user}")
    public List<AllCuentaProjection> getCuentaBeneficio(@PathVariable String user){
        return cuentaSvc.obtenerCuentasByUserSvc(user);
    }

    @Operation(summary = "Verifica que exista cuentas para usuarios", description = "Método para obtener cuenta")
    @GetMapping("/cuenta/verifica-existe-cuentas/{user}")
    public Boolean getExisteCuenta(@PathVariable String user){
        return cuentaSvc.existeCuentaByUser(user);
    }

    @Operation(summary = "Verifica que exista parcialidades para el usuario", description = "Método para obtener parcialidades")
    @GetMapping("/parcialidad/verifica-existe-parcialidades/{user}")
    public Boolean getExisteParcialidad(@PathVariable String user){
        return parcialidadSvc.existeParcialidadUserSvc(user);
    }
}
