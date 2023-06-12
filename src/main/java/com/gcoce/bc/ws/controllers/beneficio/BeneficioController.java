package com.gcoce.bc.ws.controllers.beneficio;

import com.gcoce.bc.ws.dto.beneficio.*;
import com.gcoce.bc.ws.payload.request.LoginRequest;
import com.gcoce.bc.ws.payload.request.SignupRequest;
import com.gcoce.bc.ws.projections.beneficio.AllCuentaProjection;
import com.gcoce.bc.ws.projections.beneficio.AllParcialidadProjection;
import com.gcoce.bc.ws.services.beneficio.*;
import com.gcoce.bc.ws.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "beneficio-controller", description = "Beneficio Services")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/beneficio")
public class BeneficioController {
    @Autowired
    private AuthSvc authSvc;

    @Autowired
    private SolicitudSvc solicitudSvc;

    @Autowired
    private CuentaSvc cuentaSvc;

    @Autowired
    private ParcialidadSvc parcialidadSvc;

    /*@Autowired
    private PilotoBcSvc;

    @Autowired
    private TransporteBcSvc;*/

    @Operation(summary = "Beneficio Auth", description = "Método para ingresar")
    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authSvc.authenticateUserSvc(loginRequest);
    }

    @Operation(summary = "Beneficio Auth", description = "Método para registrar usuario")
    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authSvc.registerUserSvc(signUpRequest);
    }

    @Operation(summary = "Beneficio Auth", description = "Método para obtener token de Hcaptcha")
    @GetMapping("/auth/hc/{hCaptchaResponse}")
    public ResponseEntity<?> getTokenHcaptcha(@PathVariable String hCaptchaResponse) {
        return authSvc.verifyHCaptcha(hCaptchaResponse);
    }

    @Operation(summary = "Beneficio Cuenta", description = "Método para crear cuenta")
    @PostMapping("/cuenta/crear-cuenta")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> crearCuenta(@Valid @RequestBody CuentaDto cuentaDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return cuentaSvc.crearCuentaSvc(cuentaDto, token);
    }

    @Operation(summary = "Beneficio Cuenta", description = "Método para actualizar estado cuenta")
    @PutMapping("/cuenta/permitir-ingreso/{noCuenta}")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> permitirIngreso(@PathVariable String noCuenta, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return cuentaSvc.permitirIngresoSvc(noCuenta, token);
    }

    /*@Operation(summary = "Beneficio Piloto", description = "Método para crear Piloto")
    @PostMapping("/piloto/crear-piloto")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> crearPiloto(@Valid @RequestBody PilotoBcDto dto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return pilotoBcSvc.createPilotoSvc(dto, token);
    }

    @Operation(summary = "Beneficio Piloto", description = "Método para actualizar estado Piloto")
    @PutMapping("/piloto/actualizar-estado-piloto")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> actualizarEstadoPiloto(@Valid @RequestBody ActualizarPilotoDto dto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return pilotoBcSvc.updateEstadoPilotoSvc(dto, token);
    }*/

    /*@Operation(summary = "Beneficio Transporte", description = "Método para crear Transporte")
    @PostMapping("/transporte/crear-transporte")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> crearTransporte(@Valid @RequestBody TransporteBcDto dto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return transporteBcSvc.createTransporteSvc(dto, token);
    }

    @Operation(summary = "Beneficio Transporte", description = "Método para actualizar estado Transporte")
    @PutMapping("/transporte/actualizar-estado-transporte")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> actualizarEstadoTransporte(@Valid @RequestBody ActualizarTransporteDto dto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return transporteBcSvc.updateEstadoTransporteSvc(dto, token);
    }*/

    @Operation(summary = "Rechazar Cuenta", description = "Método para actualizar estado solicitud")
    @PutMapping("/solicitud/rechazar-solicitud")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> rechazarSolicitud(@Valid @RequestBody ActualizarSolicitudDto solicitudDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return solicitudSvc.rechazarSolicitudSvc(solicitudDto.getNoSolicitud(), token);
    }

    @Operation(summary = "Cerrar Cuenta", description = "Método cambia el estado de la Cuenta a Cerrada en Beneficio")
    @PutMapping("/cuenta/cerrar-cuenta/{noCuenta}")
    public ResponseEntity<?> cerrarCuenta(@PathVariable String noCuenta, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return cuentaSvc.cerrarCuentaSvc(noCuenta, token);
    }

    @Operation(summary = "Confirmar Cuenta", description = "Método cambia el estado de la Cuenta a Confirmada en Beneficio")
    @PutMapping("/cuenta/confirmar-cuenta/{noCuenta}")
    public ResponseEntity<?> confirmarCuenta(@PathVariable String noCuenta, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return cuentaSvc.confirmarCuentaSvc(noCuenta, token);
    }

    @Operation(summary = "Obtener Parcialidades", description = "Método para obtener token de Hcaptcha")
    @GetMapping("/parcialidad/obtener-parcialidades/{noCuenta}")
    public List<AllParcialidadProjection> getAllParcialidades(@PathVariable String noCuenta) {
        return parcialidadSvc.allParcialidadesSvc(noCuenta);
    }

    @Operation(summary = "Obtener cuenta", description = "Método para obtener cuenta")
    @GetMapping("/cuenta/obtener-cuenta/{noCuenta}")
    public AllCuentaProjection getCuentaBeneficio(@PathVariable String noCuenta) {
        return cuentaSvc.obtenerCuentaByBcSvc(noCuenta);
    }
}
