package com.gcoce.bc.ws.services.peso_cabal;

import com.gcoce.bc.ws.dto.peso_cabal.ParcialidadPcDto;
import com.gcoce.bc.ws.dto.peso_cabal.ResParcialidadDto;
import com.gcoce.bc.ws.entities.beneficio.Parcialidad;
import com.gcoce.bc.ws.entities.peso_cabal.ParcialidadPc;
import com.gcoce.bc.ws.exceptions.PesoCabalException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.projections.peso_cabal.ParcialidadPcProjection;
import com.gcoce.bc.ws.repositories.peso_cabal.ParcialidadPcRepository;
import com.gcoce.bc.ws.services.beneficio.AuthSvc;
import com.gcoce.bc.ws.services.beneficio.CuentaSvc;
import com.gcoce.bc.ws.services.beneficio.ParcialidadSvc;
import com.gcoce.bc.ws.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Gabriel Coc Estrada
 * @since 30/05/2023
 */
@Service
@Transactional(transactionManager = "pesoCabalTransactionManager")
public class ParcialidadPcSvc {
    @Autowired
    private ParcialidadPcRepository pcRepository;

    @Autowired
    private AuthSvc authSvc;

    @Autowired
    private ParcialidadSvc parcialidadBeneficioSvc;

    @Autowired
    private CuentaSvc cuentaSvc;

    private static final Logger logger = LoggerFactory.getLogger(ParcialidadPcSvc.class);

    public ResponseEntity<?> iniciarPesajeSvc(String noCuenta, String token) {
        var cuenta = cuentaSvc.obtenerCuentaSvc(noCuenta);
        if (!Objects.equals(cuenta.getEstadoCuenta(), Constants.CUENTA_ABIERTA)) {
            throw new PesoCabalException("La Cuenta se encuentra en un estado no permitido");
        }
        var parcialidades = obtenerParcialidadesSvc(noCuenta);
        if (verificarEstadoParcialidad(parcialidades)) {
            throw new PesoCabalException("Ya se inicio el pesaje");
        }
        cuentaSvc.updateEstadoCuentaSvc(noCuenta, Constants.PESAJE_INICIADO, authSvc.userFromToken(token));
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Pesaje Iniciado", true));
    }

    public ResponseEntity<?> finalizarPesajeSvc(String noCuenta, String token) {
        var cuenta = cuentaSvc.obtenerCuentaSvc(noCuenta);
        if (!Objects.equals(cuenta.getEstadoCuenta(), Constants.PESAJE_INICIADO)) {
            throw new PesoCabalException("La Cuenta se encuentra en un estado no permitido");
        }
        var parcialidades = obtenerParcialidadesSvc(noCuenta);
        if (!verificarEstadoParcialidad(parcialidades)) {
            throw new PesoCabalException("No se han verificado las parcialidades");
        }

        cuentaSvc.updateEstadoCuentaSvc(noCuenta, Constants.PESAJE_FINALIZADO, authSvc.userFromToken(token));
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Pesaje Finalizado", true));
    }

    public ResponseEntity<?> registrarParcialidadSvc(ParcialidadPcDto dto, String token) {
        ParcialidadPc parcialidadPc = new ParcialidadPc();
        if (cuentaSvc.existsCuenta(dto.getNoCuenta())) {
            throw new PesoCabalException("No. Cuenta no existe en Beneficio");
        }
        if (parcialidadBeneficioSvc.existsParcialidadSvc(dto.getParcialidadId())) {
            throw new PesoCabalException("No existe parcialidad en Beneficio");
        }
        if (pcRepository.existsParcialidadPcByParcialidadId(dto.getParcialidadId())) {
            throw new PesoCabalException("La parcialidad ya se registro en Peso Cabal");
        }
        var parcialidad = parcialidadBeneficioSvc.obtenerParcialidadSvc(dto.getParcialidadId());
        parcialidadPc.setNoCuenta(dto.getNoCuenta());
        parcialidadPc.setParcialidadId(dto.getParcialidadId());
        parcialidadPc.setPesoRegistrado(dto.getPesoRegistrado());
        parcialidadPc.setUserCreated(authSvc.userFromToken(token));
        parcialidadPc.setCreatedAt(new Date());
        calcularPesajes(dto, parcialidadPc, parcialidad);
        logger.info("parcialidad a registrar {}", parcialidadPc);
        pcRepository.save(parcialidadPc);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Parcialidad registrada correctamente", true));
    }

