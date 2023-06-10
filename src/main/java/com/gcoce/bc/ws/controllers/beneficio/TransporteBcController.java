package com.gcoce.bc.ws.controllers.beneficio;

import com.gcoce.bc.ws.dto.beneficio.TransporteBcDto;
import com.gcoce.bc.ws.services.beneficio.TransporteBcSvc;
import com.gcoce.bc.ws.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/beneficio/transporte", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class TransporteBcController {
    @Autowired
    private TransporteBcSvc transporteBcSvc;
    @PostMapping("/crear-transporte")
    public ResponseEntity<?> crearTransporte(TransporteBcDto transporteBcDto, @RequestHeader(value = Constants.AUTHORIZATION, required = false) String token){
        return transporteBcSvc.createTransporteSvc(transporteBcDto, token);
    }
}
