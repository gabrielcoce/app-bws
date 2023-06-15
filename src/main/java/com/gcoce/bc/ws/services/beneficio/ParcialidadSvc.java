package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.dto.beneficio.ParcialidadDto;
import com.gcoce.bc.ws.entities.beneficio.Cuenta;
import com.gcoce.bc.ws.entities.beneficio.Parcialidad;
import com.gcoce.bc.ws.exceptions.BeneficioException;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.projections.beneficio.AllParcialidadProjection;
import com.gcoce.bc.ws.projections.beneficio.CuentaProjection;
import com.gcoce.bc.ws.projections.beneficio.ParcialidadProjection;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
        Integer pesoIngresado;
        Integer pesoResultante;
        Integer parcialidadIngresada;
        String user;
        if (cuentaSvc.existsCuenta(parcialidadDto.getNoCuenta())) {
            throw new BeneficioException("No. Cuenta no existe en Beneficio");
        }
        CuentaProjection cuentaProjection = parcialidadRepository.consultaCuenta(parcialidadDto.getNoCuenta());
        pesoIngresado = parcialidadRepository.checkPesoIngresado(parcialidadDto.getNoCuenta());
        pesoResultante = pesoRestante(cuentaProjection.getPesoTotal(), pesoIngresado);
        parcialidadIngresada = parcialidadRepository.checkParcialidadesByNoCuenta(parcialidadDto.getNoCuenta());
        user = authSvc.userFromToken(token);
        logger.info("estado cuenta " + cuentaProjection.getEstadoCuenta());
        logger.info("cantidad de peso registrado " + cuentaProjection.getPesoTotal());
        logger.info("cantidad de peso ingresado a beneficio " + pesoIngresado);
        logger.info("cantidad de peso resultante " + pesoResultante);
        logger.info("cantidad de parcialidades registradas " + cuentaProjection.getCantidadParcialidades());
        logger.info("cantidad de parcialidades ingresadas " + parcialidadIngresada);
        if (!Objects.equals(cuentaProjection.getEstadoCuenta(), Constants.CUENTA_CREADA)) {
            throw new BeneficioException("La Cuenta se encuentra en un estado no permitido");
        }
        if (allowRegisterParcialidad(parcialidadIngresada, cuentaProjection.getCantidadParcialidades())) {
            throw new BeneficioException("Parcialidades completadas");
        }

        if (lastParcialidad(cuentaProjection.getCantidadParcialidades(), parcialidadIngresada)) {
            logger.info("ultima parcialidad");
            if (allowLastRegisterPeso(parcialidadDto.getPesoIngresado(), pesoResultante)) {
                throw new BeneficioException("Peso excede el limite permitido");
            }

            if (pesoMenorRestante(parcialidadDto.getPesoIngresado(), pesoResultante)) {
                throw new BeneficioException("Peso es menor el limite permitido");
            }
        } else {
            if (allowRegisterPeso(parcialidadDto.getPesoIngresado(), pesoResultante)) {
                throw new BeneficioException("Peso excede el limite permitido");
            }
        }

        if (!pilotoBcSvc.existsPiloto(parcialidadDto.getLicenciaPiloto())) {
            throw new BeneficioException("Piloto no existe en el beneficio");
        }

        if (!pilotoBcSvc.statusPiloto(parcialidadDto.getLicenciaPiloto())) {
            throw new BeneficioException("Piloto no esta activo en el beneficio");
        }

        if (!transporteBcSvc.existsTransporte(parcialidadDto.getPlacaTransporte())) {
            throw new BeneficioException("Transporte no existe en el beneficio");
        }
        if (!transporteBcSvc.statusTransporte(parcialidadDto.getPlacaTransporte())) {
            throw new BeneficioException("Transporte no esta activo en el beneficio");
        }
        final Cuenta cuenta = cuentaSvc.obtenerCuentaSvc(parcialidadDto.getNoCuenta());
        final Parcialidad parcialidad = Parcialidad.createdParcialidad(parcialidadDto, cuenta, user);
        logger.info("parcialidad a guardar " + parcialidad);
        parcialidadRepository.save(parcialidad);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Parcialidad creada exitosamente", true));
    }

    public List<ParcialidadProjection> obtenerParcialidadesSvc(String noCuenta) {
        if (cuentaSvc.existsCuenta(noCuenta)) {
            throw new BeneficioException("No. Cuenta no existe en Beneficio");
        }
        return parcialidadRepository.obtenerParcialidades(noCuenta);
    }

    public List<AllParcialidadProjection> allParcialidadesSvc(String noCuenta) {
        if (cuentaSvc.existsCuenta(noCuenta)) {
            throw new BeneficioException("No. Cuenta no existe en Beneficio");
        }
        if (parcialidadRepository.allParcialidadesByCuenta(noCuenta).isEmpty()) {
            throw new BeneficioException("No. Cuenta no tiene parcialidades registradas");
        }
        return parcialidadRepository.allParcialidadesByCuenta(noCuenta);
    }
    public List<AllParcialidadProjection> allParcialidadesUserSvc(String user){
        if (!authSvc.existsUserSvc(user)) {
            throw new BeneficioException("Usuario no se encuentra registrado en Beneficio");
        }
        if(parcialidadRepository.allParcialidadesByUser(user).isEmpty()){
            throw new BeneficioException("Usuario no cuenta con parcialidades registradas");
        }
        return parcialidadRepository.allParcialidadesByUser(user);
    }

    public Boolean existeParcialidadUserSvc(String user) {
        if (!authSvc.existsUserSvc(user)) {
            throw new BeneficioException("Usuario no se encuentra registrado en Beneficio");
        }
        return !parcialidadRepository.allParcialidadesByUser(user).isEmpty();
    }

    public Parcialidad obtenerParcialidadSvc(UUID parcialidadId) {
        return parcialidadRepository.findParcialidadByParcialidadId(parcialidadId).orElseThrow(() -> new BeneficioException("No existe parcialidad"));
    }

    public boolean verificarParcialidadSvc(UUID parcialidadId, String user, Double pesoRegistrado) {
        Parcialidad parcialidad = parcialidadRepository.findById(parcialidadId).orElse(null);
        if (parcialidad == null) {
            logger.error("No se puedo verificar parcialidad");
            return false;
        }
        if (!authSvc.existsUserSvc(user)) {
            logger.error("No se puedo verificar parcialidad");
            return false;
        }
        parcialidad.setVerified(true);
        parcialidad.setPesoVerificado(pesoRegistrado);
        parcialidad.setUserUpdated(user);
        parcialidad.setUpdatedAt(new Date());
        parcialidadRepository.save(parcialidad);
        logger.info("Se verifico correctamente la parcialidad");
        return true;
    }

    private boolean allowRegisterParcialidad(Integer parcialidadIngresada, Integer parcialidadRegistrada) {
        return parcialidadIngresada >= parcialidadRegistrada;
    }

    private boolean allowRegisterPeso(Integer pesoIngresado, Integer allowPeso) {
        return pesoIngresado >= allowPeso;
    }

    private boolean allowLastRegisterPeso(Integer pesoIngresado, Integer allowPeso) {
        return pesoIngresado > allowPeso;
    }

    private Integer pesoRestante(Integer pesoResultante, Integer pesoIngresado) {
        return pesoResultante - pesoIngresado;
    }

    private boolean pesoMenorRestante(Integer pesoIngresado, Integer pesoResultante) {
        return pesoIngresado < pesoResultante;
    }

    private boolean lastParcialidad(Integer parcialidadRegistrada, Integer parcialidadIngresada) {
        int parcialidadRestante = parcialidadRegistrada - parcialidadIngresada;
        return parcialidadRestante == 1;
    }

    public boolean existsParcialidadSvc(UUID parcialidadId) {
        return !parcialidadRepository.existsParcialidadByParcialidadId(parcialidadId);
    }
}
