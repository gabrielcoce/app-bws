package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.dto.beneficio.CuentaDto;
import com.gcoce.bc.ws.entities.beneficio.Cuenta;
import com.gcoce.bc.ws.entities.beneficio.Solicitud;
import com.gcoce.bc.ws.exceptions.BeneficioException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.projections.beneficio.AllCuentaProjection;
import com.gcoce.bc.ws.projections.beneficio.CountProjection;
import com.gcoce.bc.ws.repositories.beneficio.CuentaRepository;
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
 * @since 27/05/2023
 */
@Service
@Transactional(transactionManager = "beneficioTransactionManager")
public class CuentaSvc {

    private final SolicitudSvc solicitudSvc;

    private final AuthSvc authSvc;

    private final CuentaRepository cuentaRepository;

    private static final Logger logger = LoggerFactory.getLogger(CuentaSvc.class);

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
        solicitudSvc.actualizarEstadoSolicitudSvc(cuentaDto.getNoSolicitud(), Constants.SOLICITUD_APROBADA, authSvc.userFromToken(token));
        Solicitud solicitud = solicitudSvc.obtenerSolicitud(cuentaDto.getNoSolicitud());
        if (!Objects.equals(solicitud.getEstadoSolicitud(), Constants.SOLICITUD_APROBADA)) {
            message = String.format("La cuenta no puede ser creada porque la solicitud %s no a sido aprobada.", solicitud.getNoSolicitud());
            throw new BeneficioException(message);
        }
        if (!Objects.equals(solicitud.getTipoSolicitud(), Constants.TIPO_SOLICITUD_CC)) {
            message = String.format("La cuenta no puede ser creada porque la solicitud %s no corresponde a creación de cuenta.", solicitud.getNoSolicitud());
            throw new BeneficioException(message);
        }
        Integer count = cuentaRepository.checkCount(cuentaDto.getNoSolicitud());
        if (count >= 1) {
            message = String.format("La solicitud %s ingresada ya esta asociada a una cuenta.", solicitud.getNoSolicitud());
            throw new BeneficioException(message);
        }
        final Cuenta cuenta = Cuenta.createdAccFromDto(cuentaDto.getUser(), solicitud);
        cuentaRepository.save(cuenta);
        message = String.format("Cuenta %s a sido registrada exitosamente.", cuenta.getNoCuenta());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, message, true));
    }

    public List<AllCuentaProjection> obtenerCuentasByUserSvc(String user) {
        if (!authSvc.existsUserSvc(user)) {
            throw new BeneficioException("Usuario no se encuentra registrado en Beneficio");
        }
        if (cuentaRepository.allCuentasByUser(user).isEmpty()) {
            throw new BeneficioException("Usuario no tiene cuentas registradas en Beneficio");
        }
        return cuentaRepository.allCuentasByUser(user);
    }

    public Boolean existeCuentaByUser(String user) {
        if (!authSvc.existsUserSvc(user)) {
            throw new BeneficioException("Usuario no se encuentra registrado en Beneficio");
        }
        return !cuentaRepository.allCuentasByUser(user).isEmpty();
    }

    public AllCuentaProjection obtenerCuentaByBcSvc(String noCuenta) {
        if (existsCuenta(noCuenta)) {
            throw new BeneficioException("No. Cuenta no existe en Beneficio");
        }
        return cuentaRepository.CuentaByNoCuenta(noCuenta);
    }

    public ResponseEntity<?> permitirIngresoSvc(String noCuenta, String token) {
        if (existsCuenta(noCuenta)) {
            throw new BeneficioException("No. Cuenta no existe en Beneficio");
        }
        var cuenta = obtenerCuentaSvc(noCuenta);
        if (!Objects.equals(cuenta.getEstadoCuenta(), Constants.CUENTA_CREADA)) {
            throw new BeneficioException("La Cuenta se encuentra en un estado no permitido");
        }
        var parcialidad = cuentaRepository.verificarParcialidades(noCuenta);
        if (parcialidad.isEmpty()) {
            throw new BeneficioException("No. Cuenta no tiene parcialidades en Beneficio");
        }
        updateEstadoCuentaSvc(noCuenta, Constants.CUENTA_ABIERTA, authSvc.userFromToken(token));
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Ingreso permitido", true));
    }

    public Boolean verificarPermitirIngresoSvc(String noCuenta) {
        if (existsCuenta(noCuenta)) {
            throw new BeneficioException("No. Cuenta no existe en Beneficio");
        }
        var cuenta = obtenerCuentaSvc(noCuenta);
        return Objects.equals(cuenta.getEstadoCuenta(), Constants.CUENTA_CREADA);
    }

    public ResponseEntity<?> cerrarCuentaSvc(String noCuenta, String token) {
        if (existsCuenta(noCuenta)) {
            throw new BeneficioException("No. Cuenta no existe en Beneficio");
        }
        var cuenta = obtenerCuentaSvc(noCuenta);
        if (!Objects.equals(cuenta.getEstadoCuenta(), Constants.PESAJE_FINALIZADO)) {
            throw new BeneficioException("La Cuenta se encuentra en un estado no permitido");
        }
        updateEstadoCuentaSvc(noCuenta, Constants.CUENTA_CERRADA, authSvc.userFromToken(token));
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Cuenta Cerrada", true));
    }

    public ResponseEntity<?> confirmarCuentaSvc(String noCuenta, String token) {
        if (existsCuenta(noCuenta)) {
            throw new BeneficioException("No. Cuenta no existe en Beneficio");
        }
        var cuenta = obtenerCuentaSvc(noCuenta);
        if (!Objects.equals(cuenta.getEstadoCuenta(), Constants.CUENTA_CERRADA)) {
            throw new BeneficioException("La Cuenta se encuentra en un estado no permitido");
        }

        if (Objects.equals(cuenta.getEstadoCuenta(), Constants.CUENTA_CONFIRMADA)) {
            throw new BeneficioException("La Cuenta ya se encuentra confirmada");
        }
        updateEstadoCuentaSvc(noCuenta, Constants.CUENTA_CONFIRMADA, authSvc.userFromToken(token));
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Cuenta Confirmada", true));
    }

    public void updateEstadoCuentaSvc(String noCuenta, Integer status, String user) {
        Cuenta cuenta = cuentaRepository.findById(noCuenta).orElse(null);
        if (cuenta == null) {
            throw new BeneficioException("No. Cuenta no existe en Beneficio");
        }
        cuenta.setEstadoCuenta(status);
        cuenta.setUserUpdated(user);
        cuenta.setUpdatedAt(new Date());
        cuentaRepository.save(cuenta);
    }

    public Cuenta obtenerCuentaSvc(String noCuenta) {
        return cuentaRepository.getCuentaByNoCuenta(noCuenta).orElseThrow(() -> new BeneficioException("No. Cuenta no existe en Beneficio"));
    }

    public boolean existsCuenta(String noCuenta) {
        return !cuentaRepository.existsCuentaByNoCuenta(noCuenta);
    }

    public List<CountProjection> obtenerCuentasSvc() {
        if (cuentaRepository.obtenerCuentas().isEmpty()) {
            throw new BeneficioException("No hay cuentas para cerrar o confirmar");
        }
        return cuentaRepository.obtenerCuentas();
    }

    public Boolean verificarCuentasSvc() {
        return !cuentaRepository.obtenerCuentas().isEmpty();
    }
}
