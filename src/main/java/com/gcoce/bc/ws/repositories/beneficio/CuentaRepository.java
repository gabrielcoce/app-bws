package com.gcoce.bc.ws.repositories.beneficio;

import com.gcoce.bc.ws.entities.beneficio.Cuenta;
import com.gcoce.bc.ws.projections.beneficio.AllCuentaProjection;
import com.gcoce.bc.ws.projections.beneficio.CountProjection;
import com.gcoce.bc.ws.projections.beneficio.CuentaParcialidadProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Query(value = "select c.no_cuenta noCuenta, s.peso_total peso, s.cantidad_parcialidades parcialidades, \n" +
            "c2.nombre_catalogo estado, c.created_at createdAt\n" +
            "from beneficio_ws.cuenta c \n" +
            "inner join beneficio_ws.solicitud s on s.no_solicitud = c.no_solicitud \n" +
            "inner join beneficio_ws.catalogo c2 on c2.codigo_catalogo = c.estado_cuenta \n" +
            "where s.usuario_solicita =:usuario order by c.created_at desc", nativeQuery = true)
    List<AllCuentaProjection> allCuentasByUser(@Param("usuario") String usuario);

    @Query(value = "select c.no_cuenta noCuenta, s.peso_total peso, s.cantidad_parcialidades parcialidades, \n" +
            "c2.nombre_catalogo estado, c.created_at createdAt\n" +
            "from beneficio_ws.cuenta c\n" +
            "inner join beneficio_ws.solicitud s on s.no_solicitud = c.no_solicitud \n" +
            "inner join beneficio_ws.catalogo c2 on c2.codigo_catalogo = c.estado_cuenta \n" +
            "where c.no_cuenta =:noCuenta", nativeQuery = true)
    AllCuentaProjection CuentaByNoCuenta(@Param("noCuenta") String noCuenta);

    @Query(value = "select p.parcialidad_id parcialidades\n" +
            "from beneficio_ws.cuenta c\n" +
            "inner join beneficio_ws.parcialidad p on p.no_cuenta = c.no_cuenta \n" +
            "where c.no_cuenta=:noCuenta", nativeQuery = true)
    List<CuentaParcialidadProjection> verificarParcialidades(@Param("noCuenta") String noCuenta);

    @Query(value = "select c.no_cuenta noCuenta, c2.nombre_catalogo estadoCuenta, p.parcialidad_id parcialidadId, \n" +
            "p.peso_ingresado pesoIngresado, p.peso_verificado pesoVerificado,  p.updated_at updatedAt\n" +
            "from beneficio_ws.cuenta c\n" +
            "inner join beneficio_ws.parcialidad p on p.no_cuenta = c.no_cuenta \n" +
            "inner join beneficio_ws.catalogo c2 on c2.codigo_catalogo = c.estado_cuenta \n" +
            "where c.estado_cuenta in(7, 8) order by p.updated_at asc", nativeQuery = true)
    List<CountProjection> obtenerCuentas();
}
