package com.gcoce.bc.ws.repositories.beneficio;

import com.gcoce.bc.ws.entities.beneficio.PilotoBc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
@Repository
public interface PilotoBcRepository extends JpaRepository<PilotoBc, String> {
    boolean existsPilotoBcByLicenciaPiloto(String noLicencia);

    Optional<PilotoBc> findPilotoBcByLicenciaPiloto(String noLicencia);
}

