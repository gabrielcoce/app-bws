package com.gcoce.bc.ws.repositories.beneficio;

import com.gcoce.bc.ws.entities.beneficio.PilotoBc;
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
public interface PilotoBcRepository extends JpaRepository<PilotoBc, String> {
    boolean existsPilotoBcByLicenciaPiloto(String noLicencia);

    Optional<PilotoBc> findPilotoBcByLicenciaPiloto(String noLicencia);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update beneficio_ws.piloto set \n" +
            "estado_piloto=:estadoPiloto, user_updated=:userUpdated, updated_at=now()\n" +
            "where licencia_piloto=:licenciaPiloto", nativeQuery = true)
    Integer putEstadoTransporte(@Param("licenciaPiloto") String licenciaPiloto, @Param("estadoPiloto") Boolean estadoPiloto, @Param("userUpdated") String userUpdated);
}

