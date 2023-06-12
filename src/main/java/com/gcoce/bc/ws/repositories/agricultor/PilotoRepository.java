package com.gcoce.bc.ws.repositories.agricultor;

import com.gcoce.bc.ws.entities.agricultor.Piloto;
import com.gcoce.bc.ws.projections.agricultor.PilotoProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
@Repository
public interface PilotoRepository extends JpaRepository<Piloto, String> {

    boolean existsPilotoByLicenciaPiloto(String noLicencia);
    @Query(value = "select p.licencia_piloto licenciaPiloto, p.nombre from agricultor_db.piloto p",
            nativeQuery = true)
    List<PilotoProjection> obtenerPilotos();
}
