package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.dto.beneficio.ActualizarPilotoDto;
import com.gcoce.bc.ws.dto.beneficio.PilotoBcDto;
import com.gcoce.bc.ws.entities.beneficio.PilotoBc;
import com.gcoce.bc.ws.exceptions.BeneficioException;
import com.gcoce.bc.ws.exceptions.RecordNotFoundException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.repositories.beneficio.PilotoBcRepository;
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
public class PilotoBcSvc {
    @Autowired
    private PilotoBcRepository pilotoBcRepository;

    @Autowired
    private AuthSvc authSvc;

    public ResponseEntity<?> createPilotoSvc(PilotoBcDto pilotoBcDto, String token) {
        if (existsPiloto(pilotoBcDto.getLicenciaPiloto())) {
            throw new BeneficioException("El piloto ingresado ya existe.");
        }
        final PilotoBc pilotoBc = PilotoBc.createPiloto(pilotoBcDto.getLicenciaPiloto(), pilotoBcDto.getNombre(), authSvc.userFromToken(token));
        pilotoBcRepository.save(pilotoBc);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Piloto creado exitosamente.", true));
    }

    public ResponseEntity<?> updateEstadoPilotoSvc(ActualizarPilotoDto actualizarPilotoDto, String token) {
        String message;
        var pilotoBc = pilotoBcRepository.findById(actualizarPilotoDto.getLicenciaPiloto()).orElse(null);
        if (pilotoBc == null) {
            message = String.format("No se encontró ningún piloto %s para actualizar", actualizarPilotoDto.getLicenciaPiloto());
            throw new BeneficioException(message);
        }

        if (!updateStatusPiloto(actualizarPilotoDto.getLicenciaPiloto(), actualizarPilotoDto.getStatus(), authSvc.userFromToken(token))) {
            throw new BeneficioException("No se pudo actualizar el estado del piloto.");
        }
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Piloto actualizado exitosamente.", true));
    }

    public boolean existsPiloto(String licenciaPiloto) {
        return pilotoBcRepository.existsPilotoBcByLicenciaPiloto(licenciaPiloto);
    }

    public boolean statusPiloto(String licenciaPiloto) {
        PilotoBc pilotoBc = pilotoBcRepository.findPilotoBcByLicenciaPiloto(licenciaPiloto).orElseThrow(() -> new RecordNotFoundException("Piloto no encontrado."));
        return pilotoBc.getStatus();
    }

    private boolean updateStatusPiloto(String licenciaPiloto, Boolean status, String user) {
        Integer estado = pilotoBcRepository.putEstadoTransporte(licenciaPiloto, status, user);
        return estado != 0;
    }
}
