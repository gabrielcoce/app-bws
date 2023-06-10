package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.dto.beneficio.TransporteBcDto;
import com.gcoce.bc.ws.entities.beneficio.TransporteBc;
import com.gcoce.bc.ws.exceptions.BeneficioException;
import com.gcoce.bc.ws.exceptions.RecordNotFoundException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.repositories.beneficio.TransporteBcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
@Service
@Transactional(transactionManager = "beneficioTransactionManager")
public class TransporteBcSvc {
    @Autowired
    private TransporteBcRepository transporteBcRepository;

    @Autowired
    private AuthSvc authSvc;

    public ResponseEntity<?> createTransporteSvc(TransporteBcDto transporteBcDto, String token) {
        if (existsTransporte(transporteBcDto.getPlacaTransporte())) {
            throw new BeneficioException("El transporte ingresado ya existe.");
        }
        final TransporteBc transporteBc = TransporteBc.createTransporte(transporteBcDto, authSvc.userFromToken(token));
        transporteBcRepository.save(transporteBc);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Transporte creado exitosamente.", true));
    }

    public boolean existsTransporte(String placaTransporte) {
        return transporteBcRepository.existsTransporteBcByPlacaTransporte(placaTransporte);
    }

    public boolean statusTransporte(String placaTransporte){
        TransporteBc transporteBc = transporteBcRepository.findTransporteBcByPlacaTransporte(placaTransporte).orElseThrow(() -> new RecordNotFoundException("Transporte no encontrado."));
        return transporteBc.getStatus();
    }
}
