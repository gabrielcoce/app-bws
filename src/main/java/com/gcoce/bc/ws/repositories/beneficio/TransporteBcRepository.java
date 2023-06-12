package com.gcoce.bc.ws.repositories.beneficio;

import com.gcoce.bc.ws.entities.beneficio.TransporteBc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
@Repository
public interface TransporteBcRepository extends JpaRepository<TransporteBc, String> {
    boolean existsTransporteBcByPlacaTransporte(String placaTransporte);

    Optional<TransporteBc> findTransporteBcByPlacaTransporte(String placaTransporte);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update beneficio_ws.transporte set \n" +
            "status=:estadoTransporte, user_updated=:userUpdated, updated_at=now()\n" +
            "where placa_transporte=:placaTransporte", nativeQuery = true)
    Integer putEstadoTransporte(@Param("placaTransporte") String placaTransporte, @Param("estadoTransporte") Boolean estadoTransporte, @Param("userUpdated") String userUpdated);
}