    public ResponseEntity<?> actualizarParcialidadSvc(ParcialidadPcDto dto, String token) {
        ParcialidadPc parcialidadPc;
        if (cuentaSvc.existsCuenta(dto.getNoCuenta())) {
            throw new PesoCabalException("No. Cuenta no existe en Beneficio");
        }
        if (parcialidadBeneficioSvc.existsParcialidadSvc(dto.getParcialidadId())) {
            throw new PesoCabalException("No existe parcialidad en Beneficio");
        }
        if (!pcRepository.existsParcialidadPcByParcialidadId(dto.getParcialidadId())) {
            throw new PesoCabalException("La parcialidad no se ha registro en Peso Cabal");
        }
        parcialidadPc = pcRepository.findParcialidadPcByParcialidadId(dto.getParcialidadId()).orElse(null);
        if (parcialidadPc == null) {
            throw new PesoCabalException("No se encontró parcialidad para actualizar");
        }
        var parcialidad = parcialidadBeneficioSvc.obtenerParcialidadSvc(dto.getParcialidadId());
        if (parcialidad.getVerified()) {
            throw new PesoCabalException("La parcialidad ya se encuentra verificada, por lo cual no puede ser actualizada");
        }
        parcialidadPc.setPesoRegistrado(dto.getPesoRegistrado());
        parcialidadPc.setUserUpdated(authSvc.userFromToken(token));
        parcialidadPc.setUpdatedAt(new Date());
        Double pesoIngresado = parcialidadPc.getPesoIngresado();
        if (dto.getPesoRegistrado() > pesoIngresado) {
            parcialidadPc.setPesoExcedido(dto.getPesoRegistrado() - pesoIngresado);
            parcialidadPc.setPesoFaltante(null);
        } else if (dto.getPesoRegistrado() < pesoIngresado) {
            parcialidadPc.setPesoFaltante(pesoIngresado - dto.getPesoRegistrado());
            parcialidadPc.setPesoExcedido(null);
        } else {
            parcialidadPc.setPesoExcedido(null);
            parcialidadPc.setPesoFaltante(null);
        }

        logger.info("parcialidad a actualizar {}", parcialidadPc);
        pcRepository.save(parcialidadPc);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Parcialidad actualizada correctamente", true));
    }

    public List<ResParcialidadDto> obtenerParcialidadesSvc(String noCuenta) {
        List<ResParcialidadDto> dtoList = new ArrayList<>();
        var parcialidades = parcialidadBeneficioSvc.obtenerParcialidadesSvc(noCuenta);
        if (parcialidades.isEmpty()) {
            throw new PesoCabalException("No. Cuenta no tiene parcialidades registradas en Beneficio");
        }
        parcialidades.forEach(parcialidad -> {
            ResParcialidadDto dto = new ResParcialidadDto();
            dto.setParcialidadId(parcialidad.getParcialidadId());
            dto.setPesoIngresado(parcialidad.getPesoIngresado());
            dto.setParcialidadVerificada(parcialidad.getParcialidadVerificada());
            dtoList.add(dto);
        });
        return dtoList;
    }

    public List<ParcialidadPcProjection> obtenerParcialidadesRegistradasSvc(String noCuenta) {
        if (cuentaSvc.existsCuenta(noCuenta)) {
            throw new PesoCabalException("No. Cuenta no existe en Beneficio");
        }
        if (pcRepository.parcialidadesRegistradas(noCuenta).isEmpty()) {
            throw new PesoCabalException("No. Cuenta no tiene parcialidades registradas en Peso Cabal");
        }
        return pcRepository.parcialidadesRegistradas(noCuenta);
    }

    public Boolean existeParcialidadSvc(UUID parcialidadId) {
        return pcRepository.existsParcialidadPcByParcialidadId(parcialidadId);
    }

    public ResponseEntity<?> verificarParcialidadSvc(UUID parcialidadId, String token) {
        if (parcialidadBeneficioSvc.existsParcialidadSvc(parcialidadId)) {
            throw new PesoCabalException("No existe parcialidad en Beneficio");
        }

        if (!parcialidadBeneficioSvc.verificarParcialidadSvc(parcialidadId, authSvc.userFromToken(token))) {
            throw new PesoCabalException("No se pudo verificar parcialidad en Beneficio");
        }
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Parcialidad verificada correctamente", true));
    }

    private void calcularPesajes(ParcialidadPcDto dto, ParcialidadPc parcialidadPc, Parcialidad parcialidad) {
        Double pesoIngresado = parcialidad.getPesoIngresado();
        parcialidadPc.setPesoIngresado(pesoIngresado);
        if (dto.getPesoRegistrado() > pesoIngresado) {
            parcialidadPc.setPesoExcedido(dto.getPesoRegistrado() - pesoIngresado);
        }

        if (dto.getPesoRegistrado() < pesoIngresado) {
            parcialidadPc.setPesoFaltante(pesoIngresado - dto.getPesoRegistrado());
        }
    }

    private boolean verificarEstadoParcialidad(List<ResParcialidadDto> dto) {
        List<Boolean> verificadas = dto.stream().map(ResParcialidadDto::getParcialidadVerificada).toList();
        return verificadas.stream().allMatch(Boolean::booleanValue);
    }
}
