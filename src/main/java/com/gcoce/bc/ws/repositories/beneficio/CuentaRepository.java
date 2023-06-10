package com.gcoce.bc.ws.repositories.beneficio;

import com.gcoce.bc.ws.entities.beneficio.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Gabriel Coc Estrada
 * @since 27/05/2023
 */
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    boolean existsCuentaByNoCuenta(String noCuenta);

    @Query(value = "select count(*) from beneficio_ws.cuenta c where c.no_solicitud =:noSolicitud", nativeQuery = true)
    Integer checkCount(@Param("noSolicitud") String noSolicitud);

    Optional<Cuenta> getCuentaByNoCuenta(String noCuenta);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update beneficio_ws.cuenta set \n" +
            "estado_cuenta=:estadoCuenta, user_updated=:userUpdated, updated_at=now()\n" +
            "where no_cuenta=:noCuenta", nativeQuery = true)
    Integer putEstadoCuenta(@Param("noCuenta") String noCuenta, @Param("estadoCuenta") Integer estadoCuenta, @Param("userUpdated") String userUpdated);
}
