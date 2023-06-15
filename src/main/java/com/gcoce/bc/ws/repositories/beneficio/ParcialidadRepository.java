package com.gcoce.bc.ws.repositories.beneficio;

import com.gcoce.bc.ws.entities.beneficio.Parcialidad;
import com.gcoce.bc.ws.projections.beneficio.AllParcialidadProjection;
import com.gcoce.bc.ws.projections.beneficio.CuentaProjection;
import com.gcoce.bc.ws.projections.beneficio.ParcialidadProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 28/05/2023
 */
@Repository
public interface ParcialidadRepository extends JpaRepository<Parcialidad, UUID> {

    boolean existsParcialidadByParcialidadId(UUID parcialidadId);

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

    @Query(value = "select p.parcialidad_id parcialidadId, p.peso_ingresado pesoIngresado, p.parcialidad_verificada  parcialidadVerificada \n" +
            "from beneficio_ws.parcialidad p \n" +
            "inner join beneficio_ws.cuenta c on c.no_cuenta =  p.no_cuenta \n" +
            "where p.no_cuenta =:noCuenta and c.estado_cuenta in(5, 6)", nativeQuery = true)
    List<ParcialidadProjection> obtenerParcialidades(@Param("noCuenta") String noCuenta);
    Optional<Parcialidad> findParcialidadByParcialidadId(UUID parcialidadId);

    @Query(value = "select c.no_cuenta noCuenta, p.parcialidad_id parcialidadId, p.licencia_piloto licenciaPiloto, p.placa_transporte placaTransporte, \n" +
            "p.peso_ingresado pesoIngresado\n" +
            "from beneficio_ws.parcialidad p \n" +
            "inner join beneficio_ws.cuenta c on c.no_cuenta =  p.no_cuenta \n" +
            "where p.no_cuenta =:noCuenta", nativeQuery = true)
    List<AllParcialidadProjection> allParcialidadesByCuenta(@Param("noCuenta") String noCuenta);

    @Query(value = "select c.no_cuenta noCuenta, p.parcialidad_id parcialidadId, p.licencia_piloto licenciaPiloto, p.placa_transporte placaTransporte, p.peso_ingresado  pesoIngresado\n" +
            "from beneficio_ws.cuenta c \n" +
            "inner join beneficio_ws.solicitud s on s.no_solicitud = c.no_solicitud \n" +
            "inner join beneficio_ws.parcialidad p on p.no_cuenta = c.no_cuenta \n" +
            "where s.usuario_solicita =:usuario and c.estado_cuenta in(4)", nativeQuery = true)
    List<AllParcialidadProjection> allParcialidadesByUser(@Param("usuario") String usuario);
}
