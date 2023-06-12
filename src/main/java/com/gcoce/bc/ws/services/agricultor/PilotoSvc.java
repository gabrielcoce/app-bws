package com.gcoce.bc.ws.services.agricultor;

import com.gcoce.bc.ws.dto.agricultor.PilotoDto;
import com.gcoce.bc.ws.entities.agricultor.Piloto;
import com.gcoce.bc.ws.exceptions.AgricultorException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.projections.agricultor.PilotoProjection;
import com.gcoce.bc.ws.repositories.agricultor.PilotoRepository;
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
public class PilotoSvc {
    @Autowired
    private AuthSvc authSvc;

    @Autowired
    private PilotoRepository pilotoRepository;

    public ResponseEntity<?> crearPilotoSvc(PilotoDto pilotoDto, String token) {
        if (existsPiloto(pilotoDto.getLicenciaPiloto())) {
            throw new AgricultorException("El piloto ingresado ya existe.");
        }

        final Piloto piloto = Piloto.createPilot(pilotoDto.getLicenciaPiloto(), pilotoDto.getNombre(), authSvc.userFromToken(token));
        pilotoRepository.save(piloto);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Piloto creado exitosamente.", true));
    }

    public List<PilotoProjection> obtenerPilotoSvc() {
        return pilotoRepository.obtenerPilotos();
    }

    public boolean existsPiloto(String licenciaPiloto) {
        return pilotoRepository.existsPilotoByLicenciaPiloto(licenciaPiloto);
    }
}
