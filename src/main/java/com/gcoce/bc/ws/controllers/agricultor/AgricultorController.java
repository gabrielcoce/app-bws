package com.gcoce.bc.ws.controllers.agricultor;

import com.gcoce.bc.ws.dto.agricultor.PilotoDto;
import com.gcoce.bc.ws.dto.agricultor.TransporteDto;
import com.gcoce.bc.ws.projections.agricultor.PilotoProjection;
import com.gcoce.bc.ws.projections.agricultor.TransporteProjection;
import com.gcoce.bc.ws.services.agricultor.PilotoSvc;
import com.gcoce.bc.ws.services.agricultor.TransporteSvc;
import com.gcoce.bc.ws.utils.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
@Tag(name = "agricultor-controller", description = "Agricultor Services")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/agricultor")
public class AgricultorController {
    @Autowired
    private PilotoSvc pilotoSvc;

    @Autowired
    private TransporteSvc transporteSvc;

    @PostMapping("/crear-piloto")
    public ResponseEntity<?> crearPiloto(@Valid @RequestBody PilotoDto pilotoDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token){
        return pilotoSvc.crearPilotoSvc(pilotoDto, token);
    }

    @PostMapping("/crear-transporte")
    public ResponseEntity<?> crearTransporte(@Valid @RequestBody TransporteDto transporteDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token){
        return transporteSvc.crearTransporteSvc(transporteDto, token);
    }
    @GetMapping("/obtener-pilotos")
    public List<PilotoProjection> obtenerPilotos(){
        return pilotoSvc.obtenerPilotoSvc();
    }

    @GetMapping("/obtener-transportes")
    public List<TransporteProjection> obtenerTransportes(){
        return transporteSvc.obtenerTransporteSvc();
    }
}
