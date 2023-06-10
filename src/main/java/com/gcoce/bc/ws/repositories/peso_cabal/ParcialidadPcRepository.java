package com.gcoce.bc.ws.repositories.peso_cabal;

import com.gcoce.bc.ws.entities.peso_cabal.ParcialidadPc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Gabriel Coc Estrada
 * @since 30/05/2023
 */
@Repository
public interface ParcialidadPcRepository extends JpaRepository<ParcialidadPc, UUID> {
}
