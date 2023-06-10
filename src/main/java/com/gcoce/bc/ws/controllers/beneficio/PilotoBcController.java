package com.gcoce.bc.ws.controllers.beneficio;

import com.gcoce.bc.ws.dto.beneficio.PilotoBcDto;
import com.gcoce.bc.ws.services.beneficio.PilotoBcSvc;
import com.gcoce.bc.ws.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/beneficio/piloto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class PilotoBcController {
    private PilotoBcSvc pilotoBcSvc;
    @PostMapping("/crear-piloto")
    public ResponseEntity<?> crearPiloto(PilotoBcDto pilotoBcDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token){
        return pilotoBcSvc.createPilotoSvc(pilotoBcDto, token);
    }
}
