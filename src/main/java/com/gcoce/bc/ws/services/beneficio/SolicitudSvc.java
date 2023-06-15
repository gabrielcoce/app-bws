package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.dto.beneficio.SolicitudDto;
import com.gcoce.bc.ws.entities.beneficio.Solicitud;
import com.gcoce.bc.ws.exceptions.BeneficioException;
import com.gcoce.bc.ws.exceptions.RecordNotFoundException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.projections.beneficio.AprobarSolicitudesProjection;
import com.gcoce.bc.ws.projections.beneficio.SolicitudesProjection;
import com.gcoce.bc.ws.repositories.beneficio.SolicitudRepository;
import com.gcoce.bc.ws.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Gabriel Coc Estrada
 * @since 20/05/2023
 */
@Service
@Transactional(transactionManager = "beneficioTransactionManager")
public class SolicitudSvc {
    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private AuthSvc authSvc;

    private static final Logger logger = LoggerFactory.getLogger(SolicitudSvc.class);

    public ResponseEntity<?> createSolicitudSvc(SolicitudDto solicitudDto, String token) {
        String message;
        if (!authSvc.existsUserSvc(solicitudDto.getUsuarioSolicita())) {
            throw new BeneficioException("Usuario no se encuentra registrado en Beneficio");
        }

        if (checkActiveReqSvc(solicitudDto.getUsuarioSolicita())) {
            throw new BeneficioException("Usuario cuenta con una solicitud en proceso");
        }
        if (authSvc.validateUserToken(token, solicitudDto.getUsuarioSolicita())) {
            throw new BeneficioException("Usuario ingresado no corresponde a usuario logueado");
        }
        try {
            final Solicitud solicitud = Solicitud.createdReqFromDto(solicitudDto);
            solicitudRepository.save(solicitud);
            message = String.format("Solicitud %s a sido creada exitosamente", solicitud.getNoSolicitud());
            return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, message, true));
        } catch (BeneficioException e) {
            throw new BeneficioException("No se pudo crear la solicitud");
        }
    }

    /*public ResponseEntity<?> updateEstadoSolicitudSvc(ActualizarSolicitudDto solicitudDto, String token) {
        String message;
        String user;
        Solicitud solicitud;
        solicitud = solicitudRepository.findById(solicitudDto.getNoSolicitud()).orElse(null);
        if (solicitud != null) {
            user = authSvc.userFromToken(token);
            solicitud.setEstadoSolicitud(solicitudDto.getNuevoEstado());
            solicitud.setUserUpdated(user);
            solicitud.setUpdatedAt(new Date());
            solicitudRepository.save(solicitud);
            message = String.format("Se actualizo correctamente la solicitud %s", solicitudDto.getNoSolicitud());
            return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, message, true));
        } else {
            message = String.format("No se encontró ninguna solicitud %s para actualizar", solicitudDto.getNoSolicitud());
            throw new BeneficioException(message);
        }
    }*/

    public ResponseEntity<?> rechazarSolicitudSvc(String noSolicitud, String token) {
        var solicitud = obtenerSolicitud(noSolicitud);
        if (Objects.equals(solicitud.getEstadoSolicitud(), Constants.SOLICITUD_RECHAZADA)) {
            throw new BeneficioException("La Solicitud ya se encuentra rechazada");
        }
        if (Objects.equals(solicitud.getEstadoSolicitud(), Constants.SOLICITUD_APROBADA)) {
            throw new BeneficioException("La Solicitud ya se encuentra aprobada");
        }
        actualizarEstadoSolicitudSvc(noSolicitud, Constants.SOLICITUD_RECHAZADA, authSvc.userFromToken(token));
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Solicitud rechazada correctamente", true));
    }

    public void actualizarEstadoSolicitudSvc(String noSolicitud, Integer estadoSolicitud, String user) {
        Solicitud solicitud = solicitudRepository.findById(noSolicitud).orElse(null);
        if (solicitud == null) {
            throw new BeneficioException("No se pudo actualizar solicitud");
        }
        solicitud.setEstadoSolicitud(estadoSolicitud);
        solicitud.setUserUpdated(user);
        solicitud.setUpdatedAt(new Date());
        logger.info("Solicitud actualizada {}", solicitud);
        solicitudRepository.save(solicitud);
    }

    public Boolean verificaSolicitudesSvc(String usuarioSolicita) {
        if (!authSvc.existsUserSvc(usuarioSolicita)) {
            throw new BeneficioException("Usuario no se encuentra registrado en Beneficio");
        }
        List<Solicitud> solicitud = solicitudRepository.checkSolicitudes(usuarioSolicita);
        return !solicitud.isEmpty();
    }

    public List<SolicitudesProjection> obtenerSolicitudesSvc(String usuarioSolicita) {
        if (!authSvc.existsUserSvc(usuarioSolicita)) {
            throw new BeneficioException("Usuario no se encuentra registrado en Beneficio");
        }
        if (solicitudRepository.obtenerSolicitudes(usuarioSolicita).isEmpty()) {
            throw new BeneficioException("Usuario no cuenta con solicitudes en Beneficio");
        }
        return solicitudRepository.obtenerSolicitudes(usuarioSolicita);
    }

    public boolean checkActiveReqSvc(String usuarioSolicita) {
        List<Solicitud> solicitud = solicitudRepository.checkActiveReq(usuarioSolicita);
        return !solicitud.isEmpty();
    }

    public boolean existsByNoSolicitudSvc(String noSolicitud) {
        return solicitudRepository.existsByNoSolicitud(noSolicitud);
    }

    public Solicitud obtenerSolicitud(String noSolicitud) {
        return solicitudRepository.getSolicitudByNoSolicitud(noSolicitud)
                .orElseThrow(() -> new RecordNotFoundException("Solicitud no existe"));
    }

    public List<AprobarSolicitudesProjection> obtenerSolicitudesApprobationSvc() {
        if (solicitudRepository.obtenerSolicitudesAprobar().isEmpty()) {
            throw new BeneficioException("No hay solicitudes para aprobar o rechazar");
        }
        return solicitudRepository.obtenerSolicitudesAprobar();
    }

    public Boolean verificarSolicitudesSvc(){
        return !solicitudRepository.obtenerSolicitudesAprobar().isEmpty();
    }
}
