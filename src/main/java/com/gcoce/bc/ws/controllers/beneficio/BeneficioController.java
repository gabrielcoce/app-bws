package com.gcoce.bc.ws.controllers.beneficio;

import com.gcoce.bc.ws.dto.beneficio.ActualizarCuentaDto;
import com.gcoce.bc.ws.dto.beneficio.ActualizarSolicitudDto;
import com.gcoce.bc.ws.dto.beneficio.VerificarParcialidadDto;
import com.gcoce.bc.ws.payload.request.LoginRequest;
import com.gcoce.bc.ws.payload.request.SignupRequest;
import com.gcoce.bc.ws.services.beneficio.AuthSvc;
import com.gcoce.bc.ws.services.beneficio.CuentaSvc;
import com.gcoce.bc.ws.services.beneficio.ParcialidadSvc;
import com.gcoce.bc.ws.services.beneficio.SolicitudSvc;
import com.gcoce.bc.ws.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Authentication User", description = "Return jwt token")
    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authSvc.authenticateUserSvc(loginRequest);
    }

    @Operation(summary = "Authentication User Creation", description = "Create user authentication in the system")
    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authSvc.registerUserSvc(signUpRequest);
    }

    @Operation(summary = "Authentication User Hc", description = "Verificar Hc Token")
    @GetMapping("/auth/hc/{hCaptchaResponse}")
    public ResponseEntity<?> getTokenHcaptcha(@PathVariable String hCaptchaResponse) {
        return authSvc.verifyHCaptcha(hCaptchaResponse);
    }

    @Operation(summary = "Beneficio Solicitud", description = "Método para actualizar estado solicitud")
    @PutMapping("/solicitud/actualizar-solicitud")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> actualizarSolicitud(@Valid @RequestBody ActualizarSolicitudDto solicitudDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return solicitudSvc.updateSolicitudSvc(solicitudDto, token);
    }

    @Operation(summary = "Beneficio Cuenta", description = "Método para actualizar estado cuenta")
    @PutMapping("/cuenta/actualizar-cuenta")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> actualizarCuenta(@Valid @RequestBody ActualizarCuentaDto cuentaDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return cuentaSvc.updateCuentaSvc(cuentaDto, token);
    }

    @Operation(summary = "Beneficio Parcialidad", description = "Método para verificar parcialidad")
    @PutMapping("/parcialidad/verificar-parcialidad")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> verificarParcialidad(@Valid @RequestBody VerificarParcialidadDto parcialidadDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return parcialidadSvc.verificarParcialidadSvc(parcialidadDto, token);
    }
}
