package com.gcoce.bc.ws.repositories.agricultor;

import com.gcoce.bc.ws.entities.agricultor.Transporte;
import com.gcoce.bc.ws.projections.agricultor.TransporteProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransporteRepository extends JpaRepository<Transporte, String> {

    boolean existsTransporteByPlacaTransporte(String placaTransporte);
    @Query(value = "select t.placa_transporte placaTransporte from agricultor_db.transporte t",
            nativeQuery = true)
    List<TransporteProjection> obtenerTransporte();
}
