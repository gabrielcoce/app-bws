package com.gcoce.bc.ws.services.agricultor;

import com.gcoce.bc.ws.dto.agricultor.TransporteDto;
import com.gcoce.bc.ws.entities.agricultor.Transporte;
import com.gcoce.bc.ws.exceptions.AgricultorException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.projections.agricultor.TransporteProjection;
import com.gcoce.bc.ws.repositories.agricultor.TransporteRepository;
import com.gcoce.bc.ws.services.beneficio.AuthSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
@Service
@Transactional(transactionManager = "agricultorTransactionManager")
public class TransporteSvc {

    @Autowired
    private AuthSvc authSvc;

    @Autowired
    private TransporteRepository transporteRepository;

    public ResponseEntity<?> crearTransporteSvc(TransporteDto transporteDto, String token) {
        if (existsTransporte(transporteDto.getPlacaTransporte())) {
            throw new AgricultorException("El transporte ingresado ya existe.");
        }

        final Transporte transporte = Transporte.createTransport(transporteDto, authSvc.userFromToken(token));
        transporteRepository.save(transporte);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Transporte creado exitosamente.", true));
    }

    public List<TransporteProjection> obtenerTransporteSvc() {
        return transporteRepository.obtenerTransporte();
    }

    public boolean existsTransporte(String placaTransporte) {
        return transporteRepository.existsTransporteByPlacaTransporte(placaTransporte);
    }
}
