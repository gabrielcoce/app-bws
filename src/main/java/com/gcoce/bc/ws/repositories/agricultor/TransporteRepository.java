package com.gcoce.bc.ws.repositories.agricultor;

import com.gcoce.bc.ws.entities.agricultor.Transportes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransporteRepository extends JpaRepository<Transportes, String> {
}
