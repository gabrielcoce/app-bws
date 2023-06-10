package com.gcoce.bc.ws.repositories.peso_cabal;

import com.gcoce.bc.ws.entities.peso_cabal.TransportesPC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportePCRespository extends JpaRepository<TransportesPC, String> {
}
