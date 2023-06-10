package com.gcoce.bc.ws.repositories.beneficio;

import com.gcoce.bc.ws.entities.beneficio.Parcialidad;
import com.gcoce.bc.ws.projections.beneficio.CuentaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 28/05/2023
 */
@Repository
public interface ParcialidadRepository extends JpaRepository<Parcialidad, UUID> {

    @Query(value = "select count(*) as parcialidades\n" +
            "from beneficio_ws.parcialidad p \n" +
            "where p.no_cuenta = :noCuenta", nativeQuery = true)
    Integer checkParcialidadesByNoCuenta(@Param("noCuenta") String noCuenta);

    @Query(value = "select coalesce(sum(p.peso_ingresado), 0) as peso_ingresado \n" +
            "from beneficio_ws.parcialidad p \n" +
            "where p.no_cuenta = :noCuenta", nativeQuery = true)
    Integer checkPesoIngresado(@Param("noCuenta") String noCuenta);

    @Query(value = "select c.no_cuenta noCuenta, c.estado_cuenta estadoCuenta, s.peso_total pesoTotal, s.cantidad_parcialidades cantidadParcialidades\n" +
            "from beneficio_ws.solicitud s \n" +
            "inner join beneficio_ws.cuenta c on c.no_solicitud = s.no_solicitud \n" +
            "where c.no_cuenta = :noCuenta", nativeQuery = true)
    CuentaProjection consultaCuenta(@Param("noCuenta") String noCuenta);
}
