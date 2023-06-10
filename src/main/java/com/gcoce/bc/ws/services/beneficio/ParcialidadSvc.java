package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.dto.beneficio.ActualizarCuentaDto;
import com.gcoce.bc.ws.dto.beneficio.ParcialidadDto;
import com.gcoce.bc.ws.dto.beneficio.VerificarParcialidadDto;
import com.gcoce.bc.ws.entities.beneficio.Cuenta;
import com.gcoce.bc.ws.entities.beneficio.Parcialidad;
import com.gcoce.bc.ws.exceptions.BeneficioException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.projections.beneficio.CuentaProjection;
import com.gcoce.bc.ws.repositories.beneficio.ParcialidadRepository;
import com.gcoce.bc.ws.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Gabriel Coc Estrada
 * @since 28/05/2023
 */
@Service
@Transactional(transactionManager = "beneficioTransactionManager")
public class ParcialidadSvc {
    @Autowired
    private ParcialidadRepository parcialidadRepository;

    @Autowired
    private AuthSvc authSvc;

    @Autowired
    private CuentaSvc cuentaSvc;

    @Autowired
    private PilotoBcSvc pilotoBcSvc;

    @Autowired
    private TransporteBcSvc transporteBcSvc;

    private static final Logger logger = LoggerFactory.getLogger(ParcialidadSvc.class);

    public ResponseEntity<?> createParcialidadSvc(ParcialidadDto parcialidadDto, String token) {
        String message;
        Integer pesoIngresado;
        Integer pesoResultante;
        Integer parcialidadIngresada;
        String user;
        if (!cuentaSvc.existsCuenta(parcialidadDto.getNoCuenta())) {
            message = String.format("La cuenta %s no existe.", parcialidadDto.getNoCuenta());
            throw new BeneficioException(message);
        }
        CuentaProjection cuentaProjection = parcialidadRepository.consultaCuenta(parcialidadDto.getNoCuenta());
        pesoIngresado = parcialidadRepository.checkPesoIngresado(parcialidadDto.getNoCuenta());
        pesoResultante = pesoRestante(cuentaProjection.getPesoTotal(), pesoIngresado);
        parcialidadIngresada = parcialidadRepository.checkParcialidadesByNoCuenta(parcialidadDto.getNoCuenta());
        user = authSvc.userFromToken(token);
        //logger.info("no cuenta " + cuentaProjection.getNoCuenta());
        logger.info("estado cuenta " + cuentaProjection.getEstadoCuenta());
        logger.info("cantidad de peso registrado " + cuentaProjection.getPesoTotal());
        logger.info("cantidad de peso ingresado a beneficio " + pesoIngresado);
        logger.info("cantidad de peso resultante " + pesoResultante);
        logger.info("cantidad de parcialidades registradas " + cuentaProjection.getCantidadParcialidades());
        logger.info("cantidad de parcialidades ingresadas " + parcialidadIngresada);
        if (allowRegisterParcialidad(parcialidadIngresada, cuentaProjection.getCantidadParcialidades())) {
            throw new BeneficioException("Parcialidades completadas.");
        }
        if (firstParcialidad(parcialidadIngresada)) {
            logger.info("primera parcialidad");
            if (!cuentaSvc.updateEstadoCuenta(parcialidadDto.getNoCuenta(), Constants.CUENTA_ABIERTA, user)) {
                throw new BeneficioException("No se pudo actualizar el estado de la cuenta.");
            }
        }
        if (lastParcialidad(cuentaProjection.getCantidadParcialidades(), parcialidadIngresada)) {
            logger.info("ultima parcialidad");
            if (allowLastRegisterPeso(parcialidadDto.getPesoIngresado(), pesoResultante)) {
                throw new BeneficioException("peso excede el limite permitido");
            }

            if (pesoMenorRestante(parcialidadDto.getPesoIngresado(), pesoResultante)) {
                throw new BeneficioException("peso es menor el limite permitido");
            }
        } else {
            if (allowRegisterPeso(parcialidadDto.getPesoIngresado(), pesoResultante)) {
                throw new BeneficioException("peso excede el limite permitido");
            }
        }

        if (!pilotoBcSvc.existsPiloto(parcialidadDto.getLicenciaPiloto())) {
            throw new BeneficioException("Piloto no existe en el beneficio.");
        }

        if (!pilotoBcSvc.statusPiloto(parcialidadDto.getLicenciaPiloto())) {
            throw new BeneficioException("Piloto no esta activo en el beneficio.");
        }

        if (!transporteBcSvc.existsTransporte(parcialidadDto.getPlacaTransporte())) {
            throw new BeneficioException("Transporte no existe en el beneficio.");
        }
        if (!transporteBcSvc.statusTransporte(parcialidadDto.getPlacaTransporte())) {
            throw new BeneficioException("Transporte no esta activo en el beneficio.");
        }
        final Cuenta cuenta = cuentaSvc.obtenerCuenta(parcialidadDto.getNoCuenta());
        final Parcialidad parcialidad = Parcialidad.createdParcialidad(parcialidadDto, cuenta, user);
        logger.info("parcialidad a guardar " + parcialidad);
        parcialidadRepository.save(parcialidad);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Parcialidad creada exitosamente.", true));
    }

    public ResponseEntity<?> verificarParcialidadSvc(VerificarParcialidadDto parcialidadDto, String token) {
        String message;
        String user;
        Parcialidad parcialidad;
        parcialidad = parcialidadRepository.findById(parcialidadDto.getParcialidadId()).orElse(null);
        if (parcialidad != null) {
            user = authSvc.userFromToken(token);
            parcialidad.setVerified(true);
            parcialidad.setUserUpdated(user);
            parcialidad.setUpdatedAt(new Date());
            parcialidadRepository.save(parcialidad);
            message = "Se verifico correctamente la parcialidad";
            return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, message, true));
        } else {
            message = "No se encontró ninguna parcialidad para verificar";
            throw new BeneficioException(message);
        }
    }

    public boolean allowRegisterParcialidad(Integer parcialidadIngresada, Integer parcialidadRegistrada) {
        return parcialidadIngresada >= parcialidadRegistrada;
    }

    public boolean allowRegisterPeso(Integer pesoIngresado, Integer allowPeso) {
        return pesoIngresado >= allowPeso;
    }

    public boolean allowLastRegisterPeso(Integer pesoIngresado, Integer allowPeso) {
        return pesoIngresado > allowPeso;
    }

    public Integer pesoRestante(Integer pesoResultante, Integer pesoIngresado) {
        return pesoResultante - pesoIngresado;
    }

    public boolean pesoMenorRestante(Integer pesoIngresado, Integer pesoResultante) {
        return pesoIngresado < pesoResultante;
    }

    public boolean firstParcialidad(Integer parcialidadIngresada) {
        return parcialidadIngresada == 0;
    }

    public boolean lastParcialidad(Integer parcialidadRegistrada, Integer parcialidadIngresada) {
        int parcialidadRestante = parcialidadRegistrada - parcialidadIngresada;
        return parcialidadRestante == 1;
    }
}
