package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.dto.beneficio.ActualizarCuentaDto;
import com.gcoce.bc.ws.dto.beneficio.CuentaDto;
import com.gcoce.bc.ws.entities.beneficio.Cuenta;
import com.gcoce.bc.ws.entities.beneficio.Solicitud;
import com.gcoce.bc.ws.exceptions.BeneficioException;
import com.gcoce.bc.ws.exceptions.RecordNotFoundException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.repositories.beneficio.CuentaRepository;
import com.gcoce.bc.ws.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @author Gabriel Coc Estrada
 * @since 27/05/2023
 */
@Service
@Transactional(transactionManager = "beneficioTransactionManager")
public class CuentaSvc {

    private final SolicitudSvc solicitudSvc;

    private final AuthSvc authSvc;

    private final CuentaRepository cuentaRepository;

    public CuentaSvc(SolicitudSvc solicitudSvc, AuthSvc authSvc, CuentaRepository cuentaRepository) {
        this.solicitudSvc = solicitudSvc;
        this.authSvc = authSvc;
        this.cuentaRepository = cuentaRepository;
    }

    public ResponseEntity<?> crearCuentaSvc(CuentaDto cuentaDto, String token) {
        String message;
        if (authSvc.validateUserToken(token, cuentaDto.getUser())) {
            throw new BeneficioException("Usuario ingresado no corresponde a usuario logueado.");
        }
        if (!solicitudSvc.existsByNoSolicitudSvc(cuentaDto.getNoSolicitud())) {
            message = String.format("La solicitud %s no existe.", cuentaDto.getNoSolicitud());
            throw new BeneficioException(message);
        }
        Solicitud solicitud = solicitudSvc.obtenerSolicitud(cuentaDto.getNoSolicitud());
        if (!Objects.equals(solicitud.getEstadoSolicitud(), Constants.SOLICITUD_APROBADA)) {
            message = String.format("La cuenta no puede ser creada porque la solicitud %s no a sido aprobada.", solicitud.getNoSolicitud());
            throw new BeneficioException(message);
        }
        if (!Objects.equals(solicitud.getEstadoSolicitud(), Constants.TIPO_SOLICITUD_CC)) {
            message = String.format("La cuenta no puede ser creada porque la solicitud %s no corresponde a creación de cuenta.", solicitud.getNoSolicitud());
            throw new BeneficioException(message);
        }
        Integer count = cuentaRepository.checkCount(cuentaDto.getNoSolicitud());
        if (count >= 1) {
            message = String.format("La solicitud %s ingresada ya esta asociada a una cuenta.", solicitud.getNoSolicitud());
            throw new BeneficioException(message);
        }
        //VERIFICAR ESTADO DE LA CUENTA
        final Cuenta cuenta = Cuenta.createdAccFromDto(cuentaDto.getUser(), solicitud);
        cuentaRepository.save(cuenta);
        message = String.format("Cuenta %s a sido registrada exitosamente.", cuenta.getNoCuenta());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, message, true));
    }

    public ResponseEntity<?> updateCuentaSvc(ActualizarCuentaDto cuentaDto, String token) {
        String message;
        String user;
        Cuenta cuenta;
        cuenta = cuentaRepository.findById(cuentaDto.getNoCuenta()).orElse(null);
        if (cuenta != null) {
            user = authSvc.userFromToken(token);
            cuenta.setEstadoCuenta(cuentaDto.getNuevoEstado());
            cuenta.setUserUpdated(user);
            cuenta.setUpdatedAt(new Date());
            cuentaRepository.save(cuenta);
            message = String.format("Se actualizo correctamente la cuenta %s", cuentaDto.getNoCuenta());
            return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, message, true));
        } else {
            message = String.format("No se encontró ninguna cuenta %s para actualizar", cuentaDto.getNoCuenta());
            throw new BeneficioException(message);
        }
    }

    public Cuenta obtenerCuenta(String noCuenta) {
        return cuentaRepository.getCuentaByNoCuenta(noCuenta).orElseThrow(() -> new RecordNotFoundException("Cuenta no encontrada"));
    }

    public boolean existsCuenta(String noCuenta) {
        return cuentaRepository.existsCuentaByNoCuenta(noCuenta);
    }

    public boolean updateEstadoCuenta(String noCuenta, Integer status, String user) {
        Integer estado = cuentaRepository.putEstadoCuenta(noCuenta, status, user);
        return estado != 0;
    }
}
