package com.gcoce.bc.ws.repositories.beneficio;

import com.gcoce.bc.ws.entities.beneficio.TransporteBc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
@Repository
public interface TransporteBcRepository extends JpaRepository<TransporteBc, String> {
    boolean existsTransporteBcByPlacaTransporte(String placaTransporte);

    Optional<TransporteBc> findTransporteBcByPlacaTransporte(String placaTransporte);
}
