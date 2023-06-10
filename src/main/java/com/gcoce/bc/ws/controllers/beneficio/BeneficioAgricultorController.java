package com.gcoce.bc.ws.controllers.beneficio;

import com.gcoce.bc.ws.dto.beneficio.CuentaDto;
import com.gcoce.bc.ws.dto.beneficio.ParcialidadDto;
import com.gcoce.bc.ws.dto.beneficio.SolicitudDto;
import com.gcoce.bc.ws.projections.beneficio.SolicitudesProjection;
import com.gcoce.bc.ws.services.beneficio.CuentaSvc;
import com.gcoce.bc.ws.services.beneficio.ParcialidadSvc;
import com.gcoce.bc.ws.services.beneficio.SolicitudSvc;
import com.gcoce.bc.ws.utils.Constants;
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
    private CuentaSvc cuentaSvc;

    @Autowired
    private ParcialidadSvc parcialidadSvc;

    @PostMapping("/solicitud/crear-solicitud")
    public ResponseEntity<?> createSolicitud(@Valid @RequestBody SolicitudDto solicitudDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return solicitudSvc.createSolicitudSvc((solicitudDto), token);
    }

    @GetMapping("/solicitud/verifica-existe-solicitudes/{usuario}")
    @ResponseBody
    public Boolean verificaSolicitudes(@PathVariable String usuario) {
        return solicitudSvc.verificaSolicitudesSvc(usuario);
    }

    @GetMapping("/solicitud/obtener-solicitudes/{usuario}")
    @ResponseBody
    public List<SolicitudesProjection> obtenerSolicitudes(@PathVariable String usuario) {
        return solicitudSvc.obtenerSolicitudesSvc(usuario);
    }

    @PostMapping("/cuenta/crear-cuenta")
    public ResponseEntity<?> crearCuenta(@Valid @RequestBody CuentaDto cuentaDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return cuentaSvc.crearCuentaSvc(cuentaDto, token);
    }

    @PostMapping("/parcialidad/crear-parcialidad")
    public ResponseEntity<?> createParcialidad(@Valid @RequestBody ParcialidadDto parcialidadDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token) {
        return parcialidadSvc.createParcialidadSvc(parcialidadDto, token);
    }
}
