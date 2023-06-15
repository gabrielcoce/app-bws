package com.gcoce.bc.ws.repositories.peso_cabal;

import com.gcoce.bc.ws.entities.peso_cabal.ParcialidadPc;
import com.gcoce.bc.ws.projections.peso_cabal.ParcialidadPcProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 30/05/2023
 */
@Repository
public interface ParcialidadPcRepository extends JpaRepository<ParcialidadPc, UUID> {
    boolean existsParcialidadPcByParcialidadId(UUID parcialidadId);

    boolean existsParcialidadPcByNoCuenta(String noCuenta);

    Optional<ParcialidadPc> findParcialidadPcByParcialidadId(UUID parcialidadId);

    @Query(value = "select p.parcialidad_id parcialidadId, p.peso_ingresado pesoIngresado, p.peso_registrado pesoRegistrado,  \n" +
            "p.peso_excedido pesoExcedido, p.peso_faltante pesoFaltante\n" +
            "from peso_cabal_db.parcialidad p \n" +
            "where p.no_cuenta =:noCuenta", nativeQuery = true)
    List<ParcialidadPcProjection> parcialidadesRegistradas(@Param("noCuenta") String noCuenta);
}
